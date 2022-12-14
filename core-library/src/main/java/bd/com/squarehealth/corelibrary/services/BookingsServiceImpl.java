package bd.com.squarehealth.corelibrary.services;

import bd.com.squarehealth.corelibrary.common.ApiException;
import bd.com.squarehealth.corelibrary.common.DateUtilities;
import bd.com.squarehealth.corelibrary.common.json.JsonSerializer;
import bd.com.squarehealth.corelibrary.dtos.BookingDto;
import bd.com.squarehealth.corelibrary.entities.Booking;
import bd.com.squarehealth.corelibrary.entities.House;
import bd.com.squarehealth.corelibrary.enumerations.BookingStatus;
import bd.com.squarehealth.corelibrary.enumerations.PostStatus;
import bd.com.squarehealth.corelibrary.repositories.BookingRepository;
import bd.com.squarehealth.corelibrary.repositories.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookingsServiceImpl implements BookingsService {

    @Autowired
    private JsonSerializer jsonSerializer;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private <Type> List<BookingDto> toBookings(List<Type> list) {
        List<BookingDto> bookings = new ArrayList<>(list.size());

        for (Type element : list) {
            bookings.add(new BookingDto(element));
        }

        return bookings;
    }

    public boolean isHouseAvailable(House house, Date from, Date to) {
        // if the house post is not approved, the house shall not be available for booking...
        // if (house.getPostStatus() != PostStatus.APPROVED) { return false; }
        Set<Booking> bookings = house.getBookings();

        // if no booking information is associated with the house,
        // it means the house is available to be booked...
        if (bookings == null || bookings.size() == 0) { return true; }

        for (Booking booking : bookings) {
            // booking requests that were cancelled or rejected shall not be taken into consideration...
            // so, only if the booking is (pending or approved) and dates overlap,
            // the house shall not be available for booking...
            if ((booking.getBookingStatus() == BookingStatus.PENDING || booking.getBookingStatus() == BookingStatus.APPROVED)
                    && DateUtilities.doDatesOverlap(booking.getCheckInDate(), booking.getCheckOutDate(), from, to)) {
                return false;
            }
        }

        return true;
    }

    private House findAvailableHouseByHouseIdAndDateRange(Long houseId, Date from, Date to) throws Exception {
        Optional<House> optionalHouse = houseRepository.findById(houseId);

        if (!optionalHouse.isPresent()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Requested house was not found.");
        }

        House house = optionalHouse.get();

        if (house.getPostStatus() != PostStatus.APPROVED) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Requested house was not found.");
        }

        boolean isHouseAvailable = isHouseAvailable(house, from, to);

        if (!isHouseAvailable) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "This house is not available for booking on your requested dates.");
        }

        return house;
    }

    @Override
    public BookingDto findCustomersBookingById(Long bookingId, Long bookedBy) {
        Optional<Booking> optionalBooking = bookingRepository.findByIdAndBookedBy(bookingId, bookedBy);

        if (!optionalBooking.isPresent()) { return null; }

        BookingDto bookingData = new BookingDto(optionalBooking.get());

        return bookingData;
    }

    @Override
    public List<BookingDto> findCustomersBookings(Long bookedBy) {
        List<Booking> bookings = bookingRepository.findByBookedBy(bookedBy);
        List<BookingDto> bookingDataList = toBookings(bookings);

        return bookingDataList;
    }

    @Override
    public BookingDto changeBookingStatusById(Long bookingId, BookingStatus bookingStatus) throws ApiException {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);

        if (!optionalBooking.isPresent()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Requested booking data was not found.");
        }

        Booking booking = optionalBooking.get();

        // if the booking in not in pending state nor in approved state, we won't proceed further...
        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Booking was cancelled by the customer.");
        }

        // changes the booking status...
        booking.setBookingStatus(bookingStatus);
        // saves to the database...
        booking = bookingRepository.save(booking);

        BookingDto bookingData = new BookingDto(booking);

        return bookingData;
    }

    @Override
    public BookingDto cancelCustomersBookingById(Long bookingId, Long bookedBy) throws ApiException {
        Optional<Booking> optionalBooking = bookingRepository.findByIdAndBookedBy(bookingId, bookedBy);

        if (!optionalBooking.isPresent()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Requested booking data was not found.");
        }

        Booking booking = optionalBooking.get();

        // if the booking in not in pending state nor in approved state, we won't proceed further...
        if (booking.getBookingStatus() != BookingStatus.PENDING &&
                booking.getBookingStatus() != BookingStatus.APPROVED) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Booking cannot be cancelled.");
        }

        Date sevenDaysBeforeBookingDate = DateUtilities.subtractDays(booking.getCheckInDate(), 7);

        // booking can be cancelled seven days before the check-in date...
        if (DateUtilities.isValidFutureDate(sevenDaysBeforeBookingDate)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Booking can only be cancelled seven days before the check-in date.");
        }

        // cancels the booking...
        booking.setBookingStatus(BookingStatus.CANCELLED);
        // saves to the database...
        booking = bookingRepository.save(booking);

        BookingDto bookingData = new BookingDto(booking);

        return bookingData;
    }

    @Override
    public List<BookingDto> findBookingRequestsByBookingStatus(BookingStatus bookingStatus) {
        List<Booking> bookings = bookingRepository.findByBookingStatus(bookingStatus);
        List<BookingDto> bookingDataList = toBookings(bookings);

        return bookingDataList;
    }

    @Override
    public BookingDto placeBookingRequest(BookingDto bookingData) throws Exception {
        bookingData.setHouse(null);

        House house = findAvailableHouseByHouseIdAndDateRange(bookingData.getHouseId(),
                bookingData.getCheckInDate(), bookingData.getCheckOutDate());
        Booking booking = bookingData.toObject(Booking.class);
        booking.setHouse(house);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking = bookingRepository.save(booking);

        BookingDto createdBookingData = new BookingDto(booking);

        return createdBookingData;
    }
}
