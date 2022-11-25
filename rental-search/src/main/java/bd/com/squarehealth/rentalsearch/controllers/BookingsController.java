package bd.com.squarehealth.rentalsearch.controllers;

import bd.com.squarehealth.corelibrary.common.ApiException;
import bd.com.squarehealth.corelibrary.common.ApiResponse;
import bd.com.squarehealth.corelibrary.common.security.AuthenticatedUserData;
import bd.com.squarehealth.corelibrary.dtos.BookingDto;
import bd.com.squarehealth.corelibrary.services.BookingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/v{version}/bookings")
public class BookingsController {

    @Autowired
    private BookingsService bookingsService;

    @PostMapping
    public ApiResponse placeBookingRequest(
            @Valid
            @RequestBody
            BookingDto bookingData,
            @RequestHeader(name = "Authorization")
            String authorization) throws Exception {
        bookingData.validate();

        AuthenticatedUserData authenticatedUserData = (AuthenticatedUserData) SecurityContextHolder.getContext().getAuthentication();
        bookingData.setBookedBy(authenticatedUserData.getUserId());

        BookingDto booking = bookingsService.placeBookingRequest(bookingData);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Successfully placed your booking request.");
        apiResponse.setData("booking", booking);

        return apiResponse;
    }
}
