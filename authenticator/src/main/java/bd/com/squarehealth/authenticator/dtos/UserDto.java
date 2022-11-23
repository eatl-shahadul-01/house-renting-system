package bd.com.squarehealth.authenticator.dtos;

import bd.com.squarehealth.authenticator.enumerations.UserType;
import bd.com.squarehealth.corelibrary.common.Mapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
public class UserDto implements Mapper {

    private Long id;

    private Long version;

    private Boolean isVerified;

    @Size(min = 1, max = 64, message = "The length of name must be between 1 and 64 characters.")
    private String name;

    @NotBlank(message = "Email is required.")
    @Size(min = 1, max = 512, message = "The length of email must be between 1 and 512 characters.")
    private String email;

    private UserType userType = UserType.NONE;

    public UserDto(Object object) {
        UserDto userData = object instanceof UserDto
                ? (UserDto) object                   // if the object is an instance of UserDto class...
                : (UserDto) fromObject(object);      // if the object is not an instance of UserDto class...
        id = userData.id;
        version = userData.version;
        isVerified = userData.isVerified;
        name = userData.name;
        email = userData.email;
        userType = userData.userType;
    }
}
