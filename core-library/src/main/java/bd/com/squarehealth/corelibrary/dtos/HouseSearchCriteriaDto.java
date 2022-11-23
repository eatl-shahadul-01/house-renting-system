package bd.com.squarehealth.corelibrary.dtos;

import bd.com.squarehealth.corelibrary.common.ApiException;
import bd.com.squarehealth.corelibrary.common.Mapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.Size;
import java.util.Date;

@NoArgsConstructor
@Data
public class HouseSearchCriteriaDto implements Mapper {

    private Date checkInDate;
    private Date checkOutDate;

    @Range(min = 3500000L, max = 25000000L, message = "Price must be greater than or equal to 3500000 and less than or equal to 25000000.")
    private Double minimumPrice;

    @Range(min = 3500000L, max = 25000000L, message = "Price must be greater than or equal to 3500000 and less than or equal to 25000000.")
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

    public void validate() throws Exception {
        if (maximumPrice < minimumPrice) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Minimum price must be less than the maximum price.");
        }

        if (!areCheckInCheckOutDatesValid()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid check-in/check-out dates provided.");
        }
    }
}
