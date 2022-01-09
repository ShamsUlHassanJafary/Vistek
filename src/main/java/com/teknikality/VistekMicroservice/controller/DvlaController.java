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

import com.teknikality.VistekMicroservice.service.DvlaService;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import net.sf.jasperreports.engine.JRException;

@CrossOrigin
//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/dvla")
public class DvlaController {

	private static final String Order_Vehicle_Retry = "orderVehicleRetry";
	private static final String Order_Vehicle_Bulkhead = "orderVehicleBulkhead";
	private static final String Order_Vehicle_CircuitBreaker = "orderVehicleCircuitBreaker";

	DvlaService dvlaService;

	@Autowired
	public DvlaController(DvlaService dvlaService) {
		this.dvlaService = dvlaService;
	}

	/*
	 * @GetMapping("/") public String getString() {
	 * 
	 * return ("<h1>Welcome to Vistek<h1>"); }
	 */

	@Retry(name = Order_Vehicle_Retry, fallbackMethod = "orderCarFallback")
//	@Bulkhead(name=Order_Vehicle_Bulkhead, fallbackMethod = "orderCarBulkheadFallback")
	@CircuitBreaker(name = Order_Vehicle_CircuitBreaker, fallbackMethod = "createOrderFallback")

	@GetMapping("/{registrationNumber}")
	public ResponseEntity<DvlaVehicle> getData(@PathVariable("registrationNumber") String registrationNumber)
			throws IOException, InterruptedException {

		DvlaVehicle vehicle = dvlaService.callDVLAmicroservice(registrationNumber);
		return new ResponseEntity<>(vehicle, HttpStatus.OK);
	}

	public ResponseEntity<String> orderCarFallback(Exception e) {

		return new ResponseEntity<>("Dvla service is down", HttpStatus.OK);

	}

	public ResponseEntity<String> orderCarBulkheadFallback(Exception t) {
		return new ResponseEntity<>(" Dvla service is full of thread please patience ",
				HttpStatus.TOO_MANY_REQUESTS);

	}

	public ResponseEntity<String> createOrderFallback(Exception e) {
		return new ResponseEntity<>(" this message is from CircuitBreaker Dvla Service is down", HttpStatus.OK);

	}

	@GetMapping("/showallvehicles")
	public ResponseEntity<DvlaVehicle> getAll() throws IOException, InterruptedException {

		List<DvlaVehicle> vehicle = dvlaService.showAllVehicles();
		return new ResponseEntity(vehicle, HttpStatus.OK);
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * @GetMapping("/sendingemail/{email}/{registrationNumber}") public String
	 * semdingEmail(@PathVariable("email") String email,
	 * 
	 * @PathVariable("registrationNumber") String registrationNumber) throws
	 * MailException, MessagingException {
	 * 
	 * try { dvlaService.sendNotification(email, registrationNumber); return
	 * "Your email is send to you ";
	 * 
	 * } catch (Exception e) {
	 * System.out.println("Something went wrong. we could not Send you Email"); }
	 * 
	 * return "Something went wrong. we could not Send you Email "; }
	 */
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping("/semdingEmailWithAttaichment/{email}/{registrationNumber}")
	public String semdingEmailWithAttaichment(@PathVariable("email") String email,
			@PathVariable("registrationNumber") String registrationNumber) throws MailException, MessagingException {

		try {
			dvlaService.SendEmailWithAttachment(email, registrationNumber);
//			String location = dvlaInfo.expoertReport(format, registrationNumber);
			return "Your email is send to you ";

		} catch (Exception e) {
			System.out.println("Something went wrong. we could not Send you Email");
		}

		return "Something went wrong. we could not Send you Email ";
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * @GetMapping("/report/{format}") public ResponseEntity<String>
	 * getVehicleReport(@PathVariable("format") String format
	 * , @PathVariable("registrationNumber") String registrationNumber) throws
	 * IOException, InterruptedException, JRException{ String location =
	 * dvlaService.expoertReport(format , registrationNumber); return new
	 * ResponseEntity<String>(location, HttpStatus.OK);
	 * 
	 * }
	 */
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * @GetMapping("/singlereport/{registration}") public ResponseEntity<String>
	 * getSingleVehicleReport(@PathVariable("registration") String registration)
	 * throws IOException, InterruptedException, JRException{ String location =
	 * dvlaService.reportGeneration(registration); return new
	 * ResponseEntity<>(location, HttpStatus.OK);
	 * 
	 * }
	 */

}
