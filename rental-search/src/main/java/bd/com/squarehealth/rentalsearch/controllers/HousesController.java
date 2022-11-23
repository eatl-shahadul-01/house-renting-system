package bd.com.squarehealth.rentalsearch.controllers;

import bd.com.squarehealth.corelibrary.common.ApiResponse;
import bd.com.squarehealth.rentalsearch.dtos.HouseSearchRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HousesController {

    @PostMapping(path = "/search")
    public ApiResponse search(
            @RequestBody
            HouseSearchRequestDto postSearchRequestData) {
        System.out.println(postSearchRequestData);

        return new ApiResponse(HttpStatus.OK, "OK");
    }
}
