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

import com.teknikality.VistekMicroservice.entities.DvlaVehicle;
import com.teknikality.VistekMicroservice.entities.SandBoxApi1Vehicle;
import com.teknikality.VistekMicroservice.service.SandBoxApi1Service;
import com.teknikality.VistekMicroservice.service.DvlaService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@CrossOrigin
@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/sandboxapi1")
public class SandBoxApi1Controller {
	
	private static final String Order_Vehicle_Retry_ms2 = "orderVehicleRetry_ms2";
	private static final String Order_Vehicle_Bulkhead_ms2 = "orderVehicleBulkhead_ms2";
	private static final String Order_Vehicle_CircuitBreaker_ms2 = "orderVehicleCircuitBreaker_ms2";
	
	private SandBoxApi1Service sandBoxApi1Service;

	@Autowired
	public SandBoxApi1Controller(SandBoxApi1Service sandBoxApi1Service) {
		this.sandBoxApi1Service = sandBoxApi1Service;
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * @GetMapping("/vistek") public String getString() {
	 * 
	 * return ("<h1>Welcome to Vistek<h1>"); }
	 */
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	@Retry(name = Order_Vehicle_Retry_ms2, fallbackMethod = "orderCarFallback")
//	@Bulkhead(name=Order_Vehicle_Bulkhead_ms2, fallbackMethod = "orderCarBulkheadFallback")
	@CircuitBreaker(name = Order_Vehicle_CircuitBreaker_ms2, fallbackMethod = "createOrderFallback")
	
	@GetMapping("/{registrationNumber}")
	public ResponseEntity<SandBoxApi1Vehicle> getData(@PathVariable("registrationNumber") String registrationNumber)
			throws IOException, InterruptedException {

		SandBoxApi1Vehicle vehicleApi1Vehicle = sandBoxApi1Service.callMicroserviceTwo(registrationNumber);
		return new ResponseEntity<>(vehicleApi1Vehicle, HttpStatus.OK);
	}
	
	
	public ResponseEntity<String> orderCarFallback(Exception e) {

		return new ResponseEntity<>("SandBox microservice api1 is down", HttpStatus.OK);

	}

	public ResponseEntity<String> orderCarBulkheadFallback(Exception t) {
		return new ResponseEntity<>(" SandBox microservice api1 is full of thread please patience ",
				HttpStatus.TOO_MANY_REQUESTS);

	}

	public ResponseEntity<String> createOrderFallback(Exception e) {
		return new ResponseEntity<>(" this message is from CircuitBreaker SandBox microservice api1 is down", HttpStatus.OK);

	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping("/showallvehicles")
	public ResponseEntity<SandBoxApi1Vehicle> getAll() throws IOException, InterruptedException {

		List<SandBoxApi1Vehicle> sandBoxApi1Vehicles = sandBoxApi1Service.showAllVehicles();
		return new ResponseEntity(sandBoxApi1Vehicles, HttpStatus.OK);
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping("/semdingEmailsanboxapi1/{email}/{registrationNumber}")
	public String semdingEmailWithAttaichment(@PathVariable("email") String email,
			@PathVariable("registrationNumber") String registrationNumber) throws MailException, MessagingException {

		try {
			sandBoxApi1Service.SendEmailWithAttachment(email, registrationNumber);
//			String location = dvlaInfo.expoertReport(format, registrationNumber);
			return "Your email is send to you ";

		} catch (Exception e) {
			System.out.println("Something went wrong. we could not Send you Email");
		}

		return "Something went wrong. we could not Send you Email ";
	}

}
