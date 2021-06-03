package sk.stuba.fei.weblab.matlabadapterclient;

public class MatlabAdapterException extends Exception {
	
	private static final long serialVersionUID = 1L;
	// http status code (mainly 4xx and 5xx status codes)
	private int httpStatusCode;
	private String errorMessage;
	// -1 means not MatlabAdapter specific error, but maybe server error, 
	// in that case only httpErrorCode and errorMessage will be filled
	private int errorCode = -1;
	private String exceptionMessage;
	private String exceptionStackTrace;
	
	
	public int getHttpStatusCode() {
		return httpStatusCode;
	}
	
	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(int matlabAdapterErrorId) {
		this.errorCode = matlabAdapterErrorId;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	
	public String getExceptionStackTrace() {
		return exceptionStackTrace;
	}
	
	public void setExceptionStackTrace(String exceptionStackTrace) {
		this.exceptionStackTrace = exceptionStackTrace;
	}
}
