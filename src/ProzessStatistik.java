public final class ProzessStatistik {
    private final Prozess prozess;
    private final long ersterStart;
    private final long ende;

    ProzessStatistik(Prozess prozess, long ersterStart, long ende) {
        this.prozess = prozess;
        this.ersterStart = ersterStart;
        this.ende = ende;
    }

    public Prozess getProzess() { return prozess; }
    public long getErsterStart() { return ersterStart; }
    public long getEnde() { return ende; }
    public long getDurchlaufzeit() { return ende - prozess.getAnkunft(); }
    public long getWartezeit() { return getDurchlaufzeit() - prozess.getRechenzeit(); }
    public long getAntwortzeit() { return ersterStart - prozess.getAnkunft(); }
}
