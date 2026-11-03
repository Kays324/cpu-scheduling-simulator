import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Interner Simulationszustand mit gemeinsamer Zeit- und Statistikverwaltung. */
final class Ablauf {
    static final int MAX_PROZESSE = 20;
    private final Prozess[] prozesse;
    private final int[] restzeiten;
    private final long[] ersteStarts;
    private final long[] endzeiten;
    private final List<Zeitabschnitt> abschnitte = new ArrayList<>();
    private long zeit;

    Ablauf(Prozess[] eingabe) {
        if (eingabe == null || eingabe.length == 0 || eingabe.length > MAX_PROZESSE) {
            throw new IllegalArgumentException("Bitte 1 bis " + MAX_PROZESSE + " Prozesse angeben.");
        }
        prozesse = eingabe.clone();
        for (int index = 0; index < prozesse.length; index++) {
            if (prozesse[index] == null) {
                throw new IllegalArgumentException("Ein Prozess darf nicht null sein.");
            }
            for (int vorher = 0; vorher < index; vorher++) {
                if (prozesse[index].getName().equalsIgnoreCase(prozesse[vorher].getName())) {
                    throw new IllegalArgumentException("Prozessnamen müssen eindeutig sein.");
                }
            }
        }
        // Stabiles Insertion-Sort: Gleiche Ankunftszeiten behalten die Eingabereihenfolge.
        for (int index = 1; index < prozesse.length; index++) {
            Prozess aktuell = prozesse[index];
            int position = index - 1;
            while (position >= 0 && prozesse[position].getAnkunft() > aktuell.getAnkunft()) {
                prozesse[position + 1] = prozesse[position];
                position--;
            }
            prozesse[position + 1] = aktuell;
        }
        restzeiten = new int[prozesse.length];
        ersteStarts = new long[prozesse.length];
        endzeiten = new long[prozesse.length];
        Arrays.fill(ersteStarts, -1);
        for (int index = 0; index < prozesse.length; index++) {
            restzeiten[index] = prozesse[index].getRechenzeit();
        }
    }

    int getAnzahl() { return prozesse.length; }
    Prozess getProzess(int index) { return prozesse[index]; }
    int getRestzeit(int index) { return restzeiten[index]; }
    long getZeit() { return zeit; }

    void wartenBis(long ziel) {
        if (ziel > zeit) {
            abschnitte.add(new Zeitabschnitt("LEER", zeit, ziel));
            zeit = ziel;
        }
    }

    void ausfuehren(int index, int dauer) {
        if (ersteStarts[index] == -1) {
            ersteStarts[index] = zeit;
        }
        abschnitte.add(new Zeitabschnitt(prozesse[index].getName(), zeit, zeit + dauer));
        zeit += dauer;
        restzeiten[index] -= dauer;
        if (restzeiten[index] == 0) {
            endzeiten[index] = zeit;
        }
    }

    Ergebnis ergebnis(String name) {
        ProzessStatistik[] statistiken = new ProzessStatistik[prozesse.length];
        for (int index = 0; index < prozesse.length; index++) {
            statistiken[index] = new ProzessStatistik(prozesse[index], ersteStarts[index], endzeiten[index]);
        }
        return new Ergebnis(name, abschnitte.toArray(new Zeitabschnitt[0]), statistiken);
    }
}
