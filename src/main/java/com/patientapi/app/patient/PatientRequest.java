package com.patientapi.app.patient;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PatientRequest {
	@NotBlank
	@Pattern(regexp="[a-zA-Z가-힣]{1,20}")
	private String name;
	
	@Min(1)
	@Max(999)
	private short age;
	
	@Min(0)
	@Max(1)
	private short gender;
	
	@NotNull
	private Boolean hasDisease;
}
