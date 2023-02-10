package com.patientapi.app.patient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patientapi.app.global.exception.ImageProcessException;
import com.patientapi.app.global.response.Response;
import com.patientapi.app.patient.dto.PatientRequest;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTests {
	private MockMvc mvc;
	
	@Mock
	private PatientService service;
	
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.standaloneSetup(new PatientController(service))
				.build();
	}
	
	@Nested
	@DisplayName("getDetail")
	class GetDetail {
		@Test
		@DisplayName("success")
		public void getDetail() throws Exception {
			given(service.getDetail(anyLong())).willReturn(new Response("1000"));
			
			mvc.perform(get("/1")
					.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").exists());
		}
	}
	
	@Nested
	@DisplayName("getImage")
	class GetImage {
		@Test
		@DisplayName("invalid image type ")
		public void getImageFail() throws Exception {
			Assertions.assertThatThrownBy(() -> mvc.perform(get("/image/test.pdf")
					.accept(MediaType.APPLICATION_JSON)
			)).hasCause(new ImageProcessException());
		}
		
		@Test
		@DisplayName("success")
		public void getImage() throws Exception {
			given(service.getImage(anyString())).willReturn(new byte[1]);
			
			mvc.perform(get("/image/test.jpg")
					.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.IMAGE_JPEG));
		}
	}
	
	@Nested
	@DisplayName("save cases")
	class Save {
		@Test
		@DisplayName("image is null")
		public void imageIsNull() throws Exception {
			ObjectMapper objectMapper = new ObjectMapper();
			PatientRequest dto = new PatientRequest("name", (short) 1, (short) 1, false);
			//MockMultipartFile image = new MockMultipartFile("image", "test.pdf", "application/pdf", null);
			MockMultipartFile data = new MockMultipartFile("data", "", "application/json", objectMapper.writeValueAsBytes(dto));
			
			
			mvc.perform(multipart("/")
					.file("image", null)
					.file(data))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").exists());			
		}
		
		@Test
		@DisplayName("success")
		public void success() throws Exception {
			ObjectMapper objectMapper = new ObjectMapper();
			PatientRequest dto = new PatientRequest("name", (short) 1, (short) 1, false);
			MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[1]);
			MockMultipartFile data = new MockMultipartFile("data", "", "application/json", objectMapper.writeValueAsBytes(dto));
			
			given(service.save(any(), any(MultipartFile.class))).willReturn(new Response("1000"));
			
			mvc.perform(multipart("/")
					.file(image)
					.file(data))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.code").exists());
		}
	}
	
	
	@Nested
	@DisplayName("delete cases")
	class Delete {
		@Test
		@DisplayName("success")
		public void success() throws Exception {
			mvc.perform(delete("/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").exists());
		}
	}
}
