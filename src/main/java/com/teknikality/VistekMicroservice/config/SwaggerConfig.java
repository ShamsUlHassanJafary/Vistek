package com.teknikality.VistekMicroservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.teknikality.VistekMicroservice.controller.DvlaController;
import com.teknikality.VistekMicroservice.controller.SandBoxApi1Controller;
import com.teknikality.VistekMicroservice.controller.SandBoxApi2Controller;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@ComponentScan(basePackageClasses = { 
		DvlaController.class,SandBoxApi1Controller.class, SandBoxApi2Controller.class})
@Configuration

public class SwaggerConfig {

    private static final String SWAGGER_API_VERSION = "1.0";
    private static final String LICENSE_TEXT = "License";
    private static final String Title = "Vehicle Information System  REST API";
    private static final String Description = "docs for test Framework";

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(Title)
                .description(Description)
                .license(LICENSE_TEXT)
                .version(SWAGGER_API_VERSION)
                .build();
    }

    @Bean
    public Docket frameworkApi() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.select()
				.paths(PathSelectors.ant("/api/**"))
				.apis(RequestHandlerSelectors.basePackage("com.teknikality.VistekMicroservice.controller"))
				.build()
				.apiInfo(apiInfo());
        
    }
 }
