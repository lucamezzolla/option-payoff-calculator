package it.lucamezzolla.optioncalc.i18n;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class I18nTest {

    @Test
    void englishShouldNotFallBackToTheOperatingSystemLanguage() {
        Locale originalDefault = Locale.getDefault();
        try {
            Locale.setDefault(Locale.ITALIAN);
            I18n.setLanguage(AppLanguage.ENGLISH);

            assertEquals("Option Payoff Calculator", I18n.text("header.title"));
            assertEquals("Clear", I18n.text("button.clear"));
            assertEquals("Scenarios ↑", I18n.text("button.scenarios"));
        } finally {
            Locale.setDefault(originalDefault);
        }
    }

    @Test
    void everyLanguageShouldResolveCoreInterfaceKeys() {
        for (AppLanguage language : AppLanguage.values()) {
            I18n.setLanguage(language);
            assertFalse(I18n.text("app.title").isBlank());
            assertFalse(I18n.text("button.scenarios").isBlank());
            assertFalse(I18n.text("scenario.dialog.title").isBlank());
            assertFalse(I18n.text("table.netProfitLoss").isBlank());
            assertFalse(I18n.text("validation.askBelowBid").isBlank());
        }
    }
}
