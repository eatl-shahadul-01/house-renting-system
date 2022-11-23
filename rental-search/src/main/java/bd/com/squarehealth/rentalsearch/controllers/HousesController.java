package bd.com.squarehealth.rentalsearch.controllers;

import bd.com.squarehealth.corelibrary.common.ApiResponse;
import bd.com.squarehealth.corelibrary.dtos.HouseSearchCriteriaDto;
import bd.com.squarehealth.corelibrary.services.HousesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v{version}/houses")
public class HousesController {

    @Autowired
    private HousesService housesService;

    @PostMapping
    public ApiResponse searchHouses(
            @RequestBody
            HouseSearchCriteriaDto houseSearchCriteria) throws Exception {
        System.out.println(houseSearchCriteria.toJson());

        houseSearchCriteria.validate();

        return new ApiResponse(HttpStatus.OK, "OK");
    }
}
