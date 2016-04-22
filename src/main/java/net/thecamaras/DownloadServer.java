package net.thecamaras;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@SpringBootApplication
@EnableSwagger2
@Configuration
public class DownloadServer {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(DownloadServer.class, args);
    }

    @Bean
    public Docket downloadAPI(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo getApiInfo(){
        return new ApiInfoBuilder()
                .title("Download Server")
                .build();
    }

}