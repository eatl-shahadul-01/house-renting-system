package bd.com.squarehealth.authenticator.services;

import bd.com.squarehealth.authenticator.dtos.RegistrationDto;
import bd.com.squarehealth.authenticator.dtos.UserDto;
import bd.com.squarehealth.authenticator.dtos.VerificationDto;
import bd.com.squarehealth.authenticator.entities.User;
import bd.com.squarehealth.authenticator.enumerations.UserType;
import bd.com.squarehealth.authenticator.repositories.UserRepository;
import bd.com.squarehealth.corelibrary.common.ApiException;
import bd.com.squarehealth.corelibrary.common.RandomGenerator;
import bd.com.squarehealth.corelibrary.common.mail.Mailer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private Environment environment;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Mailer mailer;

    @Autowired
    private UserRepository userRepository;

    private static final String ACCOUNT_VERIFICATION_OTP_SUBJECT = "House Renting System Account Verification OTP";
    private static final String ACCOUNT_VERIFICATION_OTP_MESSAGE_TEMPLATE = "Your House Renting System account verification OTP is: {{otp}}";

    @Override
    public String generateOtp() {
        // generates a six digit OTP...
        String otp = "" + RandomGenerator.generateIntegerInRange(100000, 999998);

        log.info("Generated OTP: {}", otp);

        return otp;
    }

    @Override
    public User findUserByType(UserType userType) {
        Optional<User> optionalUser = userRepository.findByUserType(userType);

        return optionalUser.orElse(null);
    }

    @Override
    public User createAdminIfNotAvailable() throws Exception {
        String email = environment.getProperty("application.users.admin.email");
        String password = environment.getProperty("application.users.admin.password");
        Optional<User> optionalAdmin = userRepository.findByEmail(email);
        User admin;

        if (optionalAdmin.isPresent()) {
            admin = optionalAdmin.get();
        } else {
            admin = createAdmin(email, email, password);
        }

        log.info("Admin email: '" + email + "'");
        log.info("Admin password: '" + password + "'");

        return admin;
    }

    @Override
    public User createAdmin(String name, String email, String password) throws Exception {
        String hashedPassword = passwordEncoder.encode(password);

        User admin = new User();
        admin.setName(name);
        admin.setEmail(email);
        admin.setHashedPassword(hashedPassword);
        admin.setUserType(UserType.ADMIN);
        admin.setIsVerified(true);

        try {
            admin = userRepository.save(admin);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            dataIntegrityViolationException.printStackTrace();

            if (dataIntegrityViolationException.getMessage().contains("ConstraintViolationException")) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "A user account is already associated with the email '" + email + "'");
            }
        }

        return admin;
    }

    @Override
    public UserDto register(RegistrationDto registrationData) throws Exception {
        User user = new User();
        String otp = generateOtp();
        String hashedOtp = passwordEncoder.encode(otp);
        String hashedPassword = passwordEncoder.encode(registrationData.getPassword());

        user.setName(registrationData.getName());
        user.setIsVerified(false);
        user.setEmail(registrationData.getEmail());
        user.setHashedPassword(hashedPassword);
        user.setHashedOtp(hashedOtp);
        user.setLastOtpGeneratedAt(new Date(System.currentTimeMillis()));
        user.setUserType(UserType.REGULAR_USER);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            dataIntegrityViolationException.printStackTrace();

            if (dataIntegrityViolationException.getMessage().contains("ConstraintViolationException")) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "A user account is already associated with the email '" + registrationData.getEmail() + "'");
            }
        }

        // sends email to the user...
        // NOTE: mail sending task shall be added to a background task queue with retry mechanism.
        // but due to time constraint, mail is sent synchronously...
        mailer.sendTextMessage(user.getEmail(),
                ACCOUNT_VERIFICATION_OTP_SUBJECT,
                ACCOUNT_VERIFICATION_OTP_MESSAGE_TEMPLATE.replaceAll("\\{\\{otp\\}\\}", otp));

        return new UserDto(user);
    }

    @Override
    public UserDto verify(VerificationDto verificationData) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(verificationData.getEmail());

        if (!optionalUser.isPresent()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Specified user was not found.");
        }

        User user = optionalUser.get();

        // Note: due to time constraint, OTP expiration is not implemented...
        /*if (new Date(System.currentTimeMillis()).getTime() - user.getLastOtpGeneratedAt().getTime() > OTP_EXPIRATION_TIME_IN_MINUTES * 60 * 1000) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "OTP has expired.");
        }*/

        if (!passwordEncoder.matches(verificationData.getOtp(), user.getHashedOtp())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Incorrect OTP provided.");
        }

        user.setIsVerified(true);
        user.setLastOtpGeneratedAt(null);
        user.setHashedOtp(null);

        return new UserDto(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);

        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("Specified user was not found.");
        }

        User user = optionalUser.get();

        // if user is not verified, we'll throw exception...
        if (user.getUserType() != UserType.ADMIN && !user.getIsVerified()) {
            // generates a new OTP...
            String otp = generateOtp();
            String hashedOtp = passwordEncoder.encode(otp);
            user.setHashedOtp(hashedOtp);
            user.setLastOtpGeneratedAt(new Date(System.currentTimeMillis()));

            // saves the newly generated OTP to user...
            userRepository.save(user);

            // sends email to the user...
            // NOTE: mail sending task shall be added to a background task queue with retry mechanism.
            // but due to time constraint, mail is sent synchronously...
            mailer.sendTextMessage(user.getEmail(),
                    ACCOUNT_VERIFICATION_OTP_SUBJECT,
                    ACCOUNT_VERIFICATION_OTP_MESSAGE_TEMPLATE.replaceAll("\\{\\{otp\\}\\}", otp));

            throw new UsernameNotFoundException("Specified user account is not verified. An OTP is sent to you via email.");
        }

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getUserType().getValue());
        List<GrantedAuthority> grantedAuthorities = new LinkedList<>();
        grantedAuthorities.add(grantedAuthority);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getHashedPassword(), grantedAuthorities);
    }
}
