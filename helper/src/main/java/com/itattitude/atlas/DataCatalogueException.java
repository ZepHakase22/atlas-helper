package com.itattitude.atlas;

import com.sun.jersey.api.client.ClientResponse;

public class DataCatalogueException extends Exception {

	
	private static final long serialVersionUID = -5911322872242040631L;
	
	public enum ErrorCodeEnum {
		NO_ERROR,
		UNDEFINED,
		MISSING_ATLAS_REST_ADDRESS,
		MISSING_USERNAME_AND_PASSWORD
	}
	
	private Boolean _unrecoverable=false;
	private ErrorCodeEnum _errorCode=ErrorCodeEnum.NO_ERROR;
	
	public DataCatalogueException(Throwable cause) {
		_unrecoverable=true;
		_errorCode=ErrorCodeEnum.UNDEFINED;
	}
	
	public DataCatalogueException(String message,ClientResponse.Status status) {
		super(message);
		_errorCode=ErrorCodeEnum.UNDEFINED;
	}
	
	public DataCatalogueException(String message,Throwable cause) {
		super(cause);
		_unrecoverable=true;
		_errorCode=ErrorCodeEnum.UNDEFINED;
	}
	
	public DataCatalogueException(String message, ErrorCodeEnum errorCode) {
		super(message);
		_unrecoverable =false;
		_errorCode=errorCode;
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
}
