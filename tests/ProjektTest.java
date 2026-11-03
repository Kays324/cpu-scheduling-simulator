import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class ProjektTest {
    private static int pruefungen;

    public static void main(String[] argumente) {
        Scheduler[] verfahren = {new FcfsScheduler(), new SjfScheduler(), new RoundRobinScheduler(2)};
        String[] erwartetePlaene = {
                "P1:0-5;P2:5-8;P3:8-9;",
                "P1:0-5;P3:5-6;P2:6-9;",
                "P1:0-2;P2:2-4;P3:4-5;P1:5-7;P2:7-8;P1:8-9;"
        };
        long[][] wartezeiten = {{0, 4, 6}, {0, 5, 3}, {4, 4, 2}};
        long[][] durchlaufzeiten = {{5, 7, 7}, {5, 8, 4}, {9, 7, 3}};
        long[][] antwortzeiten = {{0, 4, 6}, {0, 5, 3}, {0, 1, 2}};
        for (int index = 0; index < verfahren.length; index++) {
            Scheduler scheduler = verfahren[index];
            Prozess[] prozesse = Main.beispiel();
            Ergebnis ergebnis = scheduler.simulieren(prozesse);
            pruefen(plan(ergebnis).equals(erwartetePlaene[index]), scheduler.getName() + ": Zeitplan");
            ProzessStatistik[] statistiken = ergebnis.getStatistiken();
            for (int position = 0; position < statistiken.length; position++) {
                pruefen(statistiken[position].getWartezeit() == wartezeiten[index][position], "Wartezeit");
                pruefen(statistiken[position].getDurchlaufzeit() == durchlaufzeiten[index][position], "Durchlaufzeit");
                pruefen(statistiken[position].getAntwortzeit() == antwortzeiten[index][position], "Antwortzeit");
            }
            pruefen(plan(scheduler.simulieren(prozesse)).equals(plan(ergebnis)), "Wiederholbare Simulation");
            Prozess[] unsortiert = {new Prozess("B", 10, 1), new Prozess("A", 3, 2)};
            pruefen(plan(scheduler.simulieren(unsortiert)).equals("LEER:0-3;A:3-5;LEER:5-10;B:10-11;"), "Leerlauf");
            pruefen(unsortiert[0].getName().equals("B"), "Eingabereihenfolge bleibt erhalten");
            erwarteFehler(() -> scheduler.simulieren(null));
            erwarteFehler(() -> scheduler.simulieren(new Prozess[0]));
            erwarteFehler(() -> scheduler.simulieren(new Prozess[21]));
            erwarteFehler(() -> scheduler.simulieren(new Prozess[] {null}));
            erwarteFehler(() -> scheduler.simulieren(new Prozess[] {
                    new Prozess("A", 0, 1), new Prozess("a", 0, 1)}));
        }
        Prozess[] grenze = {new Prozess("A", 0, 4), new Prozess("B", 2, 1)};
        pruefen(plan(new RoundRobinScheduler(2).simulieren(grenze)).equals("A:0-2;B:2-3;A:3-5;"), "Ankunft am Quantum-Ende");
        Prozess[] gleichstand = {new Prozess("Z", 1, 2), new Prozess("A", 1, 2), new Prozess("Start", 0, 4)};
        pruefen(plan(new SjfScheduler().simulieren(gleichstand)).equals("Start:0-4;Z:4-6;A:6-8;"), "SJF-Gleichstand");
        pruefen(plan(new RoundRobinScheduler(100).simulieren(Main.beispiel())).equals(erwartetePlaene[0]), "Großes Quantum");
        erwarteFehler(() -> new RoundRobinScheduler(0));
        erwarteFehler(() -> new RoundRobinScheduler(101));
        erwarteFehler(() -> new Prozess("LEER", 0, 1));
        erwarteFehler(() -> new Prozess("A", -1, 1));
        erwarteFehler(() -> new Prozess("A", 0, 0));
        erwarteFehler(() -> new Prozess("A", 0, 101));
        erwarteFehler(() -> new Prozess(null, 0, 1));

        ByteArrayOutputStream puffer = new ByteArrayOutputStream();
        Main.starten(new ByteArrayInputStream("abc\n7\n2\n0\n".getBytes(StandardCharsets.UTF_8)),
                new PrintStream(puffer, true, StandardCharsets.UTF_8));
        String ausgabe = puffer.toString(StandardCharsets.UTF_8);
        pruefen(ausgabe.contains("Bitte eine ganze Zahl"), "Ungültige Menüeingabe");
        pruefen(ausgabe.contains("Antwort = 1,00"), "Vergleich im Menü");
        pruefen(ausgabe.contains("Auf Wiedersehen!"), "Programmende");
        puffer.reset();
        Main.starten(new ByteArrayInputStream(new byte[0]), new PrintStream(puffer, true, StandardCharsets.UTF_8));
        pruefen(puffer.toString(StandardCharsets.UTF_8).contains("Eingabe beendet"), "EOF");
        System.out.println(pruefungen + " Pruefungen erfolgreich.");
    }

    private static String plan(Ergebnis ergebnis) {
        StringBuilder text = new StringBuilder();
        for (Zeitabschnitt abschnitt : ergebnis.getAbschnitte()) {
            text.append(abschnitt.getName()).append(':').append(abschnitt.getStart())
                    .append('-').append(abschnitt.getEnde()).append(';');
        }
        return text.toString();
    }

    private static void pruefen(boolean richtig, String beschreibung) {
        if (!richtig) {
            throw new AssertionError(beschreibung);
        }
        pruefungen++;
    }

    private static void erwarteFehler(Runnable aktion) {
        try {
            aktion.run();
        } catch (IllegalArgumentException exception) {
            pruefungen++;
            return;
        }
        throw new AssertionError("Ungueltige Eingabe wurde akzeptiert.");
    }
}
