package it.lucamezzolla.optioncalc.ui;

import it.lucamezzolla.optioncalc.i18n.AppLanguage;
import it.lucamezzolla.optioncalc.i18n.I18n;
import it.lucamezzolla.optioncalc.model.OptionInput;
import it.lucamezzolla.optioncalc.model.OptionScenario;
import it.lucamezzolla.optioncalc.model.OptionSummary;
import it.lucamezzolla.optioncalc.model.OptionType;
import it.lucamezzolla.optioncalc.service.OptionCalculator;
import it.lucamezzolla.optioncalc.validation.ValidationException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class MainFrame extends JFrame {

    private final OptionCalculator calculator = new OptionCalculator();
    private final PayoffTableModel tableModel = new PayoffTableModel();
    private final List<ScenariosDialog> scenarioDialogs = new ArrayList<>();
    private OptionSummary lastSummary;

    private final JComboBox<OptionType> typeCombo = new JComboBox<>(OptionType.values());
    private final JComboBox<AppLanguage> languageCombo = new JComboBox<>(AppLanguage.values());
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

    private final JLabel headerTitle = new JLabel();
    private final JLabel headerSubtitle = new JLabel();
    private final JLabel languageLabel = new JLabel();
    private final JButton calculateButton = new JButton();
    private final JButton clearButton = new JButton();
    private final JButton scenariosButton = new JButton();

    private final JPanel inputPanel = new JPanel(new GridBagLayout());
    private final JPanel summaryPanel = new JPanel(new GridLayout(0, 2, 8, 8));
    private final TitledBorder inputBorder = BorderFactory.createTitledBorder("");
    private final TitledBorder summaryBorder = BorderFactory.createTitledBorder("");

    private final Map<String, JLabel> fieldLabels = new LinkedHashMap<>();
    private final Map<String, JLabel> summaryLabels = new LinkedHashMap<>();

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
    private final JLabel noteLabel = new JLabel();

    public MainFrame() {
        super();

        I18n.setLanguage(AppLanguage.ENGLISH);
        languageCombo.setSelectedItem(AppLanguage.ENGLISH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1080, 610));
        setLocationByPlatform(true);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createMainContent(), BorderLayout.CENTER);

        languageCombo.addActionListener(event -> changeLanguage());

        updateTexts();
        pack();
        setLocationRelativeTo(null);
        currentPriceField.requestFocusInWindow();
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));

        headerTitle.setFont(headerTitle.getFont().deriveFont(Font.BOLD, 22f));
        headerSubtitle.setBorder(new EmptyBorder(4, 0, 0, 0));

        JPanel titles = new JPanel(new BorderLayout());
        titles.add(headerTitle, BorderLayout.NORTH);
        titles.add(headerSubtitle, BorderLayout.CENTER);

        JPanel languagePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        languagePanel.add(languageLabel);
        languagePanel.add(languageCombo);

        panel.add(titles, BorderLayout.CENTER);
        panel.add(languagePanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createMainContent() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(createInputPanel(), BorderLayout.WEST);
        panel.add(createSummaryPanel(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createInputPanel() {
        inputPanel.setBorder(inputBorder);
        updateDateEditor();

        int row = 0;
        addField(inputPanel, row++, "field.type", typeCombo);
        addField(inputPanel, row++, "field.currentUnderlyingPrice", currentPriceField);
        addField(inputPanel, row++, "field.strike", strikeField);
        addField(inputPanel, row++, "field.bid", bidField);
        addField(inputPanel, row++, "field.ask", askField);
        addField(inputPanel, row++, "field.contracts", contractsField);
        addField(inputPanel, row++, "field.multiplier", multiplierField);
        addField(inputPanel, row++, "field.commissions", commissionsField);
        addField(inputPanel, row++, "field.expiration", expirationSpinner);
        addField(inputPanel, row++, "field.scenarioMin", minField);
        addField(inputPanel, row++, "field.scenarioMax", maxField);
        addField(inputPanel, row++, "field.scenarioStep", stepField);

        calculateButton.addActionListener(event -> calculate());
        clearButton.addActionListener(event -> clearForm());
        scenariosButton.addActionListener(event -> openScenariosWindow());
        scenariosButton.setVisible(false);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.add(clearButton);
        buttons.add(scenariosButton);
        buttons.add(calculateButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 4, 4, 4);
        inputPanel.add(buttons, gbc);

        return inputPanel;
    }

    private JPanel createSummaryPanel() {
        summaryPanel.setBorder(summaryBorder);

        addSummary(summaryPanel, "summary.unitPremium", unitPremiumValue);
        addSummary(summaryPanel, "summary.premiumTotal", premiumTotalValue);
        addSummary(summaryPanel, "summary.totalCost", totalCostValue);
        addSummary(summaryPanel, "summary.spreadPerShare", spreadPerShareValue);
        addSummary(summaryPanel, "summary.spreadTotal", spreadTotalValue);
        addSummary(summaryPanel, "summary.spreadPercent", spreadPercentValue);
        addSummary(summaryPanel, "summary.breakEven", breakEvenValue);
        addSummary(summaryPanel, "summary.maximumLoss", maxLossValue);
        addSummary(summaryPanel, "summary.controlledShares", controlledSharesValue);
        addSummary(summaryPanel, "summary.daysToExpiration", daysValue);
        addSummary(summaryPanel, "summary.movementToBreakEven", movementValue);

        noteLabel.setVerticalAlignment(SwingConstants.TOP);
        summaryPanel.add(noteLabel);
        summaryPanel.add(new JLabel());

        return summaryPanel;
    }

    private void openScenariosWindow() {
        if (tableModel.getRowCount() == 0) {
            return;
        }

        ScenariosDialog dialog = new ScenariosDialog(this, tableModel);
        scenarioDialogs.add(dialog);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent event) {
                scenarioDialogs.remove(dialog);
            }
        });
        dialog.setVisible(true);
    }

    private void calculate() {
        try {
            OptionInput input = readInput();
            OptionSummary summary = calculator.summarize(input);
            List<OptionScenario> scenarios = calculator.buildScenarios(input);

            lastSummary = summary;
            renderSummary(summary);

            tableModel.setRows(scenarios);
            scenarioDialogs.forEach(dialog -> dialog.setScenarios(scenarios));
            scenariosButton.setVisible(true);
            inputPanel.revalidate();
            inputPanel.repaint();
        } catch (ValidationException ex) {
            showValidationError(I18n.text(ex.messageKey(), ex.arguments()));
        } catch (RuntimeException ex) {
            showValidationError(ex.getMessage() == null
                    ? I18n.text("validation.generic")
                    : ex.getMessage());
        }
    }

    private OptionInput readInput() {
        Date selectedDate = (Date) expirationSpinner.getValue();
        LocalDate expiration = selectedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        return new OptionInput(
                (OptionType) typeCombo.getSelectedItem(),
                decimal(currentPriceField.getText(), "field.currentUnderlyingPrice"),
                decimal(strikeField.getText(), "field.strike"),
                decimal(bidField.getText(), "field.bid"),
                decimal(askField.getText(), "field.ask"),
                integer(contractsField.getText(), "field.contracts"),
                integer(multiplierField.getText(), "field.multiplier"),
                decimal(commissionsField.getText(), "field.commissions"),
                expiration,
                decimal(minField.getText(), "field.scenarioMin"),
                decimal(maxField.getText(), "field.scenarioMax"),
                decimal(stepField.getText(), "field.scenarioStep")
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

        lastSummary = null;
        resetSummaryValues();
        tableModel.setRows(List.of());
        scenariosButton.setVisible(false);
        closeScenarioDialogs();
        inputPanel.revalidate();
        inputPanel.repaint();
        currentPriceField.requestFocusInWindow();
    }

    private void changeLanguage() {
        AppLanguage selected = (AppLanguage) languageCombo.getSelectedItem();
        I18n.setLanguage(selected);
        updateTexts();
        tableModel.refreshLanguage();
        scenarioDialogs.forEach(ScenariosDialog::refreshLanguage);
    }

    private void updateTexts() {
        setTitle(I18n.text("app.title"));
        headerTitle.setText(I18n.text("header.title"));
        headerSubtitle.setText(I18n.text("header.subtitle"));
        languageLabel.setText(I18n.text("language.label"));

        inputBorder.setTitle(I18n.text("panel.contractData"));
        summaryBorder.setTitle(I18n.text("panel.summary"));

        fieldLabels.forEach((key, label) -> label.setText(I18n.text(key)));
        summaryLabels.forEach((key, label) -> label.setText(I18n.text(key)));

        clearButton.setText(I18n.text("button.clear"));
        scenariosButton.setText(I18n.text("button.scenarios"));
        scenariosButton.setToolTipText(I18n.text("button.scenarios.tooltip"));
        calculateButton.setText(I18n.text("button.calculate"));
        noteLabel.setText(I18n.text("summary.note"));

        UIManager.put("OptionPane.okButtonText", I18n.text("common.ok"));
        updateDateEditor();
        if (lastSummary != null) {
            renderSummary(lastSummary);
        }

        inputPanel.revalidate();
        inputPanel.repaint();
        summaryPanel.revalidate();
        summaryPanel.repaint();
    }

    private void updateDateEditor() {
        String pattern = I18n.language() == AppLanguage.ENGLISH
                ? "MM/dd/yyyy"
                : "dd/MM/yyyy";
        expirationSpinner.setEditor(new JSpinner.DateEditor(expirationSpinner, pattern));
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                I18n.text("message.invalidData.title"),
                JOptionPane.ERROR_MESSAGE
        );
    }


    private void renderSummary(OptionSummary summary) {
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
        movementValue.setText(formatSignedPercent(summary.movementToBreakEvenPercent()));
    }

    private void resetSummaryValues() {
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
    }

    private void closeScenarioDialogs() {
        List.copyOf(scenarioDialogs).forEach(ScenariosDialog::dispose);
        scenarioDialogs.clear();
    }

    private void addField(
            JPanel panel,
            int row,
            String labelKey,
            java.awt.Component component) {

        JLabel label = new JLabel();
        fieldLabels.put(labelKey, label);

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

        panel.add(label, left);
        panel.add(component, right);
    }

    private void addSummary(JPanel panel, String labelKey, JLabel value) {
        JLabel title = new JLabel();
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        summaryLabels.put(labelKey, title);
        panel.add(title);
        panel.add(value);
    }

    private static JLabel valueLabel() {
        JLabel label = new JLabel("—");
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    private static BigDecimal decimal(String text, String fieldKey) {
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
            throw new ValidationException(
                    "validation.number",
                    I18n.text(fieldKey)
            );
        }
    }

    private static int integer(String text, String fieldKey) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException ex) {
            throw new ValidationException(
                    "validation.integer",
                    I18n.text(fieldKey)
            );
        }
    }

    private static String formatMoney(BigDecimal value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(I18n.locale());
        format.setCurrency(java.util.Currency.getInstance("EUR"));
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return format.format(value);
    }

    private static String formatPrice(BigDecimal value) {
        NumberFormat format = NumberFormat.getNumberInstance(I18n.locale());
        format.setMinimumFractionDigits(4);
        format.setMaximumFractionDigits(4);
        return format.format(value) + " €";
    }

    private static String formatPercent(BigDecimal value) {
        NumberFormat format = NumberFormat.getNumberInstance(I18n.locale());
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return format.format(value) + " %";
    }

    private static String formatSignedPercent(BigDecimal value) {
        String prefix = value.signum() > 0 ? "+" : "";
        return prefix + formatPercent(value);
    }
}
