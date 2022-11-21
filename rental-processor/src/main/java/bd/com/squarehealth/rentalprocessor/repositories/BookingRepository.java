package bd.com.squarehealth.rentalprocessor.repositories;

import bd.com.squarehealth.rentalprocessor.entities.Booking;
import bd.com.squarehealth.rentalprocessor.enumerations.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByIdAndBookedBy(Long id, Long bookedBy);
    List<Booking> findByBookedBy(Long bookedBy);
    List<Booking> findByBookingStatus(BookingStatus bookingStatus);
    List<Booking> findByHouse_Id(Long houseId);
}
