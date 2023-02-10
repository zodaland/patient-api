package com.patientapi.app.patient.dto;

import com.patientapi.app.global.response.Response;

import lombok.Getter;

@Getter
public class PatientSaveResponse extends Response {
	private final long id;
	
	public PatientSaveResponse(String code, long id) {
		super(code);
		this.id = id;
	}
}
