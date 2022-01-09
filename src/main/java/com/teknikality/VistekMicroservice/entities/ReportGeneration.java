package com.teknikality.VistekMicroservice.entities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ResourceUtils;

import com.teknikality.VistekMicroservice.service.DvlaService;
import com.teknikality.VistekMicroservice.service.SandBoxApi1Service;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class ReportGeneration {

public String dvlaReport(String reg) {
		
        String filehtml = "C:\\Users\\shams.ulhasan\\Desktop\\Reports";
		try {
            System.out.println("Loading the .JRMXML file ....");
 //           JasperDesign jasperDesign = JRXmlLoader.load("C:\\Users\\shams.ulhasan\\git\\repository3\\VistekMicroservice2\\src\\main\\resources\\Blank_A4_Paramv2.jrxml");
            System.out.println("Compiling the .JRMXML file to .JASPER file....");
//            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            File file = ResourceUtils.getFile("classpath:Blank_A4_Paramv2.jrxml");
    		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            
    		DvlaService dvlaInfo = new DvlaService();
    		
            DvlaVehicle vehicles = dvlaInfo.callDVLAmicroservice(reg);
            String registrationNumber = vehicles.getRegistrationNumber();
            String make = vehicles.getMake();
            String fuelType = vehicles.getFuelType();
            String colour = vehicles.getColour();
            int engineCapacity = vehicles.getEngineCapacity();
            String texStatus = vehicles.getTaxStatus();
            
//            String first_language = "Java";
//            String second_language = "Structured text";
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
            
 //           JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperReport, hm, new JREmptyDataSource());
            System.out.println("exporting the JASPER file to PDF file....");
  //          JasperExportManager.exportReportToPdfFile(jprint, fileNamePdf);
            JasperExportManager.exportReportToHtmlFile(jasperPrint, filehtml+"\\DvlaVehicle.html");
            System.out.println("Successfully completed the export");
            
            return "File is generated at"+ filehtml.toString() ;

        } catch (Exception e) {
            System.out.print("Exception:" + e);
            
            return "Sorry to say we are not able to generate file";
        }

	}




public String sandBoxApi2Report(String reg) {
	
    String filehtml = "C:\\Users\\shams.ulhasan\\Desktop\\Reports";
	try {
        System.out.println("Loading the .JRMXML file ....");
//           JasperDesign jasperDesign = JRXmlLoader.load("C:\\Users\\shams.ulhasan\\git\\repository3\\VistekMicroservice2\\src\\main\\resources\\Blank_A4_Paramv2.jrxml");
        System.out.println("Compiling the .JRMXML file to .JASPER file....");
//        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        
        File file = ResourceUtils.getFile("classpath:SandBoxApi2.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        

		SandBoxApi1Service microserviceTwoService = new SandBoxApi1Service();
		SandBoxApi1Vehicle vehicles = microserviceTwoService.callMicroserviceTwo(reg);
		

        String registrationNumber = vehicles.getRegistrationNumber();
        String mileage = vehicles.getMileage();
        String plateYear = vehicles.getPlateYear();
        String valuationTime = vehicles.getValuationTime();
        String vehicleDescription = vehicles.getVehicleDescription();
    
        
//        String first_language = "Java";
//        String second_language = "Structured text";
        Map<String, Object> hm = new HashMap<>();
        hm.put("Param_registration", registrationNumber);
        hm.put("Param_mileage", mileage);
        hm.put("Param_plateYear", plateYear);
        hm.put("Param_valuationTime", valuationTime);
        hm.put("Param_description", vehicleDescription);

        
        System.out.println("filling parameters to .JASPER file....");
        System.out.println("from Service class reportGeneration()  Vehicle registration Number is : "+vehicles.getRegistrationNumber());
        
        JasperPrint jasperPrint =	JasperFillManager.fillReport(jasperReport, hm, new JREmptyDataSource());
        
//           JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperReport, hm, new JREmptyDataSource());
        System.out.println("exporting the JASPER file to PDF file....");
//          JasperExportManager.exportReportToPdfFile(jprint, fileNamePdf);
        JasperExportManager.exportReportToHtmlFile(jasperPrint, filehtml+"\\SandBoxApi2.html");
        System.out.println("Successfully completed the export");
        
        return "File is generated at"+ filehtml.toString() ;

    } catch (Exception e) {
        System.out.print("Exception:" + e);
        
        return "Sorry to say we are not able to generate file";
    }

}
	
}
