# Option Payoff Calculator

Applicazione desktop didattica per simulare il risultato **alla scadenza** di opzioni Call e Put acquistate.

L'obiettivo è rendere immediatamente visibili premio di acquisto, costo, spread, pareggio, perdita massima e profitto/perdita per differenti prezzi finali del sottostante.

## Funzionalità

- simulazione di Call e Put acquistate;
- input separati per **Bid** e **Ask** dell'opzione;
- selezione automatica dell'**Ask** come premio unitario per la simulazione di acquisto;
- calcolo del premio totale e del costo complessivo;
- calcolo dello spread Bid/Ask unitario, totale e percentuale;
- calcolo del punto di pareggio alla scadenza;
- calcolo della perdita massima;
- indicazione delle azioni controllate;
- calcolo del movimento percentuale necessario per il pareggio;
- tabella degli scenari alla scadenza;
- pulsante **Apri grande** per visualizzare tabella e grafico in una finestra separata e ridimensionabile;
- grafico del profitto/perdita netto;
- commissioni incluse nei calcoli;
- pulsante **Pulisci** per iniziare rapidamente un nuovo calcolo;
- supporto sia alla virgola sia al punto come separatore decimale.

## Tecnologie utilizzate

- **Java 21**;
- **Java Swing** per l'interfaccia desktop;
- **Maven** per build e gestione del progetto;
- **JUnit 5** per i test automatici;
- **BigDecimal** per evitare gli errori tipici dei calcoli finanziari con `double`;
- nessuna dipendenza grafica esterna.

## Requisiti

- JDK 21;
- Maven 3.8 o superiore.

Verifica dell'ambiente:

```bash
java -version
javac -version
mvn -version
```

## Compilazione e avvio

```bash
mvn clean package
java -jar target/option-payoff-calculator-1.1.0.jar
```

## Esecuzione dei test

```bash
mvn test
```

## Dati da inserire

- **Tipo**: Call oppure Put.
- **Prezzo attuale sottostante**: prezzo corrente dell'azione o altro sottostante.
- **Strike**: prezzo di esercizio del contratto.
- **Bid opzione**: miglior prezzo al quale il mercato è disposto ad acquistare l'opzione.
- **Ask opzione**: miglior prezzo al quale il mercato è disposto a vendere l'opzione.
- **Contratti**: numero di contratti acquistati.
- **Moltiplicatore**: quantità controllata da ogni contratto, spesso 100.
- **Commissioni totali**: costi complessivi da includere nell'operazione.
- **Scadenza**: data di scadenza dell'opzione.
- **Scenario minimo, massimo e passo**: intervallo dei prezzi finali da mostrare nella tabella.

Per un acquisto il programma usa automaticamente l'**Ask** come premio unitario. Bid e Ask non consentono di ricavare un prezzo teorico: sono già le quotazioni del premio di mercato.

## Formule

### Dati comuni

```text
azioni controllate = contratti × moltiplicatore
premio unitario di acquisto = Ask
premio totale = Ask × azioni controllate
spread unitario = Ask - Bid
spread totale = spread unitario × azioni controllate
```

### Call acquistata

```text
valore intrinseco unitario = max(prezzo finale - strike, 0)
pareggio = strike + Ask + commissioni / azioni controllate
profitto netto = valore totale dell'opzione - premio totale - commissioni
```

### Put acquistata

```text
valore intrinseco unitario = max(strike - prezzo finale, 0)
pareggio = strike - Ask - commissioni / azioni controllate
profitto netto = valore totale dell'opzione - premio totale - commissioni
```

## Vista estesa degli scenari

Il pulsante **Apri grande** apre la tabella e il grafico in una finestra indipendente:

- la finestra principale non viene modificata;
- la nuova finestra è ridimensionabile e massimizzabile;
- il divisore tra tabella e grafico può essere trascinato;
- un doppio clic sulla tabella principale apre la stessa vista estesa.

## Limiti della simulazione

Il programma calcola il payoff **alla scadenza**. Non stima il prezzo teorico dell'opzione prima della scadenza.

Prima della scadenza il premio dipende anche da:

- volatilità implicita;
- tempo residuo e Theta;
- Delta e Gamma;
- Vega;
- tassi e dividendi;
- liquidità del contratto.

Il progetto ha finalità didattiche e non costituisce consulenza finanziaria.

## Struttura del progetto

```text
src/main/java/it/lucamezzolla/optioncalc
├── OptionCalculatorApp.java
├── model
│   ├── OptionInput.java
│   ├── OptionScenario.java
│   ├── OptionSummary.java
│   └── OptionType.java
├── service
│   └── OptionCalculator.java
└── ui
    ├── MainFrame.java
    ├── PayoffChartPanel.java
    ├── PayoffTableModel.java
    └── ProfitLossRenderer.java
```

La logica finanziaria è separata dall'interfaccia Swing, così può essere testata e riutilizzata senza dipendere dalla UI.

## Aggiornamento del repository e tag `v1.1.0`

```bash
git add .
git commit -m "Add automatic Ask premium and expanded scenarios view"
git push

git tag -a v1.1.0 -m "Option Payoff Calculator v1.1.0"
git push origin v1.1.0
```

Con GitHub CLI:

```bash
mvn clean package
gh release create v1.1.0 \
  target/option-payoff-calculator-1.1.0.jar \
  --title "Option Payoff Calculator v1.1.0" \
  --notes-file RELEASE_NOTES.md
```
