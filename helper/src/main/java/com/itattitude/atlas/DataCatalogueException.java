package com.itattitude.atlas;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;

public class DataCatalogueException extends Exception {

	
	private static final long serialVersionUID = -1L;
	
	public enum ErrorCodeEnum {
		NO_ERROR,
		UNDEFINED,
		MISSING_ATLAS_REST_ADDRESS,
		INVALID_OR_NOT_ACTIVE_SERVER_ADDRESS,
		MISSING_USERNAME_AND_PASSWORD,
		CLIENT_RESPONSE_ERROR
	}
	
 
	
	private Boolean _unrecoverable=true;
	private ErrorCodeEnum _errorCode=ErrorCodeEnum.NO_ERROR;
	private Status _status;
	
	public DataCatalogueException(Throwable cause) {
		super(cause);
		_errorCode=ErrorCodeEnum.UNDEFINED;
	}
	
	public DataCatalogueException(String message) {
		super(message);
		_errorCode = ErrorCodeEnum.UNDEFINED;
	}
	
	public DataCatalogueException(String message,Throwable cause) {
		super(message,cause);
		_errorCode=ErrorCodeEnum.UNDEFINED;
	}
	
	public DataCatalogueException(String message, ErrorCodeEnum errorCode) {
		super(message);
		_unrecoverable =false;
		_errorCode=errorCode;
	}
	
	public DataCatalogueException(String message, ErrorCodeEnum errorCode, Throwable cause) {
		super(message,cause);
		_errorCode=errorCode;
	}


	public DataCatalogueException(String message, Status status, Throwable cause) {
		super(message,cause);
		_unrecoverable=false;
		_errorCode=ErrorCodeEnum.CLIENT_RESPONSE_ERROR;
		_status=status;
	}
	
    public DataCatalogueException(DataCatalogueAPI.API api, Exception e) {
        super("Metadata service API " + api.getMethod() + " : " + api.getNormalizedPath() + " failed", e);
    }
    
    private DataCatalogueException(DataCatalogueAPI.API api, ClientResponse.Status status, String response) {
        super("Metadata service API " + api + " failed with status " + (status != null ? status.getStatusCode() : -1)
                + " (" + status + ") Response Body (" + response + ")");
        _status = status;
    }
    
    public DataCatalogueException(DataCatalogueAPI.API api, ClientResponse response) {
        this(api, ClientResponse.Status.fromStatusCode(response.getStatus()), response.getEntity(String.class));
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
