package com.nelioalves.cursomc.resources.exception;

import java.sql.Date;
import java.text.SimpleDateFormat;

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
		
		Long currentDiteTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");		
		Date currentDate = new Date(currentDiteTime);
		
		StandardError error =  new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), currentDate);		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
}
