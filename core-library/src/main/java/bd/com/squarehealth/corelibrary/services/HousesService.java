package bd.com.squarehealth.corelibrary.services;

import bd.com.squarehealth.corelibrary.dtos.HouseDto;
import bd.com.squarehealth.corelibrary.dtos.HouseSearchCriteriaDto;

import java.util.List;

public interface HousesService {

    /**
     * Retrieves all the houses that match search criteria.
     * Note: This method utilizes cache.
     * @param houseSearchCriteria Criteria to search houses.
     * @return Returns a list of houses. If none found, returns an empty list.
     */
    List<HouseDto> findHousesByCriteria(HouseSearchCriteriaDto houseSearchCriteria);

    /**
     * Retrieves all the houses that match search criteria.
     * @param houseSearchCriteria Criteria to search houses.
     * @param ignoreCache If true, cache is ignored and data is fetched directly from the database.
     * @return Returns a list of houses. If none found, returns an empty list.
     */
    List<HouseDto> findHousesByCriteria(HouseSearchCriteriaDto houseSearchCriteria, boolean ignoreCache);
}
