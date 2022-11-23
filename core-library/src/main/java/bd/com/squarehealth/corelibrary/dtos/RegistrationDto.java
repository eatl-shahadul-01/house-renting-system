package bd.com.squarehealth.corelibrary.dtos;

import bd.com.squarehealth.corelibrary.common.Mapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
public class RegistrationDto implements Mapper {

    private Long id;

    private Long version;

    @NotBlank(message = "Name is required.")
    @Size(min = 1, max = 512, message = "The length of name must be between 1 and 512 characters.")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email address.")      // might need to add custom regular expression for email validation...
    @Size(min = 1, max = 512, message = "The length of email must be between 1 and 512 characters.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 64, message = "The length of password must be between 8 and 64 characters.")
    private String password;

    @NotBlank(message = "Confirmation password is required.")
    @Size(min = 8, max = 64, message = "The length of confirmation password must be between 8 and 64 characters.")
    private String confirmationPassword;

    public RegistrationDto(Object object) {
        RegistrationDto userRegistrationData = object instanceof RegistrationDto
                ? (RegistrationDto) object                   // if the object is an instance of UserRegistrationDto class...
                : (RegistrationDto) fromObject(object);      // if the object is not an instance of UserRegistrationDto class...
        id = userRegistrationData.id;
        version = userRegistrationData.version;
        email = userRegistrationData.email;
        password = userRegistrationData.password;
        confirmationPassword = userRegistrationData.confirmationPassword;
    }

    public boolean arePasswordsValid() {
        // checks if both passwords are equal...
        return password.equals(confirmationPassword);
    }
}
