package com.avanse.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request){
		ErrorDetails errorDetail= new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(AppException.class)
	public ResponseEntity<?> gloablAppExceptionHandler(AppException e,WebRequest req){
		ErrorDetails errorDetails= new ErrorDetails(new Date(), e.getMessage(), req.getDescription(false));
		return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
		
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globleExcpetionHandler(Exception ex,WebRequest request) {
		ErrorDetails errorDetail = new ErrorDetails(new Date(),ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetail,
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
}
