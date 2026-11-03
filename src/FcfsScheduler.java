public final class FcfsScheduler implements Scheduler {
    @Override
    public String getName() { return "FCFS"; }

    @Override
    public Ergebnis simulieren(Prozess[] prozesse) {
        Ablauf ablauf = new Ablauf(prozesse);
        for (int index = 0; index < ablauf.getAnzahl(); index++) {
            ablauf.wartenBis(ablauf.getProzess(index).getAnkunft());
            ablauf.ausfuehren(index, ablauf.getRestzeit(index));
        }
        return ablauf.ergebnis(getName());
    }
}
