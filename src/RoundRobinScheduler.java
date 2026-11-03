import java.util.ArrayDeque;
import java.util.Queue;

public final class RoundRobinScheduler implements Scheduler {
    private final int quantum;

    public RoundRobinScheduler(int quantum) {
        if (quantum < 1 || quantum > Prozess.MAX_RECHENZEIT) {
            throw new IllegalArgumentException("Quantum muss zwischen 1 und " + Prozess.MAX_RECHENZEIT + " liegen.");
        }
        this.quantum = quantum;
    }

    @Override
    public String getName() { return "Round Robin (q=" + quantum + ")"; }

    @Override
    public Ergebnis simulieren(Prozess[] prozesse) {
        Ablauf ablauf = new Ablauf(prozesse);
        Queue<Integer> bereit = new ArrayDeque<>();
        int naechster = 0;
        while (naechster < ablauf.getAnzahl() || !bereit.isEmpty()) {
            if (bereit.isEmpty()) {
                ablauf.wartenBis(ablauf.getProzess(naechster).getAnkunft());
            }
            while (naechster < ablauf.getAnzahl()
                    && ablauf.getProzess(naechster).getAnkunft() <= ablauf.getZeit()) {
                bereit.add(naechster++);
            }
            int aktuell = bereit.remove();
            ablauf.ausfuehren(aktuell, Math.min(quantum, ablauf.getRestzeit(aktuell)));
            // Ankünfte bis einschließlich Quantum-Ende werden vor dem laufenden Prozess eingereiht.
            while (naechster < ablauf.getAnzahl()
                    && ablauf.getProzess(naechster).getAnkunft() <= ablauf.getZeit()) {
                bereit.add(naechster++);
            }
            if (ablauf.getRestzeit(aktuell) > 0) {
                bereit.add(aktuell);
            }
        }
        return ablauf.ergebnis(getName());
    }
}
