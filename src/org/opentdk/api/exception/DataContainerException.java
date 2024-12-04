package org.opentdk.api.exception;

public class DataContainerException extends RuntimeException {

    public DataContainerException(Exception e) {
        super(e);
    }

    public DataContainerException(String message) {
        super(message);
    }
}
