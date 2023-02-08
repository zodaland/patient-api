package com.patientapi.app.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import com.patientapi.app.global.response.Response;

/*
 * 요청 내용 처리 중 발생하는 exception에 대한 글로벌 핸들러 입니다.
 * 처리 중 발생하는 exception에 대해 해당 핸들러에서 처리되어 결과가 리턴됩니다. 
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ImageProcessException.class)
	public ResponseEntity<Response> imageProcessException(ResponseStatusException e) {
		return ResponseEntity.internalServerError().body(new Response("1004"));
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Response> methodArgumentTypeMismatchException(Exception e) {
		return ResponseEntity.internalServerError().body(new Response("9996"));
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Response> httpRequestMethodNotSupportedException(Exception e) {
		return ResponseEntity.internalServerError().body(new Response("9997"));
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Response> httpMessageNotReadableException(Exception e) {
		return ResponseEntity.internalServerError().body(new Response("9998"));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> exception(Exception e) {
		return ResponseEntity.internalServerError().body(new Response("9999"));
	}
}