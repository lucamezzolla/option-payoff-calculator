package it.lucamezzolla.optioncalc.ui;

import it.lucamezzolla.optioncalc.i18n.I18n;
import it.lucamezzolla.optioncalc.model.OptionScenario;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public final class PayoffTableModel extends AbstractTableModel {

    private static final String[] COLUMN_KEYS = {
            "table.underlyingAtExpiration",
            "table.intrinsicPerShare",
            "table.totalOptionValue",
            "table.grossProfitLoss",
            "table.netProfitLoss",
            "table.roi"
    };

    private List<OptionScenario> rows = new ArrayList<>();

    public void setRows(List<OptionScenario> rows) {
        this.rows = new ArrayList<>(rows);
        fireTableDataChanged();
    }

    public List<OptionScenario> getRows() {
        return List.copyOf(rows);
    }

    public void refreshLanguage() {
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_KEYS.length;
    }

    @Override
    public String getColumnName(int column) {
        return I18n.text(COLUMN_KEYS[column]);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        OptionScenario scenario = rows.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> scenario.underlyingPriceAtExpiration();
            case 1 -> scenario.intrinsicValuePerShare();
            case 2 -> scenario.optionValueTotal();
            case 3 -> scenario.grossProfitLoss();
            case 4 -> scenario.netProfitLoss();
            case 5 -> scenario.roiPercent();
            default -> throw new IllegalArgumentException("Invalid column: " + columnIndex);
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return java.math.BigDecimal.class;
    }
}
