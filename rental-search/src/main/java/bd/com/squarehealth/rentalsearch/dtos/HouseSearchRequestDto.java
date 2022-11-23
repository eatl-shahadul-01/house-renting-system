package bd.com.squarehealth.rentalsearch.dtos;

import bd.com.squarehealth.corelibrary.common.Mapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Size;
import java.util.Date;

@NoArgsConstructor
@Data
public class HouseSearchRequestDto implements Mapper {

    private Date checkInDate;
    private Date checkOutDate;
    private Double minimumPrice;
    private Double maximumPrice;

    @Range(min = -90, max = 90, message = "Latitude must be between -90 and 90.")
    private Double latitude;

    @Range(min = -180, max = 180, message = "Longitude must be between -180 and 180.")
    private Double longitude;

    @Size(min = 1, max = 16, message = "The length of house no. must be between 1 and 16 characters.")
    private String house;

    @Size(min = 1, max = 64, message = "The length of street must be between 1 and 64 characters.")
    private String street;

    @Size(min = 1, max = 16, message = "The length of zip code must be between 1 and 16 characters.")
    private String zipCode;

    @Size(min = 1, max = 64, message = "The length of area must be between 1 and 64 characters.")
    private String area;

    @Size(min = 1, max = 64, message = "The length of city must be between 1 and 64 characters.")
    private String city;

    @Size(min = 1, max = 64, message = "The length of country must be between 1 and 64 characters.")
    private String country;

    // repeated code...
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

    public boolean areDataValid() {
        if (maximumPrice < minimumPrice) { return false; }
        if (!areCheckInCheckOutDatesValid()) { return false; }

        return true;
    }
}
