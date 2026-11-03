/** Halboffenes Intervall: Der Start gehört dazu, das Ende nicht. */
public final class Zeitabschnitt {
    private final String name;
    private final long start;
    private final long ende;

    Zeitabschnitt(String name, long start, long ende) {
        this.name = name;
        this.start = start;
        this.ende = ende;
    }

    public String getName() { return name; }
    public long getStart() { return start; }
    public long getEnde() { return ende; }
}
