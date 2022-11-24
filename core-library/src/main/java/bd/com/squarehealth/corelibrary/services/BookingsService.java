package bd.com.squarehealth.corelibrary.services;

import bd.com.squarehealth.corelibrary.dtos.BookingDto;
import bd.com.squarehealth.corelibrary.entities.House;
import bd.com.squarehealth.corelibrary.enumerations.BookingStatus;

import java.util.Date;
import java.util.List;

public interface BookingsService {

    boolean isHouseAvailable(House house, Date from, Date to);

    BookingDto findCustomersBookingById(Long bookingId, Long bookedBy);

    List<BookingDto> findCustomersBookings(Long bookedBy);

    BookingDto changeBookingStatusById(Long bookingId, BookingStatus bookingStatus);

    List<BookingDto> findBookingRequestsByBookingStatus(BookingStatus bookingStatus);

    BookingDto placeBookingRequest(BookingDto bookingData) throws Exception;
}
