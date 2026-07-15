package it.lucamezzolla.optioncalc.ui;

import it.lucamezzolla.optioncalc.i18n.I18n;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;

public final class ScenariosDialog extends JDialog {

    private final PayoffTableModel tableModel;
    private final JTable table;
    private final PayoffChartPanel chartPanel;
    private final JPanel scenariosPanel;
    private final TitledBorder scenariosBorder;

    public ScenariosDialog(JFrame owner, PayoffTableModel tableModel) {
        super(owner, false);
        this.tableModel = tableModel;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        table = new JTable(tableModel);
        ScenarioTableSupport.configure(table);

        JScrollPane scrollPane = new JScrollPane(table);

        scenariosBorder = BorderFactory.createTitledBorder("");
        scenariosPanel = new JPanel(new BorderLayout());
        scenariosPanel.setBorder(scenariosBorder);
        scenariosPanel.add(scrollPane, BorderLayout.CENTER);

        chartPanel = new PayoffChartPanel();
        chartPanel.setPreferredSize(new Dimension(1100, 330));
        chartPanel.setScenarios(tableModel.getRows());

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                scenariosPanel,
                chartPanel
        );
        splitPane.setResizeWeight(0.62);
        splitPane.setOneTouchExpandable(true);

        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.setBorder(new EmptyBorder(10, 10, 10, 10));
        content.add(splitPane, BorderLayout.CENTER);

        setContentPane(content);
        setSize(1250, 760);
        setMinimumSize(new Dimension(850, 500));
        setLocationRelativeTo(owner);
        refreshLanguage();
    }

    public void setScenarios(java.util.List<it.lucamezzolla.optioncalc.model.OptionScenario> scenarios) {
        chartPanel.setScenarios(scenarios);
    }

    public void refreshLanguage() {
        setTitle(I18n.text("scenario.dialog.title"));
        scenariosBorder.setTitle(I18n.text("scenario.panel.title"));
        ScenarioTableSupport.configure(table);
        chartPanel.refreshLanguage();
        scenariosPanel.revalidate();
        scenariosPanel.repaint();
    }
}
