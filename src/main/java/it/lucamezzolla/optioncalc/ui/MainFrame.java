package it.lucamezzolla.optioncalc.ui;

import it.lucamezzolla.optioncalc.model.OptionInput;
import it.lucamezzolla.optioncalc.model.OptionScenario;
import it.lucamezzolla.optioncalc.model.OptionSummary;
import it.lucamezzolla.optioncalc.model.OptionType;
import it.lucamezzolla.optioncalc.service.OptionCalculator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public final class MainFrame extends JFrame {

    private final OptionCalculator calculator = new OptionCalculator();
    private final PayoffTableModel tableModel = new PayoffTableModel();
    private final PayoffChartPanel chartPanel = new PayoffChartPanel();

    private final JComboBox<OptionType> typeCombo = new JComboBox<>(OptionType.values());
    private final JTextField currentPriceField = new JTextField("", 10);
    private final JTextField strikeField = new JTextField("", 10);
    private final JTextField bidField = new JTextField("", 10);
    private final JTextField askField = new JTextField("", 10);
    private final JTextField contractsField = new JTextField("1", 10);
    private final JTextField multiplierField = new JTextField("100", 10);
    private final JTextField commissionsField = new JTextField("0", 10);
    private final JSpinner expirationSpinner = new JSpinner(
            new SpinnerDateModel(Date.from(
                    LocalDate.now().plusDays(30)
                            .atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    null, null, java.util.Calendar.DAY_OF_MONTH));
    private final JTextField minField = new JTextField("", 10);
    private final JTextField maxField = new JTextField("", 10);
    private final JTextField stepField = new JTextField("", 10);

    private final JLabel unitPremiumValue = valueLabel();
    private final JLabel premiumTotalValue = valueLabel();
    private final JLabel totalCostValue = valueLabel();
    private final JLabel spreadPerShareValue = valueLabel();
    private final JLabel spreadTotalValue = valueLabel();
    private final JLabel spreadPercentValue = valueLabel();
    private final JLabel breakEvenValue = valueLabel();
    private final JLabel maxLossValue = valueLabel();
    private final JLabel controlledSharesValue = valueLabel();
    private final JLabel daysValue = valueLabel();
    private final JLabel movementValue = valueLabel();

    public MainFrame() {
        super("Option Payoff Calculator");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1120, 800));
        setLocationByPlatform(true);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createMainContent(), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        currentPriceField.requestFocusInWindow();
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Calcolatore payoff opzioni");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));

        JLabel subtitle = new JLabel(
                "Simulazione a scadenza per Call e Put acquistate. Inserisci Bid e Ask: per l'acquisto il premio usato è automaticamente l'Ask.");
        subtitle.setBorder(new EmptyBorder(4, 0, 0, 0));

        panel.add(title, BorderLayout.NORTH);
        panel.add(subtitle, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMainContent() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel upper = new JPanel(new BorderLayout(10, 10));
        upper.add(createInputPanel(), BorderLayout.WEST);
        upper.add(createSummaryPanel(), BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.add(createTablePanel(), BorderLayout.CENTER);
        center.add(chartPanel, BorderLayout.SOUTH);

        panel.add(upper, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dati del contratto"));

        expirationSpinner.setEditor(new JSpinner.DateEditor(expirationSpinner, "dd/MM/yyyy"));

        int row = 0;
        addField(panel, row++, "Tipo", typeCombo);
        addField(panel, row++, "Prezzo attuale sottostante", currentPriceField);
        addField(panel, row++, "Strike", strikeField);
        addField(panel, row++, "Bid opzione", bidField);
        addField(panel, row++, "Ask opzione", askField);
        addField(panel, row++, "Contratti", contractsField);
        addField(panel, row++, "Moltiplicatore", multiplierField);
        addField(panel, row++, "Commissioni totali", commissionsField);
        addField(panel, row++, "Scadenza", expirationSpinner);
        addField(panel, row++, "Scenario minimo", minField);
        addField(panel, row++, "Scenario massimo", maxField);
        addField(panel, row++, "Passo", stepField);

        JButton calculateButton = new JButton("Calcola");
        calculateButton.addActionListener(event -> calculate());

        JButton clearButton = new JButton("Pulisci");
        clearButton.addActionListener(event -> clearForm());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.add(clearButton);
        buttons.add(calculateButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 4, 4, 4);
        panel.add(buttons, gbc);

        return panel;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Riepilogo"));

        addSummary(panel, "Premio unitario usato (Ask)", unitPremiumValue);
        addSummary(panel, "Premio totale", premiumTotalValue);
        addSummary(panel, "Costo totale", totalCostValue);
        addSummary(panel, "Spread unitario Bid/Ask", spreadPerShareValue);
        addSummary(panel, "Spread totale", spreadTotalValue);
        addSummary(panel, "Spread percentuale", spreadPercentValue);
        addSummary(panel, "Pareggio a scadenza", breakEvenValue);
        addSummary(panel, "Perdita massima", maxLossValue);
        addSummary(panel, "Azioni controllate", controlledSharesValue);
        addSummary(panel, "Giorni alla scadenza", daysValue);
        addSummary(panel, "Movimento fino al pareggio", movementValue);

        JLabel note = new JLabel(
                "<html><b>Nota:</b> Bid e Ask sono già quotazioni del premio di mercato. Il programma usa l'Ask per simulare l'acquisto; non stima un prezzo teorico.</html>");
        note.setVerticalAlignment(SwingConstants.TOP);
        panel.add(note);
        panel.add(new JLabel());

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Scenari alla scadenza"));

        JTable table = createScenarioTable();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    openScenariosWindow();
                }
            }
        });

        JButton expandButton = new JButton("Apri grande ↗");
        expandButton.setToolTipText("Apre tabella e grafico in una finestra separata e ridimensionabile");
        expandButton.addActionListener(event -> openScenariosWindow());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        toolbar.add(expandButton);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(950, 190));

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JTable createScenarioTable() {
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(24);
        table.getTableHeader().setReorderingAllowed(false);

        ProfitLossRenderer priceRenderer =
                new ProfitLossRenderer("#,##0.0000", false, false);
        ProfitLossRenderer moneyRenderer =
                new ProfitLossRenderer("#,##0.00", false, false);
        ProfitLossRenderer profitRenderer =
                new ProfitLossRenderer("#,##0.00", false, true);
        ProfitLossRenderer percentRenderer =
                new ProfitLossRenderer("#,##0.00", true, true);

        table.getColumnModel().getColumn(0).setCellRenderer(priceRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(priceRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(moneyRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(profitRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(profitRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(percentRenderer);
        return table;
    }

    private void openScenariosWindow() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Esegui prima un calcolo per generare gli scenari.",
                    "Scenari non disponibili",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        JDialog dialog = new JDialog(this, "Scenari alla scadenza — vista estesa", false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JTable expandedTable = createScenarioTable();
        JScrollPane expandedScrollPane = new JScrollPane(expandedTable);

        PayoffChartPanel expandedChart = new PayoffChartPanel();
        expandedChart.setPreferredSize(new Dimension(1100, 320));
        expandedChart.setScenarios(tableModel.getRows());

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                expandedScrollPane,
                expandedChart
        );
        splitPane.setResizeWeight(0.62);
        splitPane.setOneTouchExpandable(true);

        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.setBorder(new EmptyBorder(10, 10, 10, 10));
        content.add(splitPane, BorderLayout.CENTER);

        dialog.setContentPane(content);
        dialog.setSize(1250, 760);
        dialog.setMinimumSize(new Dimension(850, 500));
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void calculate() {
        try {
            OptionInput input = readInput();
            OptionSummary summary = calculator.summarize(input);
            List<OptionScenario> scenarios = calculator.buildScenarios(input);

            unitPremiumValue.setText(formatPrice(summary.unitPremium()));
            premiumTotalValue.setText(formatMoney(summary.premiumTotal()));
            totalCostValue.setText(formatMoney(summary.totalCost()));
            spreadPerShareValue.setText(formatPrice(summary.spreadPerShare()));
            spreadTotalValue.setText(formatMoney(summary.spreadTotal()));
            spreadPercentValue.setText(formatPercent(summary.spreadPercent()));
            breakEvenValue.setText(formatPrice(summary.breakEven()));
            maxLossValue.setText(formatMoney(summary.maximumLoss()));
            controlledSharesValue.setText(Integer.toString(summary.controlledShares()));
            daysValue.setText(Long.toString(summary.daysToExpiration()));
            movementValue.setText(formatPercent(summary.movementToBreakEvenPercent()));

            tableModel.setRows(scenarios);
            chartPanel.setScenarios(scenarios);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Dati non validi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private OptionInput readInput() {
        Date selectedDate = (Date) expirationSpinner.getValue();
        LocalDate expiration = selectedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        return new OptionInput(
                (OptionType) typeCombo.getSelectedItem(),
                decimal(currentPriceField.getText()),
                decimal(strikeField.getText()),
                decimal(bidField.getText()),
                decimal(askField.getText()),
                integer(contractsField.getText(), "Contratti"),
                integer(multiplierField.getText(), "Moltiplicatore"),
                decimal(commissionsField.getText()),
                expiration,
                decimal(minField.getText()),
                decimal(maxField.getText()),
                decimal(stepField.getText())
        );
    }

    private void clearForm() {
        typeCombo.setSelectedItem(OptionType.CALL);
        currentPriceField.setText("");
        strikeField.setText("");
        bidField.setText("");
        askField.setText("");
        contractsField.setText("1");
        multiplierField.setText("100");
        commissionsField.setText("0");
        expirationSpinner.setValue(Date.from(
                LocalDate.now().plusDays(30)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        minField.setText("");
        maxField.setText("");
        stepField.setText("");

        unitPremiumValue.setText("—");
        premiumTotalValue.setText("—");
        totalCostValue.setText("—");
        spreadPerShareValue.setText("—");
        spreadTotalValue.setText("—");
        spreadPercentValue.setText("—");
        breakEvenValue.setText("—");
        maxLossValue.setText("—");
        controlledSharesValue.setText("—");
        daysValue.setText("—");
        movementValue.setText("—");

        tableModel.setRows(List.of());
        chartPanel.setScenarios(List.of());
        currentPriceField.requestFocusInWindow();
    }

    private static void addField(
            JPanel panel,
            int row,
            String label,
            java.awt.Component component) {

        GridBagConstraints left = new GridBagConstraints();
        left.gridx = 0;
        left.gridy = row;
        left.anchor = GridBagConstraints.WEST;
        left.insets = new Insets(4, 4, 4, 10);

        GridBagConstraints right = new GridBagConstraints();
        right.gridx = 1;
        right.gridy = row;
        right.weightx = 1;
        right.fill = GridBagConstraints.HORIZONTAL;
        right.insets = new Insets(4, 4, 4, 4);

        panel.add(new JLabel(label), left);
        panel.add(component, right);
    }

    private static void addSummary(JPanel panel, String label, JLabel value) {
        JLabel title = new JLabel(label);
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        panel.add(title);
        panel.add(value);
    }

    private static JLabel valueLabel() {
        JLabel label = new JLabel("—");
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    private static BigDecimal decimal(String text) {
        try {
            String normalized = text.trim().replace(" ", "");
            if (normalized.isEmpty()) {
                throw new NumberFormatException();
            }
            if (normalized.contains(",")) {
                normalized = normalized.replace(".", "").replace(",", ".");
            }
            return new BigDecimal(normalized);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Numero non valido o mancante: " + text);
        }
    }

    private static int integer(String text, String fieldName) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " deve essere un numero intero");
        }
    }

    private static String formatMoney(BigDecimal value) {
        return new DecimalFormat("#,##0.00 €").format(value);
    }

    private static String formatPrice(BigDecimal value) {
        return new DecimalFormat("#,##0.0000 €").format(value);
    }

    private static String formatPercent(BigDecimal value) {
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        String formatted = formatter.format(value.abs());

        if (value.signum() > 0) {
            return "+" + formatted + " %";
        }
        if (value.signum() < 0) {
            return "-" + formatted + " %";
        }
        return formatted + " %";
    }
}
