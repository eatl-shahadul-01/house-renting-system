package bd.com.squarehealth.authenticator.configurations;

import bd.com.squarehealth.corelibrary.services.UsersService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticatorConfiguration {

    @Bean
    public CommandLineRunner commandLineRunner(UsersService usersService) {
        return args -> {
            try {
                usersService.createAdminIfNotAvailable();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        };
    }
}
