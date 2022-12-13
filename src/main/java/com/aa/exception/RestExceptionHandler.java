package com.aa.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.aa.domain.HttpResponse;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler
	public ResponseEntity<HttpResponse> handleInternalServerException(Exception ex) {
		HttpResponse response = new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
		
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler
	public ResponseEntity<HttpResponse> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
		return new ResponseEntity<>(new HttpResponse(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(),
				ex.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<HttpResponse> handleDuplicateEmailException(DuplicateEmailException ex) {
		return new ResponseEntity<>(new HttpResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(),
				ex.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		
		List<String> listErrors = new ArrayList<>();
		
		for (FieldError fieldError : fieldErrors) {
			String errorMessage = fieldError.getDefaultMessage();
			listErrors.add(errorMessage);
		}
		
		HttpResponse httpResponse = new HttpResponse(status, status.value(), listErrors.toString());
		
		return new ResponseEntity<>(httpResponse, headers, status);
	}
	
	
}







