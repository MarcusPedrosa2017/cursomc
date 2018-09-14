package com.nelioalves.cursomc.resources.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ValidationError extends StandardError{
	
	private static final long serialVersionUID = 1L;
	
	private List<FieldMessage> errors = new ArrayList<>();
	
	public ValidationError(Integer status, String msg, Date date) {
		super(status, msg, date);
		// TODO Auto-generated constructor stub
	}

	public List<FieldMessage> getErros() {
		return errors;
	}

	public void addError(String fieldName, String message) {
		this.errors.add(new FieldMessage(fieldName, message));
	}	
	
}
