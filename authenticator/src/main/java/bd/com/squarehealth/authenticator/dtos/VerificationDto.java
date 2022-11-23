package bd.com.squarehealth.authenticator.dtos;

import bd.com.squarehealth.corelibrary.common.Mapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
public class VerificationDto implements Mapper {

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email address.")      // might need to add custom regular expression for email validation...
    @Size(min = 1, max = 512, message = "The length of email must be between 1 and 512 characters.")
    private String email;

    @NotBlank(message = "OTP is required.")
    @Size(min = 1, max = 6, message = "OTP must be exactly six characters long.")
    private String otp;

    public VerificationDto(Object object) {
        VerificationDto verificationData = object instanceof VerificationDto
                ? (VerificationDto) object                   // if the object is an instance of VerificationDto class...
                : (VerificationDto) fromObject(object);      // if the object is not an instance of VerificationDto class...
        email = verificationData.email;
        otp = verificationData.otp;
    }
}
