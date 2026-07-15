# Option Payoff Calculator v1.1.0

Aggiornamento dell'interfaccia e dei calcoli di mercato per Call e Put acquistate.

## Novità

- aggiunti i campi separati **Bid opzione** e **Ask opzione**;
- l'Ask viene usato automaticamente come premio unitario di acquisto;
- il pareggio viene calcolato automaticamente usando l'Ask e le commissioni;
- aggiunto il calcolo dello spread Bid/Ask unitario, totale e percentuale;
- aggiunto il pulsante **Apri grande** nel pannello degli scenari;
- tabella e grafico possono essere aperti in una finestra separata, ridimensionabile e massimizzabile;
- doppio clic sulla tabella per aprire la vista estesa;
- documentazione e test aggiornati.

## Precisazione sul premio

Bid e Ask sono già quotazioni del premio di mercato. La versione 1.1.0 non usa un modello teorico come Black-Scholes: per una simulazione di acquisto utilizza l'Ask visualizzato dal broker.

## Limiti noti

Il programma continua a calcolare esclusivamente il payoff alla scadenza. Non stima il prezzo dell'opzione prima della scadenza in funzione di volatilità implicita e greche.
