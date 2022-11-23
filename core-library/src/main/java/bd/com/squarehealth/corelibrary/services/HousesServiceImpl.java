package bd.com.squarehealth.corelibrary.services;

import bd.com.squarehealth.corelibrary.dtos.HouseDto;
import bd.com.squarehealth.corelibrary.dtos.HouseSearchCriteriaDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HousesServiceImpl implements HousesService {

    @Override
    public List<HouseDto> findPostsByPostStatus(HouseSearchCriteriaDto houseSearchCriteria) {
        return null;
    }
}
