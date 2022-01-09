package com.teknikality.VistekMicroservice.entities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class DvlaVehicle implements Serializable {
	private static final long serialVersionUID = 1L;
	private String registrationNumber;
	private String make;
	private String fuelType;
	private String colour;
	private int engineCapacity;
	private String taxStatus;

}
