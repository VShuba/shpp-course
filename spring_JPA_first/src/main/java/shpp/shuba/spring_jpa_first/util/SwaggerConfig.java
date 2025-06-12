package shpp.shuba.spring_jpa_first.util;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${app.version}")
    private String appV;

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(new Info()
               .title("Employee API")
                .version(appV) // <- не розібрався як тянуть з pom
                .description("API Documentation to manage Employees"));
    }

}
