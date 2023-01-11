package org.flameyosflow.greenbank.api.errors;

public class CannotPerformTransactionOnSelf extends Exception {
    public CannotPerformTransactionOnSelf(String message) {
        super(message);
    }
}
