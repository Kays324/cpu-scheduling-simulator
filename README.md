# CPU-Scheduling-Simulator

Konsolenanwendung in Java zum Vergleich von FCFS, nichtpräemptivem SJF und Round Robin. Für jeden Prozess werden Wartezeit, Durchlaufzeit und Antwortzeit berechnet. Ein textbasiertes Gantt-Diagramm zeigt den Ablauf einschließlich CPU-Leerlauf.

## Voraussetzungen

- JDK 17 oder neuer
- UTF-8 als Zeichenkodierung

## Starten

```shell
javac -encoding UTF-8 --release 17 -d out src/*.java
java -cp out Main
```

Beispiel ausführen:

```shell
java -cp out Main --demo
```

Einstiegspunkt ist `src/Main.java`. Das Projekt benötigt nur das JDK und keine externen Bibliotheken.

## Funktionen

- Eigene Prozessdaten eingeben oder Beispieldaten laden
- Verfahren einzeln ausführen oder auf denselben Daten vergleichen
- Zeitquantum für Round Robin festlegen
- Gantt-Diagramm, Einzelstatistiken und Mittelwerte anzeigen
- Ungültige Eingaben abfangen und erneute Eingabe ermöglichen

## Scheduling-Regeln

Die Simulation verwendet einen Prozessor und einen CPU-Burst je Prozess. Zeiten sind ganzzahlig. E/A-Phasen und Kontextwechselkosten werden nicht berücksichtigt.

- **FCFS:** Ankunftsreihenfolge; bei gleicher Ankunft gilt die Eingabereihenfolge.
- **SJF:** Kürzester bereits angekommener Prozess. Bei gleicher Rechenzeit entscheidet zuerst die Ankunft, danach die Eingabereihenfolge. Laufende Prozesse werden nicht unterbrochen.
- **Round Robin:** FIFO-Warteschlange. Ankünfte bis einschließlich Quantum-Ende werden vor dem unterbrochenen Prozess eingereiht; bereits wartende Prozesse behalten ihre Position.

Gantt-Abschnitte beschreiben halboffene Intervalle `[Start, Ende)` und sind nicht maßstabsgetreu. `LEER` kennzeichnet CPU-Leerlauf.

```text
Durchlaufzeit = Ende − Ankunft
Wartezeit     = Durchlaufzeit − Rechenzeit
Antwortzeit   = erster Start − Ankunft
```

## Beispiel

| Prozess | Ankunft | Rechenzeit |
| --- | ---: | ---: |
| P1 | 0 | 5 |
| P2 | 1 | 3 |
| P3 | 2 | 1 |

| Verfahren | Ø Wartezeit | Ø Durchlaufzeit | Ø Antwortzeit |
| --- | ---: | ---: | ---: |
| FCFS | 3,33 | 6,33 | 3,33 |
| SJF | 2,67 | 5,67 | 2,67 |
| Round Robin, Quantum 2 | 3,33 | 6,33 | 1,00 |

## Eingabegrenzen

1–20 Prozesse, Ankunft von 0 bis 1.000.000, Rechenzeit und Quantum von 1 bis 100. Namen bestehen aus 1–12 Zeichen, beginnen mit einem ASCII-Buchstaben und dürfen danach auch Ziffern, `_` und `-` enthalten. Namen müssen unabhängig von Groß-/Kleinschreibung eindeutig sein. `LEER` ist reserviert.

## Aufbau

Der Quellcode liegt in `src`, die Tests in `tests`.

- `Scheduler`: gemeinsame Schnittstelle der drei Verfahren
- `Prozess`: unveränderliche Prozessdaten
- `Ablauf`: interner Simulationszustand mit Restzeiten und Zeitabschnitten
- `Ergebnis` und `ProzessStatistik`: Ergebnisse und Kennzahlen
- `Main`, `KonsolenEingabe`, `KonsolenAusgabe`: Konsolenoberfläche

## Tests

Die Tests sind eigenständige Java-Programme ohne externe Bibliotheken:

```shell
javac -encoding UTF-8 --release 17 -d out src/*.java tests/*.java
java -cp out ProjektTest
```

Bei einer fehlgeschlagenen Prüfung wird ein `AssertionError` ausgelöst. Die Tests prüfen bekannte Ergebnisse, Randfälle und ungültige Eingaben.
