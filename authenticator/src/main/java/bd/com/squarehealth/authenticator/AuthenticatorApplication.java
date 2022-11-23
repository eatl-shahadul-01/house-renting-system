package bd.com.squarehealth.authenticator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthenticatorApplication {

	public static void main(String[] args) {
		// Note: all response messages should've been centralized but couldn't be done due to time limitation...
		SpringApplication.run(AuthenticatorApplication.class, args);
	}
}
