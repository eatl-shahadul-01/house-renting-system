package bd.com.squarehealth.rentalsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("bd.com.squarehealth.*")
@EntityScan("bd.com.squarehealth.*")
@ComponentScan(basePackages = { "bd.com.squarehealth.*" })
@EnableAutoConfiguration
public class RentalSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentalSearchApplication.class, args);
	}

}
