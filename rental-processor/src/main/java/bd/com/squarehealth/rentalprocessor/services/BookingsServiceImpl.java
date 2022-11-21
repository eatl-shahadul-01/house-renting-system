package bd.com.squarehealth.rentalprocessor.services;

import bd.com.squarehealth.corelibrary.common.ApiException;
import bd.com.squarehealth.corelibrary.common.json.JsonSerializer;
import bd.com.squarehealth.corelibrary.common.json.JsonSerializerImpl;
import bd.com.squarehealth.rentalprocessor.dtos.BookingDto;
import bd.com.squarehealth.rentalprocessor.dtos.PostDto;
import bd.com.squarehealth.rentalprocessor.entities.Booking;
import bd.com.squarehealth.rentalprocessor.entities.House;
import bd.com.squarehealth.rentalprocessor.enumerations.BookingStatus;
import bd.com.squarehealth.rentalprocessor.enumerations.PostStatus;
import bd.com.squarehealth.rentalprocessor.repositories.BookingRepository;
import bd.com.squarehealth.rentalprocessor.repositories.HouseRepository;
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

    private boolean doesTimeOverlap(Date fromA, Date toA, Date fromB, Date toB) {
        return toA.getTime() >= fromB.getTime() && fromA.getTime() <= toB.getTime();
    }

    private boolean isHouseAvailable(House house, Date from, Date to) {
        // if the house post is not approved, the house shall not be available for booking...
        // if (house.getPostStatus() != PostStatus.APPROVED) { return false; }
        Set<Booking> bookings = house.getBookings();

        // if no booking information is associated with the house,
        // it means the house is available to be booked...
        if (bookings == null || bookings.size() == 0) { return true; }

        System.out.println("AVAILABLE");

        for (Booking booking : bookings) {
            if (doesTimeOverlap(booking.getCheckInDate(), booking.getCheckOutDate(), from, to)) {
                System.out.println("NOT AVAILABLE");

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
            throw new ApiException(HttpStatus.BAD_REQUEST, "Requested house is not available.");
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
    public BookingDto changeBookingStatusById(Long bookingId, BookingStatus bookingStatus) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);

        if (!optionalBooking.isPresent()) { return null; }

        Booking booking = optionalBooking.get();
        booking.setBookingStatus(bookingStatus);

        bookingRepository.save(booking);

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
