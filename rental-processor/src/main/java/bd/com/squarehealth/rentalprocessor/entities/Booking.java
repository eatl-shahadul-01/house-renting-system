package bd.com.squarehealth.rentalprocessor.entities;

import bd.com.squarehealth.rentalprocessor.enumerations.BookingStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Version
    @Column(name = "version")
    private Long version;

    //<!-- VARIABLES FOR BOOKING --!>//
    @Column(name = "bookingStatus")
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus = BookingStatus.NONE;

    @Column(name = "checkInDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkInDate;

    @Column(name = "checkOutDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkOutDate;

    @Column(name = "bookedBy")
    private Long bookedBy;

    @ManyToOne
    @JoinColumn(name="houseId", nullable=false)
    private House house;
}
