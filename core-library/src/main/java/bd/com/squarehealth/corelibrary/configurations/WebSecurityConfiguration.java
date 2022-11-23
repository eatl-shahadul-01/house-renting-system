package bd.com.squarehealth.corelibrary.configurations;

import bd.com.squarehealth.corelibrary.common.json.JsonSerializer;
import bd.com.squarehealth.corelibrary.common.security.JsonWebTokenService;
import bd.com.squarehealth.corelibrary.filters.AuthenticationFilter;
import bd.com.squarehealth.corelibrary.filters.AuthorizationFilter;
import bd.com.squarehealth.corelibrary.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JsonSerializer jsonSerializer;

    @Autowired
    private JsonWebTokenService jsonWebTokenService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(usersService)
                .passwordEncoder(passwordEncoder);
    }

    /**
     * This method shall be overriden in order to ignore certain servlet routes.
     * @return Returns an array of Strings containing servlet routes.
     * e.g. return new String[] {
     *     "POST::/v1.0/users/login",
     *     "POST::/v1.0/users/register",
     * }
     */
    protected String[] configureServletRoutesToIgnore() { return new String[0]; }

    protected void configureHttpSecurity(HttpSecurity httpSecurity) throws Exception { }

    protected boolean shallAddAuthenticationFilter() { return false; }

    protected boolean shallAddAuthorizationFilter() { return true; }

    protected String filterProcessesUrl() { return null; }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        configureHttpSecurity(httpSecurity);

        httpSecurity.authorizeRequests().anyRequest().authenticated();

        if (shallAddAuthenticationFilter()) {
            AuthenticationFilter authenticationFilter = new AuthenticationFilter(
                    jsonSerializer, jsonWebTokenService, authenticationManager());
            String filterProcessesUrl = filterProcessesUrl();

            if (filterProcessesUrl != null && filterProcessesUrl.length() != 0) {
                authenticationFilter.setFilterProcessesUrl(filterProcessesUrl);
            }

            httpSecurity.addFilter(authenticationFilter);
        }

        if (shallAddAuthorizationFilter()) {
            AuthorizationFilter authorizationFilter = new AuthorizationFilter(
                    configureServletRoutesToIgnore(), jsonSerializer, jsonWebTokenService);
            httpSecurity.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }
}
