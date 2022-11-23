package bd.com.squarehealth.corelibrary.dtos;

import bd.com.squarehealth.corelibrary.common.Mapper;
import bd.com.squarehealth.corelibrary.enumerations.BookingStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@NoArgsConstructor
@Data
public class BookingDto implements Mapper {

    private Long id;

    @NotNull(message = "House ID must be provided.")
    private Long houseId;

    private Long version;

    private BookingStatus bookingStatus = BookingStatus.NONE;

    @NotNull(message = "Check-in date must be provided.")
    private Date checkInDate;

    @NotNull(message = "Check-out date must be provided.")
    private Date checkOutDate;

    private Long bookedBy;

    private HouseDto house;

    public BookingDto(Object object) {
        BookingDto bookingData = object instanceof BookingDto
                ? (BookingDto) object                   // if the object is an instance of BookingDto class...
                : (BookingDto) fromObject(object);      // if the object is not an instance of BookingDto class...
        id = bookingData.id;
        houseId = bookingData.houseId;
        version = bookingData.version;
        bookingStatus = bookingData.bookingStatus;
        checkInDate = bookingData.checkInDate;
        checkOutDate = bookingData.checkOutDate;
        bookedBy = bookingData.bookedBy;
        house = bookingData.house;
    }

    public boolean areCheckInCheckOutDatesValid() {
        // checks if check in date is greater than or equal to check out date...
        if (checkInDate == null || checkOutDate == null
                || checkInDate.getTime() >= checkOutDate.getTime()) { return false; }

        Date currentDate = new Date();
        long currentDateInMilliseconds = currentDate.getTime() + 1;

        // checks if check in and check out dates are less than current date...
        if (checkInDate.getTime() < currentDateInMilliseconds
                || checkOutDate.getTime() < currentDateInMilliseconds) { return false; }

        return true;
    }
}
