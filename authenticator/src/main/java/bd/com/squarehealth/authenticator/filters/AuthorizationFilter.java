package bd.com.squarehealth.authenticator.filters;

import bd.com.squarehealth.corelibrary.common.ApiException;
import bd.com.squarehealth.corelibrary.common.ApiResponse;
import bd.com.squarehealth.corelibrary.common.json.JsonSerializer;
import bd.com.squarehealth.corelibrary.common.security.JsonWebTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JsonSerializer jsonSerializer;
    private final JsonWebTokenService jsonWebTokenService;

    private static final String[] SERVLET_ROUTES_TO_IGNORE = new String[] {
            "POST::/v1.0/users/login",
            "POST::/v1.0/users/register",
            "POST::/v1.0/users/verify"
    };

    public AuthorizationFilter(JsonSerializer jsonSerializer, JsonWebTokenService jsonWebTokenService) {
        this.jsonSerializer = jsonSerializer;
        this.jsonWebTokenService = jsonWebTokenService;
    }

    private boolean shallIgnoreServletRoute(String method, String servletPath) {
        method = method.toUpperCase();

        String servletRoute = method + "::" + servletPath;

        // this should have been more robust. but, it will get the job done...
        for (String servletRouteToIgnore : SERVLET_ROUTES_TO_IGNORE) {
            if (servletRoute.startsWith(servletRouteToIgnore)) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // checks if current servlet route shall be ignored...
        if (shallIgnoreServletRoute(request.getMethod(), request.getServletPath())) {
            // calls next filter in chain...
            filterChain.doFilter(request, response);

            return;
        }

        try {
            String accessToken = jsonWebTokenService.extractTokenFromAuthorizationHeader(request.getHeader(HttpHeaders.AUTHORIZATION));

            if (accessToken == null) { throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid authentication token provided."); }

            Map<String, Object> payload;

            try {
                payload = jsonWebTokenService.validateAccessToken(accessToken,
                        new HashMap<String, Object>(0).getClass());
            } catch (Exception exception) {
                exception.printStackTrace();

                throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid authentication token provided.");
            }

            String email = (String) payload.get("email");
            String userType = (String) payload.get("userType");

            List<GrantedAuthority> grantedAuthorities = new LinkedList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority(userType));
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    email, null, grantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // calls next filter in chain...
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            ApiException apiException = exception instanceof ApiException
                    ? (ApiException) exception : new ApiException(exception);
            ApiResponse apiResponse = apiException.toApiResponse();
            String apiResponseAsJson = jsonSerializer.serialize(apiResponse);

            response.setContentType("application/json");
            response.getOutputStream().write(apiResponseAsJson.getBytes("UTF-8"));
        }
    }
}
