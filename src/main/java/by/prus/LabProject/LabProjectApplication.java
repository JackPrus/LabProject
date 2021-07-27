package by.prus.LabProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LabProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(LabProjectApplication.class, args);
	}


	@Bean
	public SpringApplicationContext springApplicationContext(){
		return new SpringApplicationContext();
	}

}
