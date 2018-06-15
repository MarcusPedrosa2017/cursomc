package com.nelioalves.cursomc.resources.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.nelioalves.cursomc.services.exception.ObjectsNotFoundException;

/*
 * CLASSE AUXILIAR QUE MONITORA AS EXCESSOES QUE OCORREM NO CONTROLLER
 */
@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ObjectsNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectsNotFoundException e, HttpServletRequest request){
		
		StandardError error =  new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
}
