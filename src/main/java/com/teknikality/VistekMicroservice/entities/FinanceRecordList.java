package com.teknikality.VistekMicroservice.entities;

import java.io.Serializable;

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
public class FinanceRecordList implements Serializable {
	private static final long serialVersionUID = 1L;
	

	private String agreementType;
    private String agreementNumber;
    private String agreementTerm;
    private String agreementDate;
    private String financeCompany;
    private String vehicleDescription;
    private String contactNumber;

}

