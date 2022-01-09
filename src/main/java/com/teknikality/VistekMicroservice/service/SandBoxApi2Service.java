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

import com.teknikality.VistekMicroservice.entities.SandBoxApi2Vehicle;


import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import reactor.core.publisher.Mono;

@Service
public class SandBoxApi2Service {
	
	private JavaMailSender javaMailSender;

	@Autowired
	public SandBoxApi2Service(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	 public SandBoxApi2Service() {
		 
	 }
	
	@Autowired
	private WebClient.Builder WebClientBuilder;
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public SandBoxApi2Vehicle callMicroserviceThree(String registrationNumber) {

//		Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		SandBoxApi2Vehicle vehicleApiThree = WebClientBuilder.build().get().uri("http://sandboxapi2/sanboxapi2/update/" + registrationNumber)
//				.headers(header -> header.setBearerAuth(jwt.getTokenValue()))
				.retrieve()
				.bodyToMono(SandBoxApi2Vehicle.class)
				.block();
		return vehicleApiThree;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public List<SandBoxApi2Vehicle> showAllVehicles() {

//		Jwt jwt = (Jwt) SecurityContextHolder
//				.getContext()
//				.getAuthentication()
//				.getPrincipal();
		Mono<List<SandBoxApi2Vehicle>> response = (Mono<List<SandBoxApi2Vehicle>>)
				WebClientBuilder
				.build()
				.get()
				.uri("http://sandboxapi2/sanboxapi2/vehicles")
//				.headers(header -> header.setBearerAuth(jwt.getTokenValue()))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<SandBoxApi2Vehicle>>() {
				});
		List<SandBoxApi2Vehicle> readers = response.block();
		return readers.stream().collect(Collectors.toList());
	}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
public String sandBoxApi3Report(String reg) {
		
	    String filehtml = "C:\\Users\\shams.ulhasan\\Desktop\\Reports";
		try {
	        System.out.println("Loading the .JRMXML file ....");
//	           JasperDesign jasperDesign = JRXmlLoader.load("C:\\Users\\shams.ulhasan\\git\\repository3\\VistekMicroservice2\\src\\main\\resources\\Blank_A4_Paramv2.jrxml");
	        System.out.println("Compiling the .JRMXML file to .JASPER file....");
//	        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
	        
	        File file = ResourceUtils.getFile("classpath:SandBoxApi3.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
	        

//			MicroserviceTwoService microserviceTwoService = new MicroserviceTwoService();
			SandBoxApi2Vehicle vehicles = callMicroserviceThree(reg);
			

	   
	   
	         String registrationNumber = vehicles.getRegistrationNumber();
	    	 String importUsedBeforeUkRegistration = vehicles.getImportUsedBeforeUkRegistration(); 
	         String dateFirstRegistered = vehicles.getDateFirstRegistered();
	    	 String model= vehicles.getModel();
	    	 String previousColour= vehicles.getPreviousColour();
	    	 String stolenStatus= vehicles.getStolenStatus();
	    	 int previousKeeperCount= vehicles.getPreviousKeeperCount();
	    	 String importDate= vehicles.getImportDate();
	    	 String stolenPoliceForce= vehicles.getStolenPoliceForce();
	    	 String fuelType= vehicles.getFuelType();
	    	 String certificateOfDestructionIssued= vehicles.getCertificateOfDestructionIssued();
	    	 String latestColourChangeDate= vehicles.getLatestColourChangeDate();
	    	int colourChangeCount= vehicles.getColourChangeCount();
	    	 boolean importedFromOutsideEu= vehicles.isImportedFromOutsideEu();
	    	 String make= vehicles.getMake();
	    	 String dateFirstRegisteredUk= vehicles.getDateFirstRegisteredUk();
	    	 String stolenDate= vehicles.getStolenDate();
	    	 String latestKeeperChangeDate= vehicles.getLatestKeeperChangeDate();
	    	 String financeRecordCount= vehicles.getFinanceRecordCount();
	    	 int plateChangeCount= vehicles.getPlateChangeCount();
	    	 String scrapDate= vehicles.getScrapDate();
	    	 String exportDate= vehicles.getExportDate();
	    	 int gearCount= vehicles.getGearCount();
	    	 String transmissionType= vehicles.getTransmissionType();
	    	int engineCapacity= vehicles.getEngineCapacity();
	    	 String colour= vehicles.getColour();
	    

	        Map<String, Object> hm = new HashMap<>();
	        hm.put("Param_registration", registrationNumber);
	        hm.put("Param_importUsedBeforeUkRegistration", importUsedBeforeUkRegistration);
	        hm.put("Param_dateFirstRegistered",dateFirstRegistered);
	        hm.put("Param_model", model);
	        hm.put("Param_previousColour", previousColour);
	        hm.put("Param_stolenStatus", stolenStatus);
	        hm.put("Param_previousKeeperCount", previousKeeperCount);
	        hm.put("Param_importDate", importDate);
	        hm.put("Param_stolenPoliceForce", stolenPoliceForce);
	        hm.put("Param_fuelType", fuelType);
	        hm.put("Param_certificateOfDestructionIssued", certificateOfDestructionIssued);
	        hm.put("Param_latestColourChangeDate", latestColourChangeDate);
	        hm.put("Param_colourChangeCount", colourChangeCount);
	        hm.put("Param_importedFromOutsideEu", importedFromOutsideEu);
	        hm.put("Param_make", make);
	        hm.put("Param_dateFirstRegisteredUk", dateFirstRegisteredUk);
	        hm.put("Param_stolenDate", stolenDate);
	        hm.put("Param_latestKeeperChangeDate", latestKeeperChangeDate);
	        hm.put("Param_financeRecordCount", financeRecordCount);
	        hm.put("Param_plateChangeCount", plateChangeCount);
	        hm.put("Param_scrapDate", scrapDate);
	        hm.put("Param_exportDate", exportDate);
	        hm.put("Param_gearCount", gearCount);
	        hm.put("Param_transmissionType", transmissionType);
	        hm.put("Param_engineCapacity", engineCapacity);
	        hm.put("Param_colour", colour);
	      
	        
	        

	        
	        System.out.println("filling parameters to .JASPER file....");
	        System.out.println("from Service class reportGeneration()  Vehicle registration Number is : "+vehicles.getRegistrationNumber());
	        
	        JasperPrint jasperPrint =	JasperFillManager.fillReport(jasperReport, hm, new JREmptyDataSource());
	        
//	           JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperReport, hm, new JREmptyDataSource());
	        System.out.println("exporting the JASPER file to PDF file....");
//	          JasperExportManager.exportReportToPdfFile(jprint, fileNamePdf);
	        JasperExportManager.exportReportToHtmlFile(jasperPrint, filehtml+"\\SandBoxApi3.html");
	        System.out.println("Successfully completed the export");
	        
	        return "File is generated at"+ filehtml.toString() ;

	    } catch (Exception e) {
	        System.out.print("Exception:" + e);
	        
	        return "Sorry to say we are not able to generate file";
	    }

	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public String SendEmailWithAttachment(String email, String registrationNumber) {

		SandBoxApi2Vehicle vehicle = callMicroserviceThree(registrationNumber);
		System.out.println(vehicle.toString());
		sandBoxApi3Report(registrationNumber);

		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

			messageHelper.setFrom("shamskhosa7@gmail.com");
			messageHelper.setTo(email);
			messageHelper.setSubject("Teknikality Send you requerd result");
			messageHelper.setText("This is text Body");

			File fil1 = new File("C:\\Users\\shams.ulhasan\\Desktop\\Reports\\SandBoxApi3.html");
			System.out.println("File1 Object is Created");

			messageHelper.addAttachment(fil1.getName(), fil1);

			System.out.println("File Attachments is sucessfully file 1 ");
			javaMailSender.send(message);
			System.out.println("javaMailSender.send(message); sucessfully");

			return "Mail Send Successfully";

		} catch (Exception e) {
			return "Mail Sending is failed";
		}

	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
