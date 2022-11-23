package bd.com.squarehealth.authenticator.controllers;

import bd.com.squarehealth.authenticator.dtos.RegistrationDto;
import bd.com.squarehealth.authenticator.dtos.UserDto;
import bd.com.squarehealth.authenticator.dtos.VerificationDto;
import bd.com.squarehealth.authenticator.entities.User;
import bd.com.squarehealth.authenticator.services.UsersService;
import bd.com.squarehealth.corelibrary.common.ApiException;
import bd.com.squarehealth.corelibrary.common.ApiResponse;
import bd.com.squarehealth.corelibrary.common.security.JsonWebTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/v{version}/users")
public class UsersController {

    @Autowired
    private JsonWebTokenService jsonWebTokenService;

    @Autowired
    private UsersService usersService;

    @PostMapping(path = "register")
    public ApiResponse register(@RequestBody RegistrationDto registrationData) throws Exception {
        if (!registrationData.arePasswordsValid()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Password and confirm password must match.");
        }

        UserDto userData = usersService.register(registrationData);

        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "User registration successful.");
        apiResponse.setData("user", userData);

        return apiResponse;
    }

    @PostMapping(path = "verify")
    public ApiResponse verify(
            @RequestBody
            VerificationDto verificationData) throws Exception {
        usersService.verify(verificationData);

        return new ApiResponse(HttpStatus.OK, "Account verification successful. Please sign in.");
    }

    @PostMapping(path = "admin")
    public ApiResponse createAdmin(@RequestBody RegistrationDto registrationData) throws Exception {
        if (!registrationData.arePasswordsValid()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Password and confirm password must match.");
        }

        User admin = usersService.createAdmin(
                registrationData.getName(),
                registrationData.getEmail(),
                registrationData.getPassword());
        UserDto adminData = new UserDto(admin);

        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "User registration successful.");
        apiResponse.setData("user", adminData);

        return apiResponse;
    }
}
