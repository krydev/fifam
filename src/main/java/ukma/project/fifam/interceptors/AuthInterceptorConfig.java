package ukma.project.fifam.interceptors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class AuthInterceptorConfig extends WebMvcConfigurerAdapter {
    @Bean
    public AuthInterceptor a() {
        return new AuthInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(a()).addPathPatterns("/user").addPathPatterns("/users").addPathPatterns("/users/");
    }
}
