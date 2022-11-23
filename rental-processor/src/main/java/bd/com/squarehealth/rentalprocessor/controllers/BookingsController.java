package bd.com.squarehealth.rentalprocessor.controllers;

import bd.com.squarehealth.corelibrary.common.ApiException;
import bd.com.squarehealth.corelibrary.common.ApiResponse;
import bd.com.squarehealth.corelibrary.common.security.AuthenticatedUserData;
import bd.com.squarehealth.corelibrary.dtos.BookingDto;
import bd.com.squarehealth.corelibrary.enumerations.BookingStatus;
import bd.com.squarehealth.corelibrary.services.BookingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/v{version}/bookings")
public class BookingsController {

    @Autowired
    private BookingsService bookingsService;

    @GetMapping(path = "{bookingId}")
    public ApiResponse getCustomersBookingRequestById(
            @PathVariable
            Long bookingId) throws Exception {
        AuthenticatedUserData authenticatedUserData = (AuthenticatedUserData) SecurityContextHolder.getContext().getAuthentication();
        BookingDto booking = bookingsService.findCustomersBookingById(bookingId, authenticatedUserData.getUserId());

        if (booking == null) { throw new ApiException(HttpStatus.NOT_FOUND, "Requested booking data was not found."); }

        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Booking data retrieved successfully.");
        apiResponse.setData("booking", booking);

        return apiResponse;
    }

    @GetMapping
    public ApiResponse getCustomersBookingRequests() {
        AuthenticatedUserData authenticatedUserData = (AuthenticatedUserData) SecurityContextHolder.getContext().getAuthentication();
        List<BookingDto> bookings = bookingsService.findCustomersBookings(authenticatedUserData.getUserId());
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Bookings retrieved successfully.");
        apiResponse.setData("bookings", bookings);

        return apiResponse;
    }

    @PostMapping
    public ApiResponse placeBookingRequest(
            @Valid
            @RequestBody
            BookingDto bookingData) throws Exception {
        if (!bookingData.areCheckInCheckOutDatesValid()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid booking date(s) provided.");
        }

        AuthenticatedUserData authenticatedUserData = (AuthenticatedUserData) SecurityContextHolder.getContext().getAuthentication();
        bookingData.setBookedBy(authenticatedUserData.getUserId());

        BookingDto booking = bookingsService.placeBookingRequest(bookingData);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Successfully placed your booking request.");
        apiResponse.setData("booking", booking);

        return apiResponse;
    }

    // ADMIN ONLY...
    @GetMapping(path = "pending")
    public ApiResponse getPendingBookingRequestsForAdmin() {
        List<BookingDto> bookings = bookingsService.findBookingRequestsByBookingStatus(BookingStatus.PENDING);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Pending booking requests retrieved successfully.");
        apiResponse.setData("bookings", bookings);

        return apiResponse;
    }

    // ADMIN ONLY...
    @PatchMapping(path = "{bookingId}/status/{status}")
    public ApiResponse changeBookingStatus(
            @PathVariable
            Long bookingId,
            @PathVariable
            BookingStatus status) throws Exception {
        BookingDto booking = bookingsService.changeBookingStatusById(bookingId, status);

        if (booking == null) { throw new ApiException(HttpStatus.NOT_FOUND, "Requested booking data was not found."); }

        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Successfully updated booking status to " + status.toString().toLowerCase() + ".");
        apiResponse.setData("booking", booking);

        return apiResponse;
    }
}
