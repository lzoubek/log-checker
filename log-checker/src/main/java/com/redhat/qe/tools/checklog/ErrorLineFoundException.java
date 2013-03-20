package com.redhat.qe.tools.checklog;

public class ErrorLineFoundException extends Exception {

    public ErrorLineFoundException(String message, Throwable cause) {
	super(message,cause);
    }
    public ErrorLineFoundException(String message) {
	super(message);
    }

    /**
     * 
     */
    private static final long serialVersionUID = -666987637386145604L;

}
