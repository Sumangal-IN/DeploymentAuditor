package com.kingfisher.deployment.audit.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.kingfisher.deployment.audit.exception.model.AuditError;

@ControllerAdvice
public class CustomExceptionHandler {
	
	 Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);
	 
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> exception(Exception ex) {
		logger.error("Oops!", ex);
		return new ResponseEntity<>(new AuditError(400, ex.getClass().getSimpleName(), ex.getMessage()), HttpStatus.NOT_FOUND);
	}
}