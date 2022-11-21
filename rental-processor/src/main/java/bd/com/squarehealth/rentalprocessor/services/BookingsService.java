package bd.com.squarehealth.rentalprocessor.services;

import bd.com.squarehealth.rentalprocessor.dtos.BookingDto;
import bd.com.squarehealth.rentalprocessor.enumerations.BookingStatus;

import java.util.List;

public interface BookingsService {

    BookingDto findCustomersBookingById(Long bookingId, Long bookedBy);

    List<BookingDto> findCustomersBookings(Long bookedBy);

    BookingDto changeBookingStatusById(Long bookingId, BookingStatus bookingStatus);

    List<BookingDto> findBookingRequestsByBookingStatus(BookingStatus bookingStatus);

    BookingDto placeBookingRequest(BookingDto bookingData) throws Exception;
}
