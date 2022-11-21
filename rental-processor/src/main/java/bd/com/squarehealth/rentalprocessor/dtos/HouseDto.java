package bd.com.squarehealth.rentalprocessor.dtos;

import bd.com.squarehealth.corelibrary.common.Mapper;
import bd.com.squarehealth.rentalprocessor.enumerations.PostStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
public class HouseDto implements Mapper {

    private Long id;

    private Long version;

    @Range(min = 3500000L, max = 25000000L, message = "Price must be greater than or equal to 3500000 and less than or equal to 25000000.")
    private double price;

    @NotBlank(message = "Description must be provided.")
    @Size(min = 1, max = 512, message = "The length of the description must be between 1 and 512 characters long.")
    private String description;

    @NotNull(message = "Address must be provided.")
    @Valid()
    private AddressDto address;

    private PostStatus postStatus = PostStatus.NONE;

    public HouseDto(Object object) {
        HouseDto houseData = object instanceof HouseDto
                ? (HouseDto) object                   // if the object is an instance of HouseDto class...
                : (HouseDto) fromObject(object);      // if the object is not an instance of HouseDto class...
        id = houseData.id;
        version = houseData.version;
        price = houseData.price;
        description = houseData.description;
        address = houseData.address;
        postStatus = houseData.postStatus;
    }
}
