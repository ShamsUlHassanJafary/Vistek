package com.teknikality.VistekMicroservice.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.teknikality.VistekMicroservice.entities.SandBoxApi1Vehicle;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import reactor.core.publisher.Mono;

@Service
public class SandBoxApi1Service {

	private JavaMailSender javaMailSender;

	@Autowired
	public SandBoxApi1Service(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public SandBoxApi1Service() {
		
	}

	@Autowired
	private WebClient.Builder WebClientBuilder;
	
	public SandBoxApi1Vehicle callMicroserviceTwo(String registrationNumber) {

//		Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		SandBoxApi1Vehicle vehicle = WebClientBuilder.build().get().uri("http://sandBoxapi1/sandboxapi1/update/" + registrationNumber)
//				.headers(header -> header.setBearerAuth(jwt.getTokenValue()))
				.retrieve()
				.bodyToMono(SandBoxApi1Vehicle.class)
				.block();
		return vehicle;
	}

	public SandBoxApi1Vehicle getFallbackDVLA(String registrationNumber) {

		return new SandBoxApi1Vehicle("Vehicle is not found against Registration Number you entered", "0 mileage", "no data of plateYear", "valuationTime",  "No Description");

	}
	
	public List<SandBoxApi1Vehicle> showAllVehicles() {

//		Jwt jwt = (Jwt) SecurityContextHolder
//				.getContext()
//				.getAuthentication()
//				.getPrincipal();
		Mono<List<SandBoxApi1Vehicle>> response = (Mono<List<SandBoxApi1Vehicle>>) WebClientBuilder.build().get()
				.uri("http://sandBoxapi1/sandboxapi1/vehicles")
//				.headers(header -> header.setBearerAuth(jwt.getTokenValue()))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<SandBoxApi1Vehicle>>() {
				});
		List<SandBoxApi1Vehicle> readers = response.block();
		return readers.stream().collect(Collectors.toList());
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
public String SendEmailWithAttachment(String email, String registrationNumber) {
		
		SandBoxApi1Vehicle vehicle = callMicroserviceTwo(registrationNumber);
		System.out.println(vehicle.toString());
		sandBoxApi2Report(registrationNumber);
		
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = 
					new MimeMessageHelper(message, true);
			
			messageHelper.setFrom("shamskhosa7@gmail.com");
			messageHelper.setTo(email);
			messageHelper.setSubject("Teknikality Send you requerd result");
			messageHelper.setText("This is text Body");
			

			File fil1 = new File("C:\\Users\\shams.ulhasan\\Desktop\\Reports\\SandBoxApi2.html");
			System.out.println("File1 Object is Created");

			messageHelper.addAttachment(fil1.getName(), fil1);
			
			System.out.println("File Attachments is sucessfully file 1 ");
			javaMailSender.send(message);
			System.out.println("javaMailSender.send(message); sucessfully");
			
			return "Mail Send Successfully";
			
		}catch(Exception e) {
			return "Mail Sending is failed";
		}
		
		
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String sandBoxApi2Report(String reg) {
		
	    String filehtml = "C:\\Users\\shams.ulhasan\\Desktop\\Reports";
		try {
	        System.out.println("Loading the .JRMXML file ....");
//	           JasperDesign jasperDesign = JRXmlLoader.load("C:\\Users\\shams.ulhasan\\git\\repository3\\VistekMicroservice2\\src\\main\\resources\\Blank_A4_Paramv2.jrxml");
	        System.out.println("Compiling the .JRMXML file to .JASPER file....");
//	        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
	        
	        File file = ResourceUtils.getFile("classpath:SandBoxApi2.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
	        

//			MicroserviceTwoService microserviceTwoService = new MicroserviceTwoService();
			SandBoxApi1Vehicle vehicles = callMicroserviceTwo(reg);
			

	        String registrationNumber = vehicles.getRegistrationNumber();
	        String mileage = vehicles.getMileage();
	        String plateYear = vehicles.getPlateYear();
	        String valuationTime = vehicles.getValuationTime();
	        String vehicleDescription = vehicles.getVehicleDescription();
	    
	        
//	        String first_language = "Java";
//	        String second_language = "Structured text";
	        Map<String, Object> hm = new HashMap<>();
	        hm.put("Param_registration", registrationNumber);
	        hm.put("Param_mileage", mileage);
	        hm.put("Param_plateYear", plateYear);
	        hm.put("Param_valuationTime", valuationTime);
	        hm.put("Param_description", vehicleDescription);

	        
	        System.out.println("filling parameters to .JASPER file....");
	        System.out.println("from Service class reportGeneration()  Vehicle registration Number is : "+vehicles.getRegistrationNumber());
	        
	        JasperPrint jasperPrint =	JasperFillManager.fillReport(jasperReport, hm, new JREmptyDataSource());
	        
//	           JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperReport, hm, new JREmptyDataSource());
	        System.out.println("exporting the JASPER file to PDF file....");
//	          JasperExportManager.exportReportToPdfFile(jprint, fileNamePdf);
	        JasperExportManager.exportReportToHtmlFile(jasperPrint, filehtml+"\\SandBoxApi2.html");
	        System.out.println("Successfully completed the export");
	        
	        return "File is generated at"+ filehtml.toString() ;

	    } catch (Exception e) {
	        System.out.print("Exception:" + e);
	        
	        return "Sorry to say we are not able to generate file";
	    }

	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
