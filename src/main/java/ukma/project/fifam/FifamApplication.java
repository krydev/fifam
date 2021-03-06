package ukma.project.fifam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class FifamApplication {

	public static void main(String[] args) {
		SpringApplication.run(FifamApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8080", "https://fifam-money.herokuapp.com")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                        .allowCredentials(true);
			}
		};
	}
}
