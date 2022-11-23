package bd.com.squarehealth.corelibrary.services;

import bd.com.squarehealth.corelibrary.dtos.HouseDto;
import bd.com.squarehealth.corelibrary.dtos.HouseSearchCriteriaDto;

import java.util.List;

public interface HousesService {

    /**
     * Retrieves all the houses that match search criteria.
     * @param houseSearchCriteria Criteria to search houses.
     * @return Returns a list of houses. If none found, returns an empty list.
     */
    List<HouseDto> findPostsByPostStatus(HouseSearchCriteriaDto houseSearchCriteria);
}
