package bd.com.squarehealth.rentalsearch.controllers;

import bd.com.squarehealth.corelibrary.common.ApiResponse;
import bd.com.squarehealth.corelibrary.dtos.HouseDto;
import bd.com.squarehealth.corelibrary.dtos.HouseSearchCriteriaDto;
import bd.com.squarehealth.corelibrary.services.HousesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v{version}/houses")
public class HousesController {

    @Autowired
    private HousesService housesService;

    @PostMapping
    public ApiResponse searchHouses(
            @RequestParam(required = false, defaultValue = "false")
            boolean ignoreCache,
            @RequestBody
            HouseSearchCriteriaDto houseSearchCriteria) throws Exception {
        houseSearchCriteria.validate();

        List<HouseDto> houses = housesService.findHousesByCriteria(houseSearchCriteria, ignoreCache);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Search results retrieved successfully.");
        apiResponse.setData("houses", houses);

        return apiResponse;
    }
}
