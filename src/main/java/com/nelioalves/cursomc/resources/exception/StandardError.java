package com.nelioalves.cursomc.resources.exception;

import java.io.Serializable;
import java.util.Date;


public class StandardError implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	private Integer status;
	private String msg;
	private Date date;
	
	public StandardError(Integer status, String msg, Date date) {
		super();
		this.status = status;
		this.msg = msg;
		this.date = date;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
