package by.prus.LabProject;

import by.prus.LabProject.security.AppProperties;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootApplication
public class LabProjectApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(LabProjectApplication.class, args);
	}


	@Bean
	public SpringApplicationContext springApplicationContext(){
		return new SpringApplicationContext();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder () {return new BCryptPasswordEncoder();}

	@Bean(name = "AppProperties")
	public AppProperties appProperties () {return new AppProperties();}

	@Bean
	public ModelMapper modelMapper() { return new ModelMapper(); }


}
