package com.avanse.exception;

import java.util.Date;
import java.util.List;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<ObjectError> fieldErrorList = ex.getBindingResult().getAllErrors();

		StringBuffer error = new StringBuffer();
		int i = 0;
		for (ObjectError e : fieldErrorList) {
			error.append(++i + ")" + e.getDefaultMessage() + "\n");
		}

		ErrorDetails errorDetails = new ErrorDetails(new Date(),error.toString().trim(),"Validation Failed");
		return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getLocalizedMessage().trim(),"Validation Failed");
		return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);

	}

}
