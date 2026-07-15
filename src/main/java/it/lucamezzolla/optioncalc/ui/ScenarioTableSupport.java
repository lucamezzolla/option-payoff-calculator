package it.lucamezzolla.optioncalc.ui;

import javax.swing.JTable;

public final class ScenarioTableSupport {

    private ScenarioTableSupport() {
    }

    public static void configure(JTable table) {
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(24);
        table.getTableHeader().setReorderingAllowed(false);

        table.getColumnModel().getColumn(0)
                .setCellRenderer(new ProfitLossRenderer(4, false, false));
        table.getColumnModel().getColumn(1)
                .setCellRenderer(new ProfitLossRenderer(4, false, false));
        table.getColumnModel().getColumn(2)
                .setCellRenderer(new ProfitLossRenderer(2, false, false));
        table.getColumnModel().getColumn(3)
                .setCellRenderer(new ProfitLossRenderer(2, false, true));
        table.getColumnModel().getColumn(4)
                .setCellRenderer(new ProfitLossRenderer(2, false, true));
        table.getColumnModel().getColumn(5)
                .setCellRenderer(new ProfitLossRenderer(2, true, true));
    }
}
