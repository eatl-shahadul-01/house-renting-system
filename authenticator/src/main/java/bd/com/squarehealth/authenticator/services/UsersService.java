package bd.com.squarehealth.authenticator.services;

import bd.com.squarehealth.authenticator.dtos.RegistrationDto;
import bd.com.squarehealth.authenticator.dtos.UserDto;
import bd.com.squarehealth.authenticator.dtos.VerificationDto;
import bd.com.squarehealth.authenticator.entities.User;
import bd.com.squarehealth.authenticator.enumerations.UserType;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {

    String generateOtp();
    User findUserByType(UserType userType);
    User createAdminIfNotAvailable() throws Exception;
    User createAdmin(String name, String email, String password) throws Exception;
    UserDto register(RegistrationDto registrationData) throws Exception;
    UserDto verify(VerificationDto verificationData) throws Exception;
}
