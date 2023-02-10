package com.patientapi.app.patient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.patientapi.app.global.exception.ImageProcessException;
import com.patientapi.app.global.response.Response;
import com.patientapi.app.patient.dao.Patient;
import com.patientapi.app.patient.dao.PatientRepository;
import com.patientapi.app.patient.dto.PatientRequest;

@ExtendWith(MockitoExtension.class)
@Transactional
public class PatientServiceTests {
	@InjectMocks
	private PatientService service;
	
	@Mock
	private PatientRepository repo;
	
	@Mock
	private PatientProperties props;
	
	@Nested
	@DisplayName("patient save cases")
	public class Save {
		@Test
		@DisplayName("content type == null case")
		public void contentTypeNull() throws ImageProcessException {
			// content type is null
			MultipartFile file = new MockMultipartFile("test.jpg", new byte[1]);
			PatientRequest dto = new PatientRequest("name", (short) 1, (short) 1, false);
			
			Response response = service.save(dto, file);
			
			assertEquals("1003", response.getCode());
		}
		
		@Test
		@DisplayName("content type not equals jpg, png")
		public void contentTypeInvalid() throws ImageProcessException {
			// content type is xlsx
			MultipartFile file = new MockMultipartFile("test.jpg", null, "xlsx", new byte[1]);
			PatientRequest dto = new PatientRequest("name", (short) 1, (short) 1, false);
			
			Response response = service.save(dto, file);
			
			assertEquals("1003", response.getCode());
		}
		
		@Test
		@DisplayName("Exception and db insert failure")
		public void exception() throws ImageProcessException, IllegalStateException, IOException {
			MultipartFile file = new MockMultipartFile("test.jpg", null, "image/jpeg", new byte[1]);
			MultipartFile spy = spy(file);
			PatientRequest dto = new PatientRequest("name", (short) 1, (short) 1, false);
			
			when(props.getImagePath()).thenReturn("./");
			doThrow(new IOException(), new IllegalStateException()).when(spy).transferTo(any(File.class));
			
			assertThrows(ImageProcessException.class, () -> service.save(dto, spy));
			verify(repo, times(1)).save(any(Patient.class));
		}
		
		@Test
		@DisplayName("success")
		public void success() throws IllegalStateException, IOException, ImageProcessException {
			MultipartFile file = new MockMultipartFile("test.jpg", null, "image/jpeg", new byte[1]);
			MultipartFile spy = spy(file);
			PatientRequest dto = new PatientRequest("name", (short) 1, (short) 1, false);
			
			when(props.getImagePath()).thenReturn("./");
			doNothing().when(spy).transferTo(any(File.class));
			
			Response response = service.save(dto, spy);
			
			assertEquals("1000", response.getCode());
		}
	}
	
	@Nested
	@DisplayName("patient getDetail cases")
	public class GetDetail {
		@Test
		@DisplayName("patient is null")
		public void patientIsNull() {
			//wrong id
			long id = 1;
			
			Response response = service.getDetail(id);
			
			assertEquals("1001", response.getCode());
		}
		
		@Test
		@DisplayName("success")
		public void success() throws IllegalStateException, IOException, ImageProcessException {
			long id = 1;
			Patient patient = Patient.builder()
					.name("test")
					.age((short) 1)
					.gender((short) 1)
					.hasDisease(false)
					.image("test.jpg")
					.build();
			when(repo.findById(any(Long.class))).thenReturn(Optional.of(patient));
			Response response = service.getDetail(id);
			
			assertEquals("1000", response.getCode());
		}
	}
	
	@Nested
	@DisplayName("patient getImage cases")
	public class GetImage {
		@Test
		@DisplayName("wrong file name")
		public void wrongFileName() {
			String fileName = "test.jpg";
			
			when(props.getImagePath()).thenReturn("./");
			
			assertThrows(ImageProcessException.class, () -> service.getImage(fileName));
		}
		
		@Disabled
		@Test
		@DisplayName("success")
		public void success() {
			
		}
	}
	
	@Nested
	@DisplayName("patient delete cases")
	public class Delete {
		@Test
		@DisplayName("wring id")
		public void wrongId() {
			long id = 1;
			
			when(repo.findById(any(Long.class))).thenReturn(Optional.ofNullable(null));
			service.delete(id);
			
			verify(repo, times(1)).findById(id);
			verify(repo, times(0)).deleteById(id);
		}
		
		@Test
		@DisplayName("success")
		public void success() {
			long id = 1;
			Patient patient = Patient.builder()
					.name("test")
					.age((short) 1)
					.gender((short) 1)
					.hasDisease(false)
					.image("test")
					.build();
			
			when(repo.findById(any(Long.class))).thenReturn(Optional.of(patient));
			service.delete(id);
			
			verify(repo, times(1)).findById(id);
			verify(repo, times(1)).deleteById(id);
		}
	}
}
