package bd.com.squarehealth.corelibrary.entities;

import bd.com.squarehealth.corelibrary.enumerations.BookingStatus;
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
    @Temporal(TemporalType.DATE)
    private Date checkInDate;

    @Column(name = "checkOutDate")
    @Temporal(TemporalType.DATE)
    private Date checkOutDate;

    @Column(name = "bookedBy")
    private Long bookedBy;

    @ManyToOne
    @JoinColumn(name="houseId", nullable=false)
    private House house;
}
