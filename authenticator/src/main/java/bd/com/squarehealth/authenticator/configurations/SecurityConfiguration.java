package bd.com.squarehealth.authenticator.configurations;

import bd.com.squarehealth.authenticator.enumerations.UserType;
import bd.com.squarehealth.authenticator.filters.AuthenticationFilter;
import bd.com.squarehealth.authenticator.filters.AuthorizationFilter;
import bd.com.squarehealth.authenticator.services.UsersService;
import bd.com.squarehealth.corelibrary.common.ApiException;
import bd.com.squarehealth.corelibrary.common.ApiResponse;
import bd.com.squarehealth.corelibrary.common.json.JsonSerializer;
import bd.com.squarehealth.corelibrary.common.security.JsonWebTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JsonSerializer jsonSerializer;

    @Autowired
    private JsonWebTokenService jsonWebTokenService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    protected AccessDeniedHandler accessDeniedHandler() {
        return (request, response, exception) -> {
            exception.printStackTrace();

            ApiException apiException = new ApiException(HttpStatus.UNAUTHORIZED, "You are not authorized to access the requested resource.");
            ApiResponse apiResponse = apiException.toApiResponse();

            String apiResponseAsJson = jsonSerializer.serialize(apiResponse);

            response.setContentType("application/json");
            response.getOutputStream().write(apiResponseAsJson.getBytes("UTF-8"));
        };
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(usersService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(
                jsonSerializer, jsonWebTokenService, authenticationManager());
        authenticationFilter.setFilterProcessesUrl("/v1.0/users/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter(jsonSerializer, jsonWebTokenService);

        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/v1.0/users/login").permitAll()
                .antMatchers(HttpMethod.POST, "/v1.0/users/register").permitAll()
                .antMatchers(HttpMethod.POST, "/v1.0/users/verify/**").permitAll()
                // users of admin type can access this path...
                .antMatchers(HttpMethod.POST, "/v1.0/users/admin").hasAuthority(UserType.ADMIN.getValue());
        httpSecurity.authorizeRequests().anyRequest().authenticated();
        httpSecurity.addFilter(authenticationFilter);
        httpSecurity.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
