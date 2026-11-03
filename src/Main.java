import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

public final class Main {
    private Main() { }

    public static void main(String[] argumente) {
        PrintStream ausgabe = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        if (argumente.length == 1 && argumente[0].equals("--demo")) {
            vergleichen(beispiel(), 2, ausgabe);
        } else if (argumente.length == 0) {
            starten(System.in, ausgabe);
        } else {
            new PrintStream(System.err, true, StandardCharsets.UTF_8)
                    .println("Aufruf: java -jar cpu-scheduling-simulator-1.0.0.jar [--demo]");
        }
    }

    static void starten(InputStream quelle, PrintStream ausgabe) {
        KonsolenEingabe eingabe = new KonsolenEingabe(new Scanner(quelle, StandardCharsets.UTF_8), ausgabe);
        Prozess[] prozesse = beispiel();
        ausgabe.println("CPU-Scheduling-Simulator – ein Prozessor, ganzzahlige Zeiteinheiten");
        try {
            while (true) {
                ausgabe.println("\n1 Eigene Prozesse eingeben | 2 Beispieldaten laden | 3 Prozesse anzeigen");
                ausgabe.println("4 FCFS | 5 SJF (nichtpräemptiv) | 6 Round Robin | 7 Alle vergleichen | 0 Beenden");
                int auswahl = eingabe.zahl("Auswahl: ", 0, 7);
                switch (auswahl) {
                    case 0 -> { ausgabe.println("Auf Wiedersehen!"); return; }
                    case 1 -> prozesse = prozesseLesen(eingabe, ausgabe);
                    case 2 -> { prozesse = beispiel(); ausgabe.println("Beispieldaten geladen."); }
                    case 3 -> {
                        for (Prozess prozess : prozesse) {
                            ausgabe.printf("%s: Ankunft %d, Rechenzeit %d%n", prozess.getName(), prozess.getAnkunft(), prozess.getRechenzeit());
                        }
                    }
                    case 4 -> KonsolenAusgabe.anzeigen(new FcfsScheduler().simulieren(prozesse), ausgabe);
                    case 5 -> KonsolenAusgabe.anzeigen(new SjfScheduler().simulieren(prozesse), ausgabe);
                    case 6 -> KonsolenAusgabe.anzeigen(new RoundRobinScheduler(quantumLesen(eingabe)).simulieren(prozesse), ausgabe);
                    case 7 -> vergleichen(prozesse, quantumLesen(eingabe), ausgabe);
                    default -> throw new IllegalStateException("Unbekannter Menüpunkt.");
                }
            }
        } catch (NoSuchElementException exception) {
            ausgabe.println("\nEingabe beendet. Programm wird geschlossen.");
        }
    }

    private static int quantumLesen(KonsolenEingabe eingabe) {
        return eingabe.zahl("Zeitquantum (1–100): ", 1, Prozess.MAX_RECHENZEIT);
    }

    private static Prozess[] prozesseLesen(KonsolenEingabe eingabe, PrintStream ausgabe) {
        int anzahl = eingabe.zahl("Anzahl Prozesse (1–20): ", 1, Ablauf.MAX_PROZESSE);
        Prozess[] prozesse = new Prozess[anzahl];
        for (int index = 0; index < anzahl; index++) {
            ausgabe.println("Prozess " + (index + 1));
            while (true) {
                String name = eingabe.text("Name (1–12 Zeichen): ");
                try {
                    new Prozess(name, 0, 1);
                    for (int vorher = 0; vorher < index; vorher++) {
                        if (prozesse[vorher].getName().equalsIgnoreCase(name)) {
                            throw new IllegalArgumentException("Dieser Name ist bereits vergeben.");
                        }
                    }
                    int ankunft = eingabe.zahl("Ankunft (0–1000000): ", 0, Prozess.MAX_ANKUNFT);
                    int rechenzeit = eingabe.zahl("Rechenzeit (1–100): ", 1, Prozess.MAX_RECHENZEIT);
                    prozesse[index] = new Prozess(name, ankunft, rechenzeit);
                    break;
                } catch (IllegalArgumentException exception) {
                    ausgabe.println(exception.getMessage());
                }
            }
        }
        return prozesse;
    }

    private static void vergleichen(Prozess[] prozesse, int quantum, PrintStream ausgabe) {
        Scheduler[] verfahren = {new FcfsScheduler(), new SjfScheduler(), new RoundRobinScheduler(quantum)};
        for (Scheduler scheduler : verfahren) {
            KonsolenAusgabe.anzeigen(scheduler.simulieren(prozesse), ausgabe);
        }
    }

    static Prozess[] beispiel() {
        return new Prozess[] {new Prozess("P1", 0, 5), new Prozess("P2", 1, 3), new Prozess("P3", 2, 1)};
    }
}
