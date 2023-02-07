package com.patientapi.app.patient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="patient")
public class Patient {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idx;
	
	@Column(nullable=false, length=20)
	private String name;
	
	@Column(nullable=false, columnDefinition="tinyint(3) unsigned")
	private short age;
	@Column(nullable=false, columnDefinition="tinyint(1)")
	private short gender;
	
	@Column(nullable=false, columnDefinition="boolean default false")
	private Boolean hasDisease;
	
	@Column(nullable=false, length=50)
	private String image;
}
