package com.patientapi.app;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.patientapi.app.patient.PatientController;
import com.patientapi.app.patient.PatientService;

@SpringBootTest
class AppApplicationTests {
	@Autowired
	private PatientController controller;
	
	@Autowired
	private PatientService service;

	@Test
	void contextLoads() {
		Assertions.assertThat(controller).isNotNull();
		Assertions.assertThat(service).isNotNull();
	}

}
