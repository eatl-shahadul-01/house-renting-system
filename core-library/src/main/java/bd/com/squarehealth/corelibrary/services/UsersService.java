package bd.com.squarehealth.corelibrary.services;

import bd.com.squarehealth.corelibrary.dtos.RegistrationDto;
import bd.com.squarehealth.corelibrary.dtos.UserDto;
import bd.com.squarehealth.corelibrary.dtos.VerificationDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import bd.com.squarehealth.corelibrary.entities.User;
import bd.com.squarehealth.corelibrary.enumerations.UserRole;

public interface UsersService extends UserDetailsService {

    String generateOtp();
    User findUserByRole(UserRole userRole);
    User createAdminIfNotAvailable() throws Exception;
    User createAdmin(String name, String email, String password) throws Exception;
    UserDto register(RegistrationDto registrationData) throws Exception;
    UserDto verify(VerificationDto verificationData) throws Exception;
}
