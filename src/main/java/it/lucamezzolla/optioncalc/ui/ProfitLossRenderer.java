package it.lucamezzolla.optioncalc.ui;

import it.lucamezzolla.optioncalc.i18n.I18n;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.text.NumberFormat;

public final class ProfitLossRenderer extends DefaultTableCellRenderer {

    private final int fractionDigits;
    private final boolean percent;
    private final boolean highlightSign;

    public ProfitLossRenderer(int fractionDigits, boolean percent, boolean highlightSign) {
        this.fractionDigits = fractionDigits;
        this.percent = percent;
        this.highlightSign = highlightSign;
        setHorizontalAlignment(SwingConstants.RIGHT);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        Component component = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        if (value instanceof BigDecimal decimal) {
            NumberFormat format = NumberFormat.getNumberInstance(I18n.locale());
            format.setMinimumFractionDigits(fractionDigits);
            format.setMaximumFractionDigits(fractionDigits);
            setText(format.format(decimal) + (percent ? " %" : " €"));

            if (!isSelected && highlightSign) {
                if (decimal.signum() > 0) {
                    component.setForeground(new Color(0, 128, 0));
                } else if (decimal.signum() < 0) {
                    component.setForeground(new Color(180, 0, 0));
                } else {
                    component.setForeground(Color.DARK_GRAY);
                }
            } else if (!isSelected) {
                component.setForeground(table.getForeground());
            }
        }

        return component;
    }
}
