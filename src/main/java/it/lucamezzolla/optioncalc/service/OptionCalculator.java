package it.lucamezzolla.optioncalc.service;

import it.lucamezzolla.optioncalc.model.OptionInput;
import it.lucamezzolla.optioncalc.model.OptionScenario;
import it.lucamezzolla.optioncalc.model.OptionSummary;
import it.lucamezzolla.optioncalc.model.OptionType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public final class OptionCalculator {

    private static final int SCALE = 10;

    public OptionSummary summarize(OptionInput input) {
        BigDecimal controlledShares = BigDecimal.valueOf(input.controlledShares());
        BigDecimal premiumTotal = input.unitPremium().multiply(controlledShares);
        BigDecimal totalCost = premiumTotal.add(input.commissions());

        BigDecimal commissionPerShare = input.commissions()
                .divide(controlledShares, SCALE, RoundingMode.HALF_UP);

        BigDecimal breakEven = switch (input.type()) {
            case CALL -> input.strike()
                    .add(input.unitPremium())
                    .add(commissionPerShare);
            case PUT -> input.strike()
                    .subtract(input.unitPremium())
                    .subtract(commissionPerShare);
        };

        BigDecimal movementPercent = breakEven
                .subtract(input.currentUnderlyingPrice())
                .divide(input.currentUnderlyingPrice(), SCALE, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        return new OptionSummary(
                money(premiumTotal),
                money(totalCost),
                price(breakEven),
                money(totalCost),
                input.controlledShares(),
                OptionSummary.daysToExpiration(input.expirationDate()),
                percent(movementPercent)
        );
    }

    public List<OptionScenario> buildScenarios(OptionInput input) {
        List<OptionScenario> result = new ArrayList<>();
        BigDecimal current = input.scenarioMin();

        while (current.compareTo(input.scenarioMax()) <= 0) {
            result.add(calculateScenario(input, current));
            current = current.add(input.scenarioStep());
        }

        return result;
    }

    public OptionScenario calculateScenario(OptionInput input, BigDecimal finalUnderlyingPrice) {
        BigDecimal intrinsicPerShare = intrinsicValuePerShare(
                input.type(),
                finalUnderlyingPrice,
                input.strike()
        );

        BigDecimal controlledShares = BigDecimal.valueOf(input.controlledShares());
        BigDecimal optionValueTotal = intrinsicPerShare.multiply(controlledShares);
        BigDecimal premiumTotal = input.unitPremium().multiply(controlledShares);

        BigDecimal grossProfitLoss = optionValueTotal.subtract(premiumTotal);
        BigDecimal netProfitLoss = grossProfitLoss.subtract(input.commissions());

        BigDecimal totalCost = premiumTotal.add(input.commissions());
        BigDecimal roi = totalCost.signum() == 0
                ? BigDecimal.ZERO
                : netProfitLoss.divide(totalCost, SCALE, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

        return new OptionScenario(
                price(finalUnderlyingPrice),
                price(intrinsicPerShare),
                money(optionValueTotal),
                money(grossProfitLoss),
                money(netProfitLoss),
                percent(roi)
        );
    }

    public BigDecimal intrinsicValuePerShare(
            OptionType type,
            BigDecimal finalUnderlyingPrice,
            BigDecimal strike) {

        BigDecimal raw = switch (type) {
            case CALL -> finalUnderlyingPrice.subtract(strike);
            case PUT -> strike.subtract(finalUnderlyingPrice);
        };

        return raw.max(BigDecimal.ZERO);
    }

    private static BigDecimal money(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal price(BigDecimal value) {
        return value.setScale(4, RoundingMode.HALF_UP);
    }

    private static BigDecimal percent(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
