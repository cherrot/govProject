package com.cherrot.govproject.dao.exceptions;

public class PreexistingEntityException extends Exception {
//public class PreexistingEntityException extends RuntimeException {
    public PreexistingEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    public PreexistingEntityException(String message) {
        super(message);
    }
}
