package bd.com.squarehealth.corelibrary.services;

import bd.com.squarehealth.corelibrary.common.ApiException;
import bd.com.squarehealth.corelibrary.common.ApiResponse;
import bd.com.squarehealth.corelibrary.common.json.JsonSerializer;
import bd.com.squarehealth.corelibrary.dtos.BookingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ISCServiceImpl implements ISCService {

    @Autowired
    private JsonSerializer jsonSerializer;

    @Autowired
    private RestTemplate restTemplate;

    private static final String RENTAL_PROCESSOR_BOOKING_REQUEST_URL = "http://127.0.0.1:61302/api/rental-processor/v1.0/bookings";

    private <Type> List<BookingDto> toBookings(List<Type> list) {
        List<BookingDto> bookings = new ArrayList<>(list.size());

        for (Type element : list) {
            bookings.add(new BookingDto(element));
        }

        return bookings;
    }

    @Override
    public BookingDto placeBookingRequest(BookingDto bookingData, String authorization) throws Exception {
        // Note 1: Eureka isn't used to avoid complexity and save time...
        // Note 2: This method shall be refactored to increase code re-usability...
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(HttpHeaders.AUTHORIZATION, authorization);

        HttpEntity<String> httpEntity = new HttpEntity<>(bookingData.toJson(), httpHeaders);

        String apiResponseAsJson;

        try {
            apiResponseAsJson = restTemplate.postForObject(RENTAL_PROCESSOR_BOOKING_REQUEST_URL, httpEntity, String.class);
        } catch (HttpClientErrorException httpClientErrorException) {
            apiResponseAsJson = httpClientErrorException.getResponseBodyAsString();
        }

        Map<String, Object> apiResponseAsMap = jsonSerializer.deserialize(apiResponseAsJson, new HashMap<String, Object>().getClass());
        ApiResponse apiResponse = new ApiResponse(apiResponseAsMap);

        if (apiResponse.getHttpStatus() != HttpStatus.OK) {
            throw new ApiException(apiResponse.getHttpStatus(), apiResponse.getMessage());
        }

        BookingDto createdBookingData = jsonSerializer.transform(apiResponse.getData().get("booking"), BookingDto.class);

        return createdBookingData;
    }
}
