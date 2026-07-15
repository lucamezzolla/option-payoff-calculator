package it.lucamezzolla.optioncalc.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record OptionSummary(
        BigDecimal premiumTotal,
        BigDecimal totalCost,
        BigDecimal breakEven,
        BigDecimal maximumLoss,
        int controlledShares,
        long daysToExpiration,
        BigDecimal movementToBreakEvenPercent) {

    public static long daysToExpiration(LocalDate expirationDate) {
        return Math.max(0, ChronoUnit.DAYS.between(LocalDate.now(), expirationDate));
    }
}
