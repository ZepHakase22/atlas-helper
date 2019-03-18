package com.itattitude.atlas;

import com.sun.jersey.api.client.ClientResponse.Status;

public class DataCatalogueException extends Exception {

	
	private static final long serialVersionUID = -1L;
	
	public enum ErrorCodeEnum {
		NO_ERROR,
		UNDEFINED,
		MISSING_ATLAS_REST_ADDRESS,
		MISSING_USERNAME_AND_PASSWORD,
		CLIENT_RESPONSE_ERROR
	}
	
 
	
	private Boolean _unrecoverable=true;
	private ErrorCodeEnum _errorCode=ErrorCodeEnum.NO_ERROR;
	private Status _status;
	
	public DataCatalogueException(Throwable cause) {
		_errorCode=ErrorCodeEnum.UNDEFINED;
	}
	
	public DataCatalogueException(String message,Throwable cause) {
		super(cause);
		_errorCode=ErrorCodeEnum.UNDEFINED;
	}
	
	public DataCatalogueException(String message, ErrorCodeEnum errorCode) {
		super(message);
		_unrecoverable =false;
		_errorCode=errorCode;
	}

	public DataCatalogueException(String message, Status status, Throwable cause) {
		super(cause);
		_unrecoverable=false;
		_errorCode=ErrorCodeEnum.CLIENT_RESPONSE_ERROR;
		_status=status;
	}

	public String getMessage() {
		return super.getMessage();
	}
	
	public Boolean isUnrecoverable() {
		return _unrecoverable;
	}
	
	public Throwable getCause() {
		return super.getCause();
	}
	
	public ErrorCodeEnum getErrorCode() {
		return _errorCode;
	}

	public Status getStatus() {
		return _status;
	}
}
