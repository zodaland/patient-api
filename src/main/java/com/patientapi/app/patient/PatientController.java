package com.patientapi.app.patient;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.patientapi.app.global.exception.ImageProcessException;
import com.patientapi.app.global.response.Response;
import com.patientapi.app.patient.dto.PatientRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class PatientController {
	private final PatientService service;
	
	@GetMapping("/{id}")
	public Response getDetail(@PathVariable long id) {
		return service.getDetail(id);
	}
	
	@GetMapping("/image/{image}")
	public ResponseEntity<byte[]> getImage(@PathVariable String image) throws ImageProcessException {
		String extension = image.substring(image.indexOf("."));
		MediaType mediaType;
		switch (extension) {
		case ".jpg":
			mediaType = MediaType.IMAGE_JPEG;
			break;
		case ".png":
			mediaType = MediaType.IMAGE_PNG;
			break;
		default:
			throw new ImageProcessException();
		}
		byte[] file = service.getImage(image);
		
		return ResponseEntity.ok().contentType(mediaType).body(file);
	}
	
	@PostMapping
	public ResponseEntity<Response> save(
			@RequestPart(value = "data") @Validated PatientRequest dto,
			@RequestPart MultipartFile image
	) throws ImageProcessException {
		if (image == null || image.isEmpty()) {
			return ResponseEntity.badRequest().body(new Response("1004"));
		}
		Response response = service.save(dto, image);
		return response.getCode().equals("1000") ? ResponseEntity.status(HttpStatus.CREATED).body(response) : ResponseEntity.badRequest().body(response);
	}
	
	@DeleteMapping("/{id}")
	public Response remove(@PathVariable long id) {
		service.delete(id);
		return new Response("1000");
	}
}
