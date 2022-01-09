package com.teknikality.VistekMicroservice.entities;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SandBoxApi2Vehicle implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String registrationNumber;
	private String importUsedBeforeUkRegistration;
	private String dateFirstRegistered;
	private String model;
	private String previousColour;
	private String stolenStatus;
	private int previousKeeperCount;
	private String importDate;
	private String stolenPoliceForce;
	private String fuelType;
	private String certificateOfDestructionIssued;
	private String latestColourChangeDate;
	private int colourChangeCount;
	private boolean importedFromOutsideEu;
	private String make;
	private String dateFirstRegisteredUk;
	private String stolenDate;
	private String latestKeeperChangeDate;
	private String financeRecordCount;
	private int plateChangeCount;
	private String scrapDate;
	private String exportDate;
	private int gearCount;
	private String transmissionType;
	private int engineCapacity;
	private String colour;
	private List<FinanceRecordList> financeRecordList;
	private List<PlateChangeList> plateChangeList;
	private List<MileageRecordList> mileageRecordList;
}
