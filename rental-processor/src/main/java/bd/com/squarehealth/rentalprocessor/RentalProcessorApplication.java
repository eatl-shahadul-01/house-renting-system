package bd.com.squarehealth.rentalprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration()
public class RentalProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentalProcessorApplication.class, args);
	}

}
