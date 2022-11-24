package bd.com.squarehealth.corelibrary.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Version
    private Long version;

    @NotNull
    @Range(min = -90, max = 90, message = "Latitude must be between -90 and 90.")
    private Double latitude;

    @NotNull
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
}
