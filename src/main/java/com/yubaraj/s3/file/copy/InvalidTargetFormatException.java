package com.yubaraj.s3.file.copy;

/**
 * Exception class for invalid target format.
 * 
 * @author Yuba Raj Kalathoki
 * @version 1.0.0
 * @since 1.0.0, Dec 31, 2016
 */
@SuppressWarnings("serial")
public class InvalidTargetFormatException extends Exception {
    public InvalidTargetFormatException(String message) {
	super(message);
    }
}
