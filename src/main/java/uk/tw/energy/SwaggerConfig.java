package uk.tw.energy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/3.1.0/")
                .resourceChain(false);
    }

    // @Bean
    // public OpenAPI customOpenAPI() {
    //     return new OpenAPI()
    //             .info(new Info().title("Your API").version("1.0"))
    //             // Additional customization if needed
    //             ;
    // }
}
