/** Unveränderliche Eingabedaten; jede Simulation verwaltet ihren Zustand separat. */
public final class Prozess {
    public static final int MAX_ANKUNFT = 1_000_000;
    public static final int MAX_RECHENZEIT = 100;

    private final String name;
    private final int ankunft;
    private final int rechenzeit;

    public Prozess(String name, int ankunft, int rechenzeit) {
        if (name == null || !name.matches("[A-Za-z][A-Za-z0-9_-]{0,11}")) {
            throw new IllegalArgumentException("Name: 1 bis 12 Zeichen, beginnend mit einem Buchstaben; danach Buchstaben, Ziffern, _ oder -.");
        }
        if (name.equalsIgnoreCase("LEER")) {
            throw new IllegalArgumentException("Der Name LEER ist für CPU-Leerlauf reserviert.");
        }
        if (ankunft < 0 || ankunft > MAX_ANKUNFT) {
            throw new IllegalArgumentException("Ankunft muss zwischen 0 und " + MAX_ANKUNFT + " liegen.");
        }
        if (rechenzeit < 1 || rechenzeit > MAX_RECHENZEIT) {
            throw new IllegalArgumentException("Rechenzeit muss zwischen 1 und " + MAX_RECHENZEIT + " liegen.");
        }
        this.name = name;
        this.ankunft = ankunft;
        this.rechenzeit = rechenzeit;
    }

    public String getName() { return name; }
    public int getAnkunft() { return ankunft; }
    public int getRechenzeit() { return rechenzeit; }
}
