package bd.com.squarehealth.rentalsearch.configurations;

import bd.com.squarehealth.corelibrary.configurations.WebSecurityConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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
}
