package com.nelioalves.cursomc.resources.exception;

import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.nelioalves.cursomc.services.exception.AuthorizationException;
import com.nelioalves.cursomc.services.exception.DataIntegrityException;
import com.nelioalves.cursomc.services.exception.ObjectsNotFoundException;

/*
 * CLASSE AUXILIAR QUE MONITORA AS EXCESSOES QUE OCORREM NO CONTROLLER
 */
@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ObjectsNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectsNotFoundException e, HttpServletRequest request){
		
		Long currentDiteTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");		
		Date currentDate = new Date(currentDiteTime);
		
		StandardError error =  new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), currentDate);		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrityException(DataIntegrityException e, HttpServletRequest request){
		
		Long currentDiteTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");		
		Date currentDate = new Date(currentDiteTime);
		
		StandardError error =  new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), currentDate);		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
		
		Long currentDiteTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");		
		Date currentDate = new Date(currentDiteTime);
		
		ValidationError error =  new ValidationError(HttpStatus.BAD_REQUEST.value(), "Erro de Validação", currentDate);
		
		for(FieldError x : e.getBindingResult().getFieldErrors()) {
			error.addError(x.getField(), x.getDefaultMessage());
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}	
	
	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<StandardError> authorization(AuthorizationException e, HttpServletRequest request){
		
		Long currentDiteTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");		
		Date currentDate = new Date(currentDiteTime);
		
		StandardError error =  new StandardError(HttpStatus.FORBIDDEN.value(), e.getMessage(), currentDate);		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}
}
