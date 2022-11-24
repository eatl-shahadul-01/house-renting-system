package bd.com.squarehealth.corelibrary.dtos;

import bd.com.squarehealth.corelibrary.common.ApiException;
import bd.com.squarehealth.corelibrary.common.DateUtilities;
import bd.com.squarehealth.corelibrary.common.Mapper;
import bd.com.squarehealth.corelibrary.enumerations.BookingStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

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

    public void validate() throws Exception {
        if (checkInDate == null || DateUtilities.isValidFutureDate(checkInDate)
                || DateUtilities.isValidFutureDate(checkInDate)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid check-in date provided.");
        }

        if (checkOutDate == null || DateUtilities.isValidFutureDate(checkOutDate)
                || DateUtilities.isValidFutureDate(checkOutDate)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid check-out date provided.");
        }
    }
}
