package org.flameyosflow.greenbank.utils;

import org.flameyosflow.greenbank.GreenBankMain;
import org.flameyosflow.greenbank.api.*;

public class EcoUtils extends EcoUtilsSupport {
    public GreenBankMain greenBank;
    public EcoUtils(GreenBankMain greenBank) {
        super(greenBank);
        this.greenBank = greenBank;
    }
}