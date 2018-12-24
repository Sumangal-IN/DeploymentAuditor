package com.kingfisher.deployment.audit.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.kingfisher.deployment.audit.exception.model.AuditException;

@ControllerAdvice
public class CustomExceptionHandler {
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> exception(Exception ex) {
		ex.printStackTrace();
		return new ResponseEntity<>(new AuditException(400, ex.getClass().getSimpleName(), ex.getMessage()), HttpStatus.NOT_FOUND);
	}
}