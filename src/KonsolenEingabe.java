import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

final class KonsolenEingabe {
    private final Scanner scanner;
    private final PrintStream ausgabe;

    KonsolenEingabe(Scanner scanner, PrintStream ausgabe) {
        this.scanner = scanner;
        this.ausgabe = ausgabe;
    }

    String text(String frage) {
        ausgabe.print(frage);
        if (!scanner.hasNextLine()) {
            throw new NoSuchElementException("Eingabe beendet.");
        }
        return scanner.nextLine().trim();
    }

    int zahl(String frage, int minimum, int maximum) {
        while (true) {
            String eingabe = text(frage);
            try {
                int wert = Integer.parseInt(eingabe);
                if (wert >= minimum && wert <= maximum) {
                    return wert;
                }
            } catch (NumberFormatException exception) {
                // Auch Zahlen außerhalb des int-Bereichs werden als ungültige Eingabe behandelt.
            }
            ausgabe.println("Bitte eine ganze Zahl von " + minimum + " bis " + maximum + " eingeben.");
        }
    }
}
