# Option Payoff Calculator v1.2.0

## Highlights

- English is now the default application language.
- Added runtime language switching for English, Italian, Spanish, Portuguese and French.
- Localized the complete interface, including validation messages, table headers, chart text and scenario windows.
- Removed the embedded scenario table and chart from the main window.
- Added a contextual **Scenarios ↑** button next to **Clear**.
- The scenario button is shown only after a successful calculation.
- Pressing **Clear** hides the scenario button, clears calculated scenarios and closes open scenario windows.
- Scenario results now open exclusively in a separate resizable and maximizable window.
- Number and currency formatting follow the selected language.

## Technical changes

- Added Java `ResourceBundle`-based internationalization.
- Added `AppLanguage` and `I18n` components.
- Added localized validation through `ValidationException`.
- Added `ScenariosDialog` and centralized table configuration.
- Updated Maven project version to `1.2.0`.
