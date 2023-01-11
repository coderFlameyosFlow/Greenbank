package org.flameyosflow.greenbank.api.errors;

public class CannotFindUserException extends Exception {
    public CannotFindUserException(String error) {
        super(error);
    }
}