package bd.com.squarehealth.authenticator.dtos;

import bd.com.squarehealth.corelibrary.common.Mapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
public class LoginDto implements Mapper {

    @NotBlank(message = "Email is required.")
    @Size(min = 1, max = 512, message = "The length of email must be between 1 and 512 characters.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 64, message = "The length of password must be between 8 and 64 characters.")
    private String password;

    public LoginDto(Object object) {
        LoginDto userLoginData = object instanceof LoginDto
                ? (LoginDto) object                   // if the object is an instance of UserRegistrationDto class...
                : (LoginDto) fromObject(object);      // if the object is not an instance of UserRegistrationDto class...
        email = userLoginData.email;
        password = userLoginData.password;
    }
}
