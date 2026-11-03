import java.io.PrintStream;
import java.util.Locale;

final class KonsolenAusgabe {
    private KonsolenAusgabe() { }

    static void anzeigen(Ergebnis ergebnis, PrintStream ausgabe) {
        ausgabe.println("\n=== " + ergebnis.getVerfahren() + " ===");
        ausgabe.println("Gantt-Diagramm: [Start -- Prozess --> Ende], nicht maßstabsgetreu; LEER = CPU-Leerlauf");
        Zeitabschnitt[] abschnitte = ergebnis.getAbschnitte();
        for (int index = 0; index < abschnitte.length; index++) {
            Zeitabschnitt abschnitt = abschnitte[index];
            ausgabe.printf("[%d -- %s --> %d] ", abschnitt.getStart(), abschnitt.getName(), abschnitt.getEnde());
            if ((index + 1) % 4 == 0 || index == abschnitte.length - 1) {
                ausgabe.println();
            }
        }
        ausgabe.printf("%n%-12s %8s %8s %8s %8s %8s %10s %8s%n",
                "Prozess", "Ankunft", "Rechnen", "Start", "Ende", "Warten", "Durchlauf", "Antwort");
        for (ProzessStatistik statistik : ergebnis.getStatistiken()) {
            Prozess prozess = statistik.getProzess();
            ausgabe.printf("%-12s %8d %8d %8d %8d %8d %10d %8d%n",
                    prozess.getName(), prozess.getAnkunft(), prozess.getRechenzeit(), statistik.getErsterStart(),
                    statistik.getEnde(), statistik.getWartezeit(), statistik.getDurchlaufzeit(), statistik.getAntwortzeit());
        }
        ausgabe.printf(Locale.GERMANY, "Mittelwerte: Warten = %.2f | Durchlauf = %.2f | Antwort = %.2f%n",
                ergebnis.getMittlereWartezeit(), ergebnis.getMittlereDurchlaufzeit(), ergebnis.getMittlereAntwortzeit());
    }
}
