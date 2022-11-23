package bd.com.squarehealth.corelibrary.repositories;

import bd.com.squarehealth.corelibrary.entities.Booking;
import bd.com.squarehealth.corelibrary.enumerations.BookingStatus;
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
