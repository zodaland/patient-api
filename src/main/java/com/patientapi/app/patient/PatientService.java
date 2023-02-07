package com.patientapi.app.patient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.patientapi.app.global.exception.ImageProcessException;
import com.patientapi.app.global.response.Response;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientService {
	private final PatientRepository repo;
	private final PatientProperties props;
	
	public Response getDetail(long id) {
		Patient patient = repo.findById(id).orElse(null);
		if (patient == null) {
			return new Response("1001");
		}
		return PatientResponse.builder()
				.name(patient.getName())
				.age(patient.getAge())
				.gender(patient.getGender())
				.hasDisease(patient.getHasDisease())
				.imageUrl(props.getImageUrl() + patient.getImage())
				.code("1000")
				.build();
	}
	
	@Transactional(rollbackFor=ImageProcessException.class)
	public Response save(PatientRequest dto, MultipartFile image) throws ImageProcessException {
		// 파일 확장자를 찾고 파일명을 결정합니다.
		String contentType = image.getContentType();
		String extension;
		switch (contentType) {
		case "image/jpeg":
			extension = ".jpg";
			break;
		case "image/png":
			extension = ".png";
			break;
		default:
			return new Response("1003");
		}
		String fileName = Long.toString(System.nanoTime()) + extension;
		
		// 1 단계 환자 정보를 업로드 합니다.
		Patient patient = Patient.builder()
				.name(dto.getName())
				.age(dto.getAge())
				.gender(dto.getGender())
				.hasDisease(dto.getHasDisease())
				.image(fileName)
				.build();
		repo.save(patient);
		
		long id = patient.getIdx();
		
		// 2 단계 관련 이미지를 업로드 합니다.
		String path = props.getImagePath();
		// 디렉터리가 없다면 생성합니다.
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		//파일 생성 후 타겟 티렉터리로 옮깁니다.
		File myFile = new File(dir.getAbsolutePath() + File.separator + fileName);
		try {
			image.transferTo(myFile);
		} catch (Exception e) {
			e.printStackTrace();
			// Exception 발생 시 커스텀 Exception을 설정하여 결과 처리를 GlobalExceptionHandler에 위임하고 rollback 처리합니다. 
			throw new ImageProcessException();
		}
		
		return new PatientSaveResponse("1000", id);
	}
	
	public byte[] getImage(String fileName) throws ImageProcessException {
		String path = props.getImagePath() + fileName;
		
		try {
			InputStream inputStream = new FileInputStream(path);
			byte[] bytes = IOUtils.toByteArray(inputStream);
			inputStream.close();
			return bytes;
		} catch (Exception e) {
			throw new ImageProcessException();
		}
	}
	
	public void delete(long id) {
		if (repo.existsById(id)) {
			repo.deleteById(id);
		}
	}
}
