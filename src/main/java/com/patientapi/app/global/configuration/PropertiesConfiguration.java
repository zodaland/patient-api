package com.patientapi.app.global.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.patientapi.app.patient.PatientProperties;

/*
 * properties 내 생성한 프로퍼티 클래스들을 bean에 등록하기 위한 클래스
 * EnableConfigurationProperties 내 추가되는 클래스들을 입력해주어야 properties를 객체로 사용 가능하다.
 */
@Configuration
@EnableConfigurationProperties({ PatientProperties.class })
public class PropertiesConfiguration {
	
}