package it.lucamezzolla.optioncalc.model;

import java.math.BigDecimal;

public record OptionScenario(
        BigDecimal underlyingPriceAtExpiration,
        BigDecimal intrinsicValuePerShare,
        BigDecimal optionValueTotal,
        BigDecimal grossProfitLoss,
        BigDecimal netProfitLoss,
        BigDecimal roiPercent) {
}
