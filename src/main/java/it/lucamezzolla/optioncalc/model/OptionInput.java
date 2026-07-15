package it.lucamezzolla.optioncalc.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public record OptionInput(
        OptionType type,
        BigDecimal currentUnderlyingPrice,
        BigDecimal strike,
        BigDecimal unitPremium,
        int contracts,
        int multiplier,
        BigDecimal commissions,
        LocalDate expirationDate,
        BigDecimal scenarioMin,
        BigDecimal scenarioMax,
        BigDecimal scenarioStep) {

    public OptionInput {
        Objects.requireNonNull(type, "Il tipo di opzione è obbligatorio");
        Objects.requireNonNull(currentUnderlyingPrice, "Il prezzo corrente è obbligatorio");
        Objects.requireNonNull(strike, "Lo strike è obbligatorio");
        Objects.requireNonNull(unitPremium, "Il premio è obbligatorio");
        Objects.requireNonNull(commissions, "Le commissioni sono obbligatorie");
        Objects.requireNonNull(expirationDate, "La scadenza è obbligatoria");
        Objects.requireNonNull(scenarioMin, "Il prezzo minimo è obbligatorio");
        Objects.requireNonNull(scenarioMax, "Il prezzo massimo è obbligatorio");
        Objects.requireNonNull(scenarioStep, "Il passo è obbligatorio");

        requirePositive(currentUnderlyingPrice, "Il prezzo corrente deve essere maggiore di zero");
        requirePositive(strike, "Lo strike deve essere maggiore di zero");
        requireNonNegative(unitPremium, "Il premio non può essere negativo");
        requireNonNegative(commissions, "Le commissioni non possono essere negative");
        requireNonNegative(scenarioMin, "Il prezzo minimo non può essere negativo");
        requirePositive(scenarioMax, "Il prezzo massimo deve essere maggiore di zero");
        requirePositive(scenarioStep, "Il passo deve essere maggiore di zero");

        if (contracts <= 0) {
            throw new IllegalArgumentException("Il numero di contratti deve essere maggiore di zero");
        }
        if (multiplier <= 0) {
            throw new IllegalArgumentException("Il moltiplicatore deve essere maggiore di zero");
        }
        if (scenarioMax.compareTo(scenarioMin) < 0) {
            throw new IllegalArgumentException("Il prezzo massimo deve essere maggiore o uguale al minimo");
        }

        BigDecimal rows = scenarioMax.subtract(scenarioMin)
                .divide(scenarioStep, 0, java.math.RoundingMode.DOWN);
        if (rows.compareTo(BigDecimal.valueOf(10_000)) > 0) {
            throw new IllegalArgumentException("L'intervallo genera troppe righe: aumenta il passo");
        }
    }

    public int controlledShares() {
        return Math.multiplyExact(contracts, multiplier);
    }

    private static void requirePositive(BigDecimal value, String message) {
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void requireNonNegative(BigDecimal value, String message) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(message);
        }
    }
}
