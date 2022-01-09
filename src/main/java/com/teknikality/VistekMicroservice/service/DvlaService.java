package com.teknikality.VistekMicroservice.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.WebClient;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import com.teknikality.VistekMicroservice.entities.DvlaVehicle;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import reactor.core.publisher.Mono;

@Service
public class DvlaService {

	private JavaMailSender javaMailSender;

	@Autowired
	public DvlaService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public DvlaService() {
		
	}

	@Autowired
	private WebClient.Builder WebClientBuilder;


	public DvlaVehicle callDVLAmicroservice(String registrationNumber) {

//		Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		DvlaVehicle vehicle = WebClientBuilder.build().get().uri("http://DvlaMicroservices/dvla/update/" + registrationNumber)
//				.headers(header -> header.setBearerAuth(jwt.getTokenValue()))
				.retrieve()
				.bodyToMono(DvlaVehicle.class)
				.block();
		return vehicle;
	}

	public DvlaVehicle getFallbackDVLA(String registrationNumber) {

		return new DvlaVehicle("Vehicle is not found against Registration Number you entered", "Toyota", "Petrol", "Black",
				350, "Paid");

	}

	public void sendNotification(String email, String registrationNumber) throws MailException, MessagingException {

		DvlaVehicle vehicle = callDVLAmicroservice(registrationNumber);
		pdfcreation(vehicle.toString());
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setFrom("shamskhosa7@gmail.com");
		message.setSubject("Teknikality Send you requerd result");

		System.out.println("from sendNotification DVLAService.java");

		message.setText(vehicle.toString());

		javaMailSender.send(message);

	}

	public void pdfcreation(String fileContent) {
		try {

			String fileName = "E:\\PDF\\DVLA.pdf";
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(fileName));
			document.open();

			Paragraph paragraph = new Paragraph(fileContent);
			document.add(paragraph);

			document.close();

			System.out.println("File is generated");

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (DocumentException e) {

			e.printStackTrace();
		}
	}
	
	public String SendEmailWithAttachment(String email, String registrationNumber) {
		
		DvlaVehicle vehicle = callDVLAmicroservice(registrationNumber);
		System.out.println(vehicle.toString());
		pdfcreation(vehicle.toString());
		reportGeneration(registrationNumber);
		
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = 
					new MimeMessageHelper(message, true);
			
			messageHelper.setFrom("shamskhosa7@gmail.com");
			messageHelper.setTo(email);
			messageHelper.setSubject("Teknikality Send you requerd result");
			messageHelper.setText("This is text Body");
			
			File file = new File("E:\\PDF\\DVLA.pdf");
			File fil1 = new File("C:\\Users\\shams.ulhasan\\Desktop\\Reports\\dvla.html");
			System.out.println("File1 Object is Created");
			messageHelper.addAttachment(file.getName(), file);
			messageHelper.addAttachment(fil1.getName(), fil1);
			
			System.out.println("File Attachments is sucessfully file 1 and file 2");
			javaMailSender.send(message);
			System.out.println("javaMailSender.send(message); sucessfully");
			
			return "Mail Send Successfully";
			
		}catch(Exception e) {
			return "Mail Sending is failed";
		}
		
		
	}

	public List<DvlaVehicle> showAllVehicles() {

//		Jwt jwt = (Jwt) SecurityContextHolder
//				.getContext()
//				.getAuthentication()
//				.getPrincipal();
		Mono<List<DvlaVehicle>> response = (Mono<List<DvlaVehicle>>) WebClientBuilder.build().get()
				.uri("http://DvlaMicroservices/dvla/vehicles")
//				.headers(header -> header.setBearerAuth(jwt.getTokenValue()))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<DvlaVehicle>>() {
				});
		List<DvlaVehicle> readers = response.block();
		return readers.stream().collect(Collectors.toList());
	}
	
	
	
	
	
String Path = "C:\\Users\\shams.ulhasan\\Desktop\\Reports";

	
	public String expoertReport(String formate, String reg) throws JRException, IOException, InterruptedException {
		
		System.out.println("in export Repost methood ");
		
//		Vehicle vehicles =  callDVLAmicroservice(reg);
		List<DvlaVehicle>  vehicles = showAllVehicles();
		System.out.println("successfully called showAllVehicles(); ");
		// load file and compile it
		
		File file = ResourceUtils.getFile("classpath:vehicle.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		
		// now we get Vehicles and map to the report 
	
//		JRResultSetDataSource dataSource = new JRResultSetDataSource((ResultSet) vehicle);
//		JavaBean dataSource = new JavaBean(vehicle);
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(vehicles);
		
		// now using dataSource, we can fill the report and design the report
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("registrationNumber", reg);
//		parameters.put("CreatedBy", "Teknikality");
		
		
	
	JasperPrint jasperPrint =	JasperFillManager.fillReport(jasperReport, parameters, dataSource);
	
	if(formate.equalsIgnoreCase("html")){
		JasperExportManager.exportReportToHtmlFile(jasperPrint, Path+"\\employees.html");
		
		System.out.println("repost is created");
	}
	if(formate.equalsIgnoreCase("pdf")){
		JasperExportManager.exportReportToPdfFile(jasperPrint, Path+"\\employees.pdf");
	}
		return "report is generated in path : " +Path;
	}
	
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String reportGeneration(String reg) {
		

        String dvlaReport = "C:\\Users\\shams.ulhasan\\Desktop\\Reports";
		try {
            System.out.println("Loading the .JRMXML file ....");
 
            System.out.println("Compiling the .JRMXML file to .JASPER file....");

            
            File file = ResourceUtils.getFile("classpath:Blank_A4_Paramv2.jrxml");
    		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            
            DvlaVehicle vehicles =  callDVLAmicroservice(reg);
            String registrationNumber = vehicles.getRegistrationNumber();
            String make = vehicles.getMake();
            String fuelType = vehicles.getFuelType();
            String colour = vehicles.getColour();
            int engineCapacity = vehicles.getEngineCapacity();
            String texStatus = vehicles.getTaxStatus();

            Map<String, Object> hm = new HashMap<>();
            hm.put("Param_registrationNumber", registrationNumber);
            hm.put("Param_make", make);
            hm.put("Param_fuelType", fuelType);
            hm.put("Param_colour", colour);
            hm.put("Param_engineCapacity", engineCapacity);
            hm.put("Param_texStatus", texStatus);
            
            System.out.println("filling parameters to .JASPER file....");
            System.out.println("from Service class reportGeneration()  Vehicle registration Number is : "+vehicles.getRegistrationNumber());
            
            JasperPrint jasperPrint =	JasperFillManager.fillReport(jasperReport, hm, new JREmptyDataSource());
            
            System.out.println("exporting the JASPER file to PDF file....");
            JasperExportManager.exportReportToHtmlFile(jasperPrint, dvlaReport+"\\dvla.html");
            System.out.println("Successfully completed the export");
            
            return "File is generated at"+ dvlaReport.toString() ;

        } catch (Exception e) {
            System.out.print("Exception:" + e);
            
            return "Sorry to say we are not able to generate file";
        }
	

	}
	
	

}
