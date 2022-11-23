package bd.com.squarehealth.rentalprocessor.configurations;

import bd.com.squarehealth.corelibrary.configurations.WebSecurityConfiguration;
import bd.com.squarehealth.corelibrary.enumerations.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfiguration {

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected boolean shallAddAuthenticationFilter() { return false; }

    @Override
    protected boolean shallAddAuthorizationFilter() { return true; }

    @Override
    protected void configureHttpSecurity(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                // users with admin role can access this path...
                .antMatchers(HttpMethod.GET, "/v1.0/posts/pending").hasAuthority(UserRole.ADMIN.getValue())
                .antMatchers(HttpMethod.PATCH, "/v1.0/posts/*/status/*").hasAuthority(UserRole.ADMIN.getValue());
    }
}
