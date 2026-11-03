/** Einheitliche Schnittstelle für austauschbare Scheduling-Verfahren. */
public interface Scheduler {
    String getName();
    Ergebnis simulieren(Prozess[] prozesse);
}
