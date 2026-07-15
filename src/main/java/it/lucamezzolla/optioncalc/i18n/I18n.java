package it.lucamezzolla.optioncalc.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public final class I18n {

    private static final String BUNDLE_NAME = "i18n.Messages";

    private static AppLanguage language = AppLanguage.ENGLISH;
    private static ResourceBundle bundle = loadBundle(language.locale());

    private I18n() {
    }

    public static AppLanguage language() {
        return language;
    }

    public static Locale locale() {
        return language.locale();
    }

    public static void setLanguage(AppLanguage newLanguage) {
        language = newLanguage == null ? AppLanguage.ENGLISH : newLanguage;
        bundle = loadBundle(language.locale());
    }

    public static String text(String key, Object... arguments) {
        String pattern = bundle.getString(key);
        if (arguments == null || arguments.length == 0) {
            return pattern;
        }
        MessageFormat format = new MessageFormat(pattern, locale());
        return format.format(arguments);
    }

    private static ResourceBundle loadBundle(Locale locale) {
        ResourceBundle.Control noDefaultLocaleFallback =
                ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES);
        return ResourceBundle.getBundle(BUNDLE_NAME, locale, noDefaultLocaleFallback);
    }
}
