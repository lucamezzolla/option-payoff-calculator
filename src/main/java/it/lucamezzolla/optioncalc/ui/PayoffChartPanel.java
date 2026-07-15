package it.lucamezzolla.optioncalc.ui;

import it.lucamezzolla.optioncalc.model.OptionScenario;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class PayoffChartPanel extends JPanel {

    private List<OptionScenario> scenarios = new ArrayList<>();

    public PayoffChartPanel() {
        setPreferredSize(new Dimension(700, 260));
        setBackground(Color.WHITE);
    }

    public void setScenarios(List<OptionScenario> scenarios) {
        this.scenarios = new ArrayList<>(scenarios);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2 = (Graphics2D) graphics.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int left = 70;
            int right = 25;
            int top = 25;
            int bottom = 45;
            int width = getWidth() - left - right;
            int height = getHeight() - top - bottom;

            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRect(left, top, width, height);

            if (scenarios.size() < 2) {
                g2.setColor(Color.DARK_GRAY);
                g2.drawString("Premi Calcola per generare il grafico", left + 20, top + 30);
                return;
            }

            BigDecimal minX = scenarios.getFirst().underlyingPriceAtExpiration();
            BigDecimal maxX = scenarios.getLast().underlyingPriceAtExpiration();

            BigDecimal minY = scenarios.stream()
                    .map(OptionScenario::netProfitLoss)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            BigDecimal maxY = scenarios.stream()
                    .map(OptionScenario::netProfitLoss)
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            if (minY.compareTo(BigDecimal.ZERO) > 0) {
                minY = BigDecimal.ZERO;
            }
            if (maxY.compareTo(BigDecimal.ZERO) < 0) {
                maxY = BigDecimal.ZERO;
            }
            if (maxY.compareTo(minY) == 0) {
                maxY = maxY.add(BigDecimal.ONE);
            }

            int zeroY = mapY(BigDecimal.ZERO, minY, maxY, top, height);
            g2.setColor(new Color(130, 130, 130));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawLine(left, zeroY, left + width, zeroY);

            Path2D path = new Path2D.Double();
            for (int i = 0; i < scenarios.size(); i++) {
                OptionScenario scenario = scenarios.get(i);
                int x = mapX(scenario.underlyingPriceAtExpiration(), minX, maxX, left, width);
                int y = mapY(scenario.netProfitLoss(), minY, maxY, top, height);
                if (i == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }

            g2.setColor(new Color(30, 90, 180));
            g2.setStroke(new BasicStroke(2.2f));
            g2.draw(path);

            g2.setColor(Color.DARK_GRAY);
            FontMetrics fm = g2.getFontMetrics();
            String minXText = minX.toPlainString() + " €";
            String maxXText = maxX.toPlainString() + " €";
            String minYText = minY.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString() + " €";
            String maxYText = maxY.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString() + " €";

            g2.drawString(minXText, left, top + height + fm.getAscent() + 8);
            g2.drawString(maxXText, left + width - fm.stringWidth(maxXText),
                    top + height + fm.getAscent() + 8);
            g2.drawString(maxYText, 5, top + fm.getAscent());
            g2.drawString(minYText, 5, top + height);

            String title = "Profitto / perdita netta alla scadenza";
            g2.drawString(title, left + (width - fm.stringWidth(title)) / 2, 17);
        } finally {
            g2.dispose();
        }
    }

    private static int mapX(
            BigDecimal value,
            BigDecimal min,
            BigDecimal max,
            int left,
            int width) {

        double ratio = value.subtract(min)
                .divide(max.subtract(min), 12, java.math.RoundingMode.HALF_UP)
                .doubleValue();
        return left + (int) Math.round(ratio * width);
    }

    private static int mapY(
            BigDecimal value,
            BigDecimal min,
            BigDecimal max,
            int top,
            int height) {

        double ratio = value.subtract(min)
                .divide(max.subtract(min), 12, java.math.RoundingMode.HALF_UP)
                .doubleValue();
        return top + height - (int) Math.round(ratio * height);
    }
}
