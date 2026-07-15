# Option Payoff Calculator v1.0.0

Prima release pubblica del calcolatore desktop per il payoff a scadenza delle opzioni.

## Funzionalità incluse

- calcolo per Call e Put acquistate;
- premio totale, costo complessivo e perdita massima;
- pareggio comprensivo delle commissioni;
- tabella degli scenari alla scadenza;
- grafico del profitto/perdita netto;
- supporto per contratti e moltiplicatori differenti;
- pulsante Pulisci per iniziare un nuovo calcolo;
- build con Maven oppure direttamente con JDK 21;
- test automatici JUnit 5.

## Limiti noti

La versione 1.0.0 calcola esclusivamente il payoff alla scadenza. Non include ancora una stima del premio prima della scadenza basata su volatilità implicita e greche.
