# Option Payoff Calculator

Applicazione desktop didattica per simulare il risultato **alla scadenza** di opzioni Call e Put acquistate.

L'obiettivo è rendere immediatamente visibili costo, pareggio, perdita massima e profitto/perdita per differenti prezzi finali del sottostante.

## Funzionalità

- simulazione di Call e Put acquistate;
- calcolo del premio totale e del costo complessivo;
- calcolo del punto di pareggio alla scadenza;
- calcolo della perdita massima;
- indicazione delle azioni controllate;
- calcolo del movimento percentuale necessario per il pareggio;
- tabella degli scenari alla scadenza;
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
- Maven 3.8 o superiore, solo per la build Maven.

Verifica dell'ambiente:

```bash
java -version
javac -version
mvn -version
```

## Avvio rapido con Maven

```bash
mvn clean package
java -jar target/option-payoff-calculator-1.0.0.jar
```

## Esecuzione dei test

```bash
mvn test
```

## Dati da inserire

- **Tipo**: Call oppure Put.
- **Prezzo attuale sottostante**: prezzo corrente dell'azione o altro sottostante.
- **Strike**: prezzo di esercizio del contratto.
- **Premio unitario**: normalmente l'Ask per una simulazione di acquisto, oppure il prezzo effettivamente eseguito.
- **Contratti**: numero di contratti acquistati.
- **Moltiplicatore**: quantità controllata da ogni contratto, spesso 100.
- **Commissioni totali**: costi complessivi da includere nell'operazione.
- **Scadenza**: data di scadenza dell'opzione.
- **Scenario minimo, massimo e passo**: intervallo dei prezzi finali da mostrare nella tabella.

## Formule

### Call acquistata

```text
valore intrinseco unitario = max(prezzo finale - strike, 0)
premio totale = premio unitario × contratti × moltiplicatore
pareggio = strike + premio unitario + commissioni / azioni controllate
profitto netto = valore totale dell'opzione - premio totale - commissioni
```

### Put acquistata

```text
valore intrinseco unitario = max(strike - prezzo finale, 0)
premio totale = premio unitario × contratti × moltiplicatore
pareggio = strike - premio unitario - commissioni / azioni controllate
profitto netto = valore totale dell'opzione - premio totale - commissioni
```

## Limiti della simulazione

Il programma calcola il payoff **alla scadenza**. Non stima ancora il prezzo dell'opzione prima della scadenza.

Prima della scadenza il premio dipende anche da:

- volatilità implicita;
- tempo residuo e Theta;
- Delta e Gamma;
- Vega;
- tassi e dividendi;
- Bid, Ask e liquidità del contratto.

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

## Pubblicazione iniziale su GitHub

Dopo aver creato un repository vuoto su GitHub:

```bash
git init
git add .
git commit -m "Initial release v1.0.0"
git branch -M main
git remote add origin https://github.com/USERNAME/option-payoff-calculator.git
git push -u origin main
```

## Tag e release `v1.0.0`

Creazione e pubblicazione del tag:

```bash
git tag -a v1.0.0 -m "Option Payoff Calculator v1.0.0"
git push origin v1.0.0
```

Con GitHub CLI è possibile creare anche la release allegando il JAR:

```bash
mvn clean package
gh release create v1.0.0 \
  target/option-payoff-calculator-1.0.0.jar \
  --title "Option Payoff Calculator v1.0.0" \
  --notes-file RELEASE_NOTES.md
```
