package it.lucamezzolla.optioncalc.model;

import it.lucamezzolla.optioncalc.validation.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public record OptionInput(
        OptionType type,
        BigDecimal currentUnderlyingPrice,
        BigDecimal strike,
        BigDecimal bid,
        BigDecimal ask,
        int contracts,
        int multiplier,
        BigDecimal commissions,
        LocalDate expirationDate,
        BigDecimal scenarioMin,
        BigDecimal scenarioMax,
        BigDecimal scenarioStep) {

    public OptionInput {
        requireNotNull(type, "validation.type.required");
        requireNotNull(currentUnderlyingPrice, "validation.currentPrice.required");
        requireNotNull(strike, "validation.strike.required");
        requireNotNull(bid, "validation.bid.required");
        requireNotNull(ask, "validation.ask.required");
        requireNotNull(commissions, "validation.commissions.required");
        requireNotNull(expirationDate, "validation.expiration.required");
        requireNotNull(scenarioMin, "validation.scenarioMin.required");
        requireNotNull(scenarioMax, "validation.scenarioMax.required");
        requireNotNull(scenarioStep, "validation.scenarioStep.required");

        requirePositive(currentUnderlyingPrice, "validation.currentPrice.positive");
        requirePositive(strike, "validation.strike.positive");
        requireNonNegative(bid, "validation.bid.nonNegative");
        requirePositive(ask, "validation.ask.positive");
        requireNonNegative(commissions, "validation.commissions.nonNegative");
        requireNonNegative(scenarioMin, "validation.scenarioMin.nonNegative");
        requirePositive(scenarioMax, "validation.scenarioMax.positive");
        requirePositive(scenarioStep, "validation.scenarioStep.positive");

        if (ask.compareTo(bid) < 0) {
            throw new ValidationException("validation.askBelowBid");
        }
        if (contracts <= 0) {
            throw new ValidationException("validation.contracts.positive");
        }
        if (multiplier <= 0) {
            throw new ValidationException("validation.multiplier.positive");
        }
        if (scenarioMax.compareTo(scenarioMin) < 0) {
            throw new ValidationException("validation.scenarioRange");
        }

        BigDecimal rows = scenarioMax.subtract(scenarioMin)
                .divide(scenarioStep, 0, java.math.RoundingMode.DOWN);
        if (rows.compareTo(BigDecimal.valueOf(10_000)) > 0) {
            throw new ValidationException("validation.tooManyRows");
        }
    }

    /**
     * For the purchase of a Call or Put, the conservative reference is the Ask.
     */
    public BigDecimal purchasePremium() {
        return ask;
    }

    public int controlledShares() {
        try {
            return Math.multiplyExact(contracts, multiplier);
        } catch (ArithmeticException ex) {
            throw new ValidationException("validation.controlledSharesOverflow");
        }
    }

    private static <T> T requireNotNull(T value, String messageKey) {
        if (Objects.isNull(value)) {
            throw new ValidationException(messageKey);
        }
        return value;
    }

    private static void requirePositive(BigDecimal value, String messageKey) {
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(messageKey);
        }
    }

    private static void requireNonNegative(BigDecimal value, String messageKey) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException(messageKey);
        }
    }
}
