package bd.com.squarehealth.corelibrary.dtos;

import bd.com.squarehealth.corelibrary.common.Mapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
public class AddressDto implements Mapper {

    private Long id;

    private Long version;

    @NotNull(message = "Latitude must be provided.")
    @Range(min = -90, max = 90, message = "Latitude must be between -90 and 90.")
    private Double latitude;

    @NotNull(message = "Longitude must be provided.")
    @Range(min = -180, max = 180, message = "Longitude must be between -180 and 180.")
    private Double longitude;

    @NotBlank(message = "House no. is required.")
    @Size(min = 1, max = 16, message = "The length of house no. must be between 1 and 16 characters.")
    private String house;

    @NotBlank(message = "Street is required.")
    @Size(min = 1, max = 64, message = "The length of street must be between 1 and 64 characters.")
    private String street;

    @NotBlank(message = "Zip code is required.")
    @Size(min = 1, max = 16, message = "The length of zip code must be between 1 and 16 characters.")
    private String zipCode;

    @NotBlank(message = "Area name is required.")
    @Size(min = 1, max = 64, message = "The length of area must be between 1 and 64 characters.")
    private String area;

    @NotBlank(message = "City is required.")
    @Size(min = 1, max = 64, message = "The length of city must be between 1 and 64 characters.")
    private String city;

    @NotBlank(message = "Country is required.")
    @Size(min = 1, max = 64, message = "The length of country must be between 1 and 64 characters.")
    private String country;

    public AddressDto(Object object) {
        AddressDto addressData = object instanceof AddressDto
                ? (AddressDto) object                   // if the object is an instance of AddressDto class...
                : (AddressDto) fromObject(object);      // if the object is not an instance of AddressDto class...
        id = addressData.id;
        version = addressData.version;
        house = addressData.house;
        zipCode = addressData.zipCode;
        area = addressData.area;
        city = addressData.city;
        country = addressData.country;
        latitude = addressData.latitude;
        longitude = addressData.longitude;
    }
}
