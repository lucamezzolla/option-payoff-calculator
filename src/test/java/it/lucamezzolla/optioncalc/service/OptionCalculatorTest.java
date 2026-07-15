package it.lucamezzolla.optioncalc.service;

import it.lucamezzolla.optioncalc.model.OptionInput;
import it.lucamezzolla.optioncalc.model.OptionScenario;
import it.lucamezzolla.optioncalc.model.OptionSummary;
import it.lucamezzolla.optioncalc.model.OptionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptionCalculatorTest {

    private final OptionCalculator calculator = new OptionCalculator();

    @Test
    void callExampleShouldProduceElevenEurosProfit() {
        OptionInput input = callInput();

        OptionScenario scenario = calculator.calculateScenario(
                input,
                new BigDecimal("15.20")
        );

        assertEquals(new BigDecimal("45.00"), scenario.optionValueTotal());
        assertEquals(new BigDecimal("11.00"), scenario.netProfitLoss());
    }

    @Test
    void callBreakEvenShouldUseAskAsPurchasePremium() {
        OptionSummary summary = calculator.summarize(callInput());

        assertEquals(new BigDecimal("0.3400"), summary.unitPremium());
        assertEquals(new BigDecimal("15.0900"), summary.breakEven());
        assertEquals(new BigDecimal("34.00"), summary.maximumLoss());
    }

    @Test
    void spreadShouldBeCalculatedFromBidAndAsk() {
        OptionSummary summary = calculator.summarize(callInput());

        assertEquals(new BigDecimal("0.0400"), summary.spreadPerShare());
        assertEquals(new BigDecimal("4.00"), summary.spreadTotal());
        assertEquals(new BigDecimal("12.50"), summary.spreadPercent());
    }

    @Test
    void putProfitShouldIncreaseWhenUnderlyingFalls() {
        OptionInput input = new OptionInput(
                OptionType.PUT,
                new BigDecimal("14.84"),
                new BigDecimal("15.00"),
                new BigDecimal("0.25"),
                new BigDecimal("0.30"),
                1,
                100,
                BigDecimal.ZERO,
                LocalDate.now().plusDays(30),
                new BigDecimal("13.00"),
                new BigDecimal("16.00"),
                new BigDecimal("0.10")
        );

        OptionScenario scenario = calculator.calculateScenario(
                input,
                new BigDecimal("14.20")
        );

        assertEquals(new BigDecimal("80.00"), scenario.optionValueTotal());
        assertEquals(new BigDecimal("50.00"), scenario.netProfitLoss());
    }

    private static OptionInput callInput() {
        return new OptionInput(
                OptionType.CALL,
                new BigDecimal("14.84"),
                new BigDecimal("14.75"),
                new BigDecimal("0.30"),
                new BigDecimal("0.34"),
                1,
                100,
                BigDecimal.ZERO,
                LocalDate.now().plusDays(30),
                new BigDecimal("14.00"),
                new BigDecimal("16.00"),
                new BigDecimal("0.10")
        );
    }
}
