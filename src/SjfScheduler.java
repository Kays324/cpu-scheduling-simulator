public final class SjfScheduler implements Scheduler {
    @Override
    public String getName() { return "SJF (nichtpräemptiv)"; }

    @Override
    public Ergebnis simulieren(Prozess[] prozesse) {
        Ablauf ablauf = new Ablauf(prozesse);
        int abgeschlossen = 0;
        while (abgeschlossen < ablauf.getAnzahl()) {
            int auswahl = -1;
            long naechsteAnkunft = Long.MAX_VALUE;
            for (int index = 0; index < ablauf.getAnzahl(); index++) {
                if (ablauf.getRestzeit(index) == 0) {
                    continue;
                }
                Prozess prozess = ablauf.getProzess(index);
                if (prozess.getAnkunft() <= ablauf.getZeit()) {
                    // Bei Gleichstand bleibt der zuerst gefundene, früher einsortierte Prozess.
                    if (auswahl == -1 || prozess.getRechenzeit() < ablauf.getProzess(auswahl).getRechenzeit()) {
                        auswahl = index;
                    }
                } else {
                    naechsteAnkunft = Math.min(naechsteAnkunft, prozess.getAnkunft());
                }
            }
            if (auswahl == -1) {
                ablauf.wartenBis(naechsteAnkunft);
            } else {
                ablauf.ausfuehren(auswahl, ablauf.getRestzeit(auswahl));
                abgeschlossen++;
            }
        }
        return ablauf.ergebnis(getName());
    }
}
