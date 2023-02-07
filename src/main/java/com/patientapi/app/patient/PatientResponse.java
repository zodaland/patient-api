package com.patientapi.app.patient;

import com.patientapi.app.global.response.Response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PatientResponse extends Response {
	private final String name;
	private final short age;
	private final short gender;
	private final boolean hasDisease;
	private final String imageUrl;
	
	@Builder
	public PatientResponse(String code, String name, short age, short gender, boolean hasDisease, String imageUrl) {
		super(code);
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.hasDisease = hasDisease;
		this.imageUrl = imageUrl;
	}
}
