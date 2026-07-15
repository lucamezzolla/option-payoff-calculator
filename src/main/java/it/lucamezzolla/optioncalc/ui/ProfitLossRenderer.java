package it.lucamezzolla.optioncalc.ui;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public final class ProfitLossRenderer extends DefaultTableCellRenderer {

    private final DecimalFormat format;
    private final boolean percent;
    private final boolean highlightSign;

    public ProfitLossRenderer(String pattern, boolean percent, boolean highlightSign) {
        this.format = new DecimalFormat(pattern);
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
