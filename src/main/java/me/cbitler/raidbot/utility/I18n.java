package me.cbitler.raidbot.utility;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {
    private static ResourceBundle bundle;
    private final static String BUNDLE_NAME = "me.cbitler.raidbot.res.MessageBundle";

    private I18n() {
    }

    public static Locale getLocale() {
        return Locale.getDefault();
    }

    public static boolean isSupported(Locale l) {
        return Arrays.asList(Locale.getAvailableLocales()).contains(l);
    }

    public static void setLocale(Locale l) {
        Locale.setDefault(l);
    }

    public static String getMessage(String key) {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(BUNDLE_NAME);
        }
        return bundle.getString(key);
    }


}
