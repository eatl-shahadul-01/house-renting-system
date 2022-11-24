package bd.com.squarehealth.corelibrary.dtos;

import bd.com.squarehealth.corelibrary.common.ApiException;
import bd.com.squarehealth.corelibrary.common.DateUtilities;
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

    private static final String DEFAULT_STRING_VALUE = "no-criteria";

    public void validate() throws Exception {
        if (minimumPrice != null && maximumPrice != null && maximumPrice < minimumPrice) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Minimum price must be less than the maximum price.");
        }

        if (checkInDate != null && (DateUtilities.isValidFutureDate(checkInDate)
                || DateUtilities.isValidFutureDate(checkInDate))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid check-in date provided.");
        }

        if (checkOutDate != null && (DateUtilities.isValidFutureDate(checkOutDate)
                || DateUtilities.isValidFutureDate(checkOutDate))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid check-out date provided.");
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(1024);
        stringBuilder.append(checkInDate == null ? "" : "checkInDate=" + checkInDate.getTime() + "&");
        stringBuilder.append(checkOutDate == null ? "" : "checkOutDate=" + checkOutDate.getTime() + "&");
        stringBuilder.append(minimumPrice == null ? "" : "minimumPrice=" + minimumPrice + "&");
        stringBuilder.append(maximumPrice == null ? "" : "maximumPrice=" + maximumPrice + "&");
        stringBuilder.append(latitude == null ? "" : "latitude=" + latitude + "&");
        stringBuilder.append(longitude == null ? "" : "longitude=" + longitude + "&");
        stringBuilder.append(house == null ? "" : "house=" + house + "&");
        stringBuilder.append(street == null ? "" : "street=" + street + "&");
        stringBuilder.append(zipCode == null ? "" : "zipCode=" + zipCode + "&");
        stringBuilder.append(area == null ? "" : "area=" + area + "&");
        stringBuilder.append(city == null ? "" : "city=" + city + "&");
        stringBuilder.append(country == null ? "" : "country=" + country);

        String text = stringBuilder.toString();

        // if text is of length zero, we'll return a default value...
        if (text.length() == 0) { return DEFAULT_STRING_VALUE; }

        return text;
    }
}
