package bd.com.squarehealth.authenticator.configurations;

import bd.com.squarehealth.authenticator.services.UsersService;
import bd.com.squarehealth.corelibrary.common.cryptography.CryptographicService;
import bd.com.squarehealth.corelibrary.common.cryptography.CryptographicServiceImpl;
import bd.com.squarehealth.corelibrary.common.cryptography.HmacSha512PasswordEncoder;
import bd.com.squarehealth.corelibrary.common.json.JsonSerializer;
import bd.com.squarehealth.corelibrary.common.json.JsonSerializerImpl;
import bd.com.squarehealth.corelibrary.common.mail.Mailer;
import bd.com.squarehealth.corelibrary.common.mail.MailerImpl;
import bd.com.squarehealth.corelibrary.common.security.JsonWebTokenService;
import bd.com.squarehealth.corelibrary.common.security.JsonWebTokenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticatorConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public JsonSerializer jsonSerializer() {
        return JsonSerializerImpl.getInstance();
    }

    @Bean
    public CryptographicService cryptographicService() {
        return new CryptographicServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new HmacSha512PasswordEncoder();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JsonWebTokenService jsonWebTokenService() {
        return new JsonWebTokenServiceImpl();
    }

    @Bean
    public Mailer mailer() {
        // acquiring mailer configuration from properties file...
        String host = environment.getProperty("mailer.host");
        int port = Integer.parseInt(environment.getProperty("mailer.port"));
        String username = environment.getProperty("mailer.username");
        String password = environment.getProperty("mailer.password");

        // we could've used 'JavaMailSender' class directly. But, we might need to change
        // the underlying mailing engine in future...
        Mailer mailer = new MailerImpl(host, port, username, password);

        return mailer;
    }

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