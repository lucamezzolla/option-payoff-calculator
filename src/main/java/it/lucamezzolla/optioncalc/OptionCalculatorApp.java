package it.lucamezzolla.optioncalc;

import it.lucamezzolla.optioncalc.ui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class OptionCalculatorApp {

    private OptionCalculatorApp() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Swing's default look and feel remains available.
            }

            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
