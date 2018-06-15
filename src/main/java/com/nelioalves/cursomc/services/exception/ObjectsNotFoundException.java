package com.nelioalves.cursomc.services.exception;

public class ObjectsNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ObjectsNotFoundException(String msg){
		super(msg);
	}
	
	public ObjectsNotFoundException(String msg, Throwable cause){
		super(msg, cause);
	}
	
}
