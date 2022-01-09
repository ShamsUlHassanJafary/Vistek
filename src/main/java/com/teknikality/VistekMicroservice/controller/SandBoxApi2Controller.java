package com.teknikality.VistekMicroservice.controller;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teknikality.VistekMicroservice.entities.SandBoxApi2Vehicle;
import com.teknikality.VistekMicroservice.entities.SandBoxApi1Vehicle;
import com.teknikality.VistekMicroservice.service.SandBoxApi2Service;
import com.teknikality.VistekMicroservice.service.SandBoxApi1Service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@CrossOrigin
@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/sandboxapi2")
public class SandBoxApi2Controller {
	private static final String Order_Vehicle_Retry_ms3 = "orderVehicleRetry_ms3";
	private static final String Order_Vehicle_Bulkhead_ms3 = "orderVehicleBulkhead_ms3";
	private static final String Order_Vehicle_CircuitBreaker_ms3 = "orderVehicleCircuitBreaker_ms3";
	
	private SandBoxApi2Service sandBoxApi2Service;
	
	@Autowired
	public SandBoxApi2Controller(SandBoxApi2Service sandBoxApi2Service) {
		this.sandBoxApi2Service = sandBoxApi2Service;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * @GetMapping("/api3vistek") public String getString() {
	 * 
	 * return ("<h1>Welcome to Vistek<h1>"); }
	 */
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Retry(name = Order_Vehicle_Retry_ms3, fallbackMethod = "orderCarFallback")
//	@Bulkhead(name=Order_Vehicle_Bulkhead_ms3, fallbackMethod = "orderCarBulkheadFallback")
	@CircuitBreaker(name = Order_Vehicle_CircuitBreaker_ms3, fallbackMethod = "createOrderFallback")
	
	@GetMapping("/{registrationNumber}")
	public ResponseEntity<SandBoxApi1Vehicle> getData(@PathVariable("registrationNumber") String registrationNumber)
			throws IOException, InterruptedException {

		SandBoxApi2Vehicle sandBoxApi2Vehicle = sandBoxApi2Service.callMicroserviceThree(registrationNumber);
		return new ResponseEntity(sandBoxApi2Vehicle, HttpStatus.OK);
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public ResponseEntity<String> orderCarFallback(Exception e) {

		return new ResponseEntity<>("SandBox microservice api2 is down", HttpStatus.OK);

	}

	public ResponseEntity<String> orderCarBulkheadFallback(Exception t) {
		return new ResponseEntity<>(" SandBox microservice api2 is full of thread please patience ",
				HttpStatus.TOO_MANY_REQUESTS);

	}

	public ResponseEntity<String> createOrderFallback(Exception e) {
		
		return new ResponseEntity<>(" this message is from CircuitBreaker SandBox microservice api2 is down", HttpStatus.OK);

	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	@GetMapping("/showallvehicles")
	public ResponseEntity<SandBoxApi2Vehicle> getAll() throws IOException, InterruptedException {

		List<SandBoxApi2Vehicle> sandBoxApi2Vehicles = sandBoxApi2Service.showAllVehicles();
		return new ResponseEntity(sandBoxApi2Vehicles, HttpStatus.OK);
	}

	
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping("/semdingEmailsanboxapi2/{email}/{registrationNumber}")
	public String semdingEmailWithAttaichment(@PathVariable("email") String email,
			@PathVariable("registrationNumber") String registrationNumber) throws MailException, MessagingException {

		try {
			sandBoxApi2Service.SendEmailWithAttachment(email, registrationNumber);
            //String location = dvlaInfo.expoertReport(format, registrationNumber);
			return "Your email is send to you ";

		} catch (Exception e) {
			System.out.println("Something went wrong. we could not Send you Email");
		}

		return "Something went wrong. we could not Send you Email ";
	}

}
