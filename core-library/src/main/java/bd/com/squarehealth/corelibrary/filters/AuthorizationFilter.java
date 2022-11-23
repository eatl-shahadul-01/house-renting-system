package bd.com.squarehealth.corelibrary.filters;

import bd.com.squarehealth.corelibrary.common.ApiException;
import bd.com.squarehealth.corelibrary.common.ApiResponse;
import bd.com.squarehealth.corelibrary.common.json.JsonSerializer;
import bd.com.squarehealth.corelibrary.common.security.AuthenticatedUserData;
import bd.com.squarehealth.corelibrary.common.security.JsonWebTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final String[] servletRouteToIgnore;
    private final JsonSerializer jsonSerializer;
    private final JsonWebTokenService jsonWebTokenService;

    private boolean shallIgnoreServletRoute(String method, String servletPath) {
        method = method.toUpperCase();

        String servletRoute = method + "::" + servletPath;

        // this should have been more robust. but, it will get the job done...
        for (String servletRouteToIgnore : servletRouteToIgnore) {
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

            Long userId = ((Number) payload.get("userId")).longValue();
            String name = (String) payload.get("name");
            String email = (String) payload.get("email");
            String role = (String) payload.get("role");

            Authentication authenticatedUserData = new AuthenticatedUserData(userId, name, email, role);
            SecurityContextHolder.getContext().setAuthentication(authenticatedUserData);

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
