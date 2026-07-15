package it.lucamezzolla.optioncalc.ui;

import it.lucamezzolla.optioncalc.model.OptionScenario;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public final class PayoffTableModel extends AbstractTableModel {

    private static final String[] COLUMNS = {
            "Prezzo sottostante a scadenza",
            "Valore intrinseco per azione",
            "Valore opzione totale",
            "P/L lordo",
            "P/L netto",
            "ROI"
    };

    private List<OptionScenario> rows = new ArrayList<>();

    public void setRows(List<OptionScenario> rows) {
        this.rows = new ArrayList<>(rows);
        fireTableDataChanged();
    }

    public OptionScenario getScenario(int rowIndex) {
        return rows.get(rowIndex);
    }

    public List<OptionScenario> getRows() {
        return List.copyOf(rows);
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
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
            default -> throw new IllegalArgumentException("Colonna non valida: " + columnIndex);
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return java.math.BigDecimal.class;
    }
}
