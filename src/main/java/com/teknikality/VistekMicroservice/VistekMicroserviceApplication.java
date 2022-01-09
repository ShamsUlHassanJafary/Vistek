package com.teknikality.VistekMicroservice;




import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import org.springframework.web.reactive.function.client.WebClient;


import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
public class VistekMicroserviceApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(VistekMicroserviceApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public WebClient.Builder getWebClientBuilder(){
		
		return  WebClient.builder();
	}
	
//	@Bean
//	public Docket dvlaSwaggerConfigurationd() {
//		
//		return new Docket(DocumentationType.SWAGGER_2)
//				.select()
//				.paths(PathSelectors.ant("/dvla/**"))
//				.apis(RequestHandlerSelectors.basePackage("com.teknikality.VistekMicroservice.controller.**"))
//				.build()
//				.apiInfo(dvlaApiDetails());
//	}
//	
//
//	private ApiInfo dvlaApiDetails() {
//		
//		return new ApiInfo(
//				"Viskek Api (DVLA)",
//				"Viskek Api (DVLA) for retriving data from DVLA",
//				"1.0",
//				"Free to Use",
//				new springfox.documentation.service.Contact("Mr Mao", "http://www.carconnect.uk/", "mao@gmail.com"),
//				"API Licence",
//				"http://www.carconnect.uk/",
//				Collections.emptyList());
//	}
//	
//	
////	@Bean
////	public Docket api2SwaggerConfigurationd() {
////		
////		return new Docket(DocumentationType.SWAGGER_2)
////				.select()
////				.paths(PathSelectors.ant("/api2/**"))
////				.apis(RequestHandlerSelectors.basePackage("com.teknikality.VistekMicroservice.controller"))
////				.build()
////				.apiInfo(dvlaApiDetails());
////	}
////	
////
////	private ApiInfo api2ApiDetails() {
////		
////		return new ApiInfo(
////				"Viskek Api (DVLA)",
////				"Viskek Api (DVLA) for retriving data from DVLA",
////				"1.0",
////				"Free to Use",
////				new springfox.documentation.service.Contact("Mr Mao", "http://www.carconnect.uk/", "mao@gmail.com"),
////				"API Licence",
////				"http://www.carconnect.uk/",
////				Collections.emptyList());
////	}
////	
    
}
