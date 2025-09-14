package com.example.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author:Niu
 * Date:2025/1/24 11:06
 * Description: nothing.
 */
@Configuration
@AutoConfigureBefore(SpringDocConfiguration.class)
public class SpringDocConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info();
        info.setTitle("接口文档");
        info.setDescription("springboot3开发接口文档");
        info.setVersion("1.0.0");
        info.setContact(new Contact().name("Niu").email("728097735@qq.com").url("https://github.com/AlanNiew"));
        info.setLicense(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html"));
        return new OpenAPI().info(info).externalDocs(new ExternalDocumentation()
                .description("SpringDoc Full Documentation")
                .url("https://springdoc.org/"));
    }

}
