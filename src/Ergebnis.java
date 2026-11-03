public final class Ergebnis {
    private final String verfahren;
    private final Zeitabschnitt[] abschnitte;
    private final ProzessStatistik[] statistiken;

    Ergebnis(String verfahren, Zeitabschnitt[] abschnitte, ProzessStatistik[] statistiken) {
        this.verfahren = verfahren;
        this.abschnitte = abschnitte.clone();
        this.statistiken = statistiken.clone();
    }

    public String getVerfahren() { return verfahren; }
    public Zeitabschnitt[] getAbschnitte() { return abschnitte.clone(); }
    public ProzessStatistik[] getStatistiken() { return statistiken.clone(); }

    public double getMittlereWartezeit() {
        long summe = 0;
        for (ProzessStatistik statistik : statistiken) {
            summe += statistik.getWartezeit();
        }
        return (double) summe / statistiken.length;
    }

    public double getMittlereDurchlaufzeit() {
        long summe = 0;
        for (ProzessStatistik statistik : statistiken) {
            summe += statistik.getDurchlaufzeit();
        }
        return (double) summe / statistiken.length;
    }

    public double getMittlereAntwortzeit() {
        long summe = 0;
        for (ProzessStatistik statistik : statistiken) {
            summe += statistik.getAntwortzeit();
        }
        return (double) summe / statistiken.length;
    }
}
