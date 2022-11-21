package bd.com.squarehealth.rentalprocessor.configurations;

import bd.com.squarehealth.corelibrary.common.json.JsonSerializer;
import bd.com.squarehealth.corelibrary.common.json.JsonSerializerImpl;
import bd.com.squarehealth.corelibrary.common.mail.Mailer;
import bd.com.squarehealth.corelibrary.common.mail.MailerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class RentalProcessorConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public JsonSerializer jsonSerializer() {
        return JsonSerializerImpl.getInstance();
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
}
