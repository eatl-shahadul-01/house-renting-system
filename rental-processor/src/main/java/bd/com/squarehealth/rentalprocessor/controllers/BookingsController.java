package bd.com.squarehealth.rentalprocessor.controllers;

import bd.com.squarehealth.corelibrary.common.ApiException;
import bd.com.squarehealth.corelibrary.common.ApiResponse;
import bd.com.squarehealth.rentalprocessor.dtos.BookingDto;
import bd.com.squarehealth.rentalprocessor.enumerations.BookingStatus;
import bd.com.squarehealth.rentalprocessor.services.BookingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        BookingDto booking = bookingsService.findCustomersBookingById(bookingId, 5L);

        if (booking == null) { throw new ApiException(HttpStatus.NOT_FOUND, "Requested booking data was not found."); }

        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Booking data retrieved successfully.");
        apiResponse.setData("booking", booking);

        return apiResponse;
    }

    @GetMapping
    public ApiResponse getCustomersBookingRequests() {
        List<BookingDto> bookings = bookingsService.findCustomersBookings(5L);
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
