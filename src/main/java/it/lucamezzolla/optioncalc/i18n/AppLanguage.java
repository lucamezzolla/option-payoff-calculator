package it.lucamezzolla.optioncalc.i18n;

import java.util.Locale;

public enum AppLanguage {
    ENGLISH(Locale.ENGLISH, "English"),
    ITALIAN(Locale.ITALIAN, "Italiano"),
    SPANISH(Locale.forLanguageTag("es"), "Español"),
    PORTUGUESE(Locale.forLanguageTag("pt"), "Português"),
    FRENCH(Locale.FRENCH, "Français");

    private final Locale locale;
    private final String displayName;

    AppLanguage(Locale locale, String displayName) {
        this.locale = locale;
        this.displayName = displayName;
    }

    public Locale locale() {
        return locale;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
