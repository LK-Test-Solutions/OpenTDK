package org.opentdk.api.exception;

/**
 * DataContainerException is a custom runtime exception that represents
 * errors related to data container operations.
 * <p>
 * This exception can be initialized with a detailed error message or
 * another exception to encapsulate the original cause of the error.
 */
public class DataContainerException extends RuntimeException {

    public DataContainerException(Exception e) {
        super(e);
    }

    public DataContainerException(String message) {
        super(message);
    }
}
