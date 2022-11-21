package org.flameyosflow.greenbank.api;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.text.MessageFormat;

import org.flameyosflow.greenbank.GreenBankMain;
import org.flameyosflow.greenbank.utils.EcoUtils;
import org.flameyosflow.greenbank.utils.UTF8PropertiesControl;

public class EcoUtilsSupport {
    private final static Pattern NODOUBLEMARK = Pattern.compile("''");
    private static final String MESSAGES = "messages";
    private static EcoUtils instance;
    private static transient GreenBankMain greenBank;
    private static String result;
    
    private transient static Map<String, MessageFormat> messageFormatCache = new HashMap<>();
    //private transient Locale currentLocale = defaultLocale;
    private static ResourceBundle NULL_BUNDLE = new ResourceBundle() {
        public Enumeration<String> getKeys() {
            return null;
        }

        protected Object handleGetObject(final String key) {
            return null;
        }
    };
    
    private static ResourceBundle defaultBundle;
    private static ResourceBundle customBundle;
    private static ResourceBundle localeBundle;

    public EcoUtilsSupport(GreenBankMain greenBank) {
        EcoUtilsSupport.greenBank = greenBank;
        defaultBundle = ResourceBundle.getBundle(MESSAGES, Locale.ENGLISH, new UTF8PropertiesControl());
        localeBundle = defaultBundle;
        customBundle = NULL_BUNDLE;
    }

    public static String tl(final String string, final Object... objects) {
        if (instance == null) {
            return null;
        }
        if (objects.length == 0) {
            return NODOUBLEMARK.matcher(EcoUtilsSupport.translate(string)).replaceAll("'");
        }
        return EcoUtilsSupport.format(string, objects);
        
    }

    public static String format(final String string, final Object... objects) {
        String format = translate(string);
        MessageFormat messageFormat = messageFormatCache.get(format);
        if (messageFormat == null) {
            try {
                messageFormat = new MessageFormat(format);
            } catch (final IllegalArgumentException e) {
                greenBank.logError("Invalid Translation key for '" + string + "': " + e.getMessage());
                format = format.replaceAll("\\{(\\D*?)\\}", "\\[$1\\]");
                messageFormat = new MessageFormat(format);
            }
            messageFormatCache.put(format, messageFormat);
        }

        String msg = messageFormat.format(objects);

        return msg.replace(' ', ' '); // replace nbsp with a space
    }

    public static String translate(final String string) {
        try {
            try {
                return customBundle.getString(string);
            } catch (final MissingResourceException ex) {
                return localeBundle.getString(string);
            }
        } catch (final MissingResourceException ex) {
            if (greenBank == null || greenBank.getSettings().isDebug()) {
                greenBank.logger.warning(String.format("Missing translation key \"%s\" in translation file %s", ex.getKey(), localeBundle.getLocale().toString()));
                greenBank.logger.warning(ex.toString());
            }
            return defaultBundle.getString(string);
        }
    }
}
