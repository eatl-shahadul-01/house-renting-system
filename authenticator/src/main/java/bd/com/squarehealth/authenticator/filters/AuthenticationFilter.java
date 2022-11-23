package bd.com.squarehealth.authenticator.filters;

import bd.com.squarehealth.corelibrary.common.ApiException;
import bd.com.squarehealth.corelibrary.common.ApiResponse;
import bd.com.squarehealth.corelibrary.common.StreamUtilities;
import bd.com.squarehealth.corelibrary.common.json.JsonSerializer;
import bd.com.squarehealth.corelibrary.common.security.JsonWebTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JsonSerializer jsonSerializer;
    private final JsonWebTokenService jsonWebTokenService;

    public AuthenticationFilter(JsonSerializer jsonSerializer, JsonWebTokenService jsonWebTokenService, AuthenticationManager authenticationManager) {
        this.jsonSerializer = jsonSerializer;
        this.jsonWebTokenService = jsonWebTokenService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Map<String, Object> requestBody;

        try {
            String requestBodyJsonContent = StreamUtilities.readFromInputStream(request.getInputStream());
            requestBody = jsonSerializer.deserialize(requestBodyJsonContent, new HashMap<String, Object>(0).getClass());
        } catch (Exception exception) {
            exception.printStackTrace();

            Authentication authentication = new UsernamePasswordAuthenticationToken("", "");

            return authenticationManager.authenticate(authentication);
        }

        String email = (String) requestBody.get("email");
        String password = (String) requestBody.get("password");

        log.info("Email: " + email);
        log.info("Password: " + password);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);

        return authenticationManager.authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String userType = user.getAuthorities().iterator().next().getAuthority();
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", user.getUsername());

        // refresh token shall only contain the email...
        // Note: as access token never expires (for now), refresh token is not required.
        // but it is kept to be used in future...
        String refreshToken = jsonWebTokenService.generateAccessToken(userData);

        userData.put("userType", userType);

        // Note: due to time limitation, token renewal feature is not implemented...
        // so, access token never expires for now...
        String accessToken = jsonWebTokenService.generateAccessToken(userData);

        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Authentication successful.");
        apiResponse.setData("tokenType", jsonWebTokenService.getTokenType());
        apiResponse.setData("accessToken", accessToken);
        apiResponse.setData("refreshToken", refreshToken);

        String apiResponseAsJson = jsonSerializer.serialize(apiResponse);

        response.setContentType("application/json");
        response.getOutputStream().write(apiResponseAsJson.getBytes("UTF-8"));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        exception.printStackTrace();

        ApiException apiException = new ApiException(HttpStatus.FORBIDDEN, "Authentication failed.");
        ApiResponse apiResponse = apiException.toApiResponse();

        String apiResponseAsJson = jsonSerializer.serialize(apiResponse);

        response.setContentType("application/json");
        response.getOutputStream().write(apiResponseAsJson.getBytes("UTF-8"));
    }
}
