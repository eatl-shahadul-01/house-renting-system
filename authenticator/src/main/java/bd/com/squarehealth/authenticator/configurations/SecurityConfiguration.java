package bd.com.squarehealth.authenticator.configurations;

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
    protected boolean shallAddAuthenticationFilter() { return true; }

    @Override
    protected boolean shallAddAuthorizationFilter() { return true; }

    @Override
    protected String[] configureServletRoutesToIgnore() {
        return new String[] {
                "POST::/v1.0/users/login",
                "POST::/v1.0/users/register",
                "POST::/v1.0/users/verify",
        };
    }

    @Override
    protected String filterProcessesUrl() {
        return "/v1.0/users/login";
    }

    @Override
    protected void configureHttpSecurity(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/v1.0/users/login").permitAll()
                .antMatchers(HttpMethod.POST, "/v1.0/users/register").permitAll()
                .antMatchers(HttpMethod.POST, "/v1.0/users/verify/**").permitAll()
                // users of admin type can access this path...
                .antMatchers(HttpMethod.POST, "/v1.0/users/admin").hasAuthority(UserRole.ADMIN.getValue());
    }
}
