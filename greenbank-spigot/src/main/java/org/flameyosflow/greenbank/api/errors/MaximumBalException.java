package org.flameyosflow.greenbank.api.errors;

import static org.flameyosflow.greenbank.utils.EcoUtils.tl;

public class MaximumBalException extends Exception {
    public MaximumBalException() {
        super(tl("maxMoney"));
    }
}