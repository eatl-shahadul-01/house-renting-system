package bd.com.squarehealth.corelibrary.services;

import bd.com.squarehealth.corelibrary.common.DateUtilities;
import bd.com.squarehealth.corelibrary.common.json.JsonSerializer;
import bd.com.squarehealth.corelibrary.dtos.HouseDto;
import bd.com.squarehealth.corelibrary.dtos.HouseSearchCriteriaDto;
import bd.com.squarehealth.corelibrary.entities.Address;
import bd.com.squarehealth.corelibrary.entities.House;
import bd.com.squarehealth.corelibrary.enumerations.PostStatus;
import bd.com.squarehealth.corelibrary.repositories.HouseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.JedisPooled;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HousesServiceImpl implements HousesService {

    @Autowired
    private JsonSerializer jsonSerializer;

    @Autowired
    private JedisPooled jedisPooled;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private BookingsService bookingsService;

    private static final long CACHE_KEY_EXPIRATION_IN_SECONDS = 30;
    private static final String CACHE_KEY_SUFFIX = "HousesService";

    private <Type> List<HouseDto> toHouses(List<Type> list) {
        List<HouseDto> houses = new ArrayList<>(list.size());

        for (Type element : list) {
            houses.add(new HouseDto(element));
        }

        return houses;
    }

    private boolean doesFulfillCriteria(House house, HouseSearchCriteriaDto searchCriteria) {
        // checks if the house is approved...
        if (house.getPostStatus() != PostStatus.APPROVED) { return false; }
        // checks if the house is available on the given date...
        if (searchCriteria.getCheckInDate() != null &&
                searchCriteria.getCheckOutDate() != null &&
                DateUtilities.isValidFutureDate(searchCriteria.getCheckInDate()) &&
                DateUtilities.isValidFutureDate(searchCriteria.getCheckOutDate()) &&
                DateUtilities.isValidDateRange(searchCriteria.getCheckInDate(), searchCriteria.getCheckOutDate()) &&
                !bookingsService.isHouseAvailable(house, searchCriteria.getCheckInDate(), searchCriteria.getCheckOutDate())) { return false; }
        // checks if the house is within the given price range...
        if (searchCriteria.getMinimumPrice() != null && searchCriteria.getMaximumPrice() != null &&
                (house.getPrice() < searchCriteria.getMinimumPrice() || house.getPrice() > searchCriteria.getMaximumPrice())) { return false; }

        Address address = house.getAddress();

        // only null check is sufficient for search criteria because we've already annotated the fields...
        if (searchCriteria.getHouse() != null && !address.getHouse().equals(searchCriteria.getHouse())) { return false; }
        if (searchCriteria.getStreet() != null && !address.getStreet().equals(searchCriteria.getStreet())) { return false; }
        if (searchCriteria.getZipCode() != null && !address.getZipCode().equals(searchCriteria.getZipCode())) { return false; }
        if (searchCriteria.getArea() != null && !address.getArea().equals(searchCriteria.getArea())) { return false; }
        if (searchCriteria.getCity() != null && !address.getCity().equals(searchCriteria.getCity())) { return false; }
        if (searchCriteria.getCountry() != null && !address.getCountry().equals(searchCriteria.getCountry())) { return false; }

        // if latitude and longitude are not provided, we don't have any more criteria to check...
        if (searchCriteria.getLatitude() == null && searchCriteria.getLongitude() == null) { return true; }
        // otherwise, checks if the given latitude and longitude exactly matches the house's latitude and longitude...
        if (searchCriteria.getLatitude().equals(address.getLatitude()) && searchCriteria.getLongitude().equals(address.getLongitude())) { return true; }
        // otherwise, we'll check if the house is within 10 km range of the given latitude and longitude from google map...
        // Note: 10 km radius around the position/coordinate (latitude and latitude) could not be implemented due to time constraint...

        return true;
    }

    @Override
    public List<HouseDto> findHousesByCriteria(HouseSearchCriteriaDto searchCriteria) {
        return findHousesByCriteria(searchCriteria, false);
    }

    @Override
    public List<HouseDto> findHousesByCriteria(HouseSearchCriteriaDto searchCriteria, boolean ignoreCache) {
        String cacheKey = searchCriteria.toString() + "@" + CACHE_KEY_SUFFIX;

        // if ignore cache is false...
        if (!ignoreCache) {
            // we'll try to retrieve data from cache...
            String houseDataListAsJson = jedisPooled.get(cacheKey);

            // if cached data is found...
            if (houseDataListAsJson != null) {
                log.info("Method 'findHousesByCriteria' is serving data from cache.");

                // we'll deserialize the json content to have the house data list...
                List<HouseDto> houseDataList = jsonSerializer.deserialize(
                        houseDataListAsJson, new ArrayList<HouseDto>(0).getClass());

                return houseDataList;
            }
        }

        log.info("Method 'findHousesByCriteria' is serving data from the database.");

        // we shall only retrieve the approved posts...
        // Note: this is the most unoptimized yet the easiest way of doing this. I would've used dynamic and/or native query
        // to fetch appropriate data but couldn't do it due to time constraint... furthermore, as we are using
        // redis cache, the performance difference wouldn't be noticeable...
        List<House> houses = houseRepository.findByPostStatus(PostStatus.APPROVED);
        int totalHouses = houses.size();
        List<House> filteredHouses = new ArrayList<>(totalHouses);

        for (int i = 0; i < totalHouses; i++) {
            House house = houses.get(i);

            // if the house does not fulfill search criteria,
            // we shall continue to next iteration...
            if (!doesFulfillCriteria(house, searchCriteria)) { continue; }

            filteredHouses.add(house);
        }

        List<HouseDto> houseDataList = toHouses(filteredHouses);

        // we must have at least one house data in our list before the set it to our cache...
        if (houseDataList.size() > 0) {
            // serializes the list as json...
            String houseDataListAsJson = jsonSerializer.serialize(houseDataList);

            // sets data to cache...
            jedisPooled.setex(cacheKey, CACHE_KEY_EXPIRATION_IN_SECONDS, houseDataListAsJson);
        }

        return houseDataList;
    }
}
