

/*import MediaManager.AudioManager;
import contract.Tag;
import contract.Uploader;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DomainLogicmain {
    public static void main(String[] args) {
        // Erstellen von Tags
        List<Tag> tags = new ArrayList<>();
        // Tags können hier hinzugefügt werden

        // Erstellen von Uploadern (Produzenten)
        Uploader uploader1 = new MediaImpl.Uploader("Produzent1");
        Uploader uploader2 = new MediaImpl.Uploader("Produzent2");

        // Initialisieren der Producer-Map
        HashMap<String, Uploader> producers = new HashMap<>();
        producers.put(uploader1.getName(), uploader1);
        producers.put(uploader2.getName(), uploader2);

        // Erstellen von Audio-Mediendateien
        MediaImpl.Audio audio1 = new MediaImpl.Audio(tags, 0, "address1", 500, uploader1, Duration.ofMinutes(3), BigDecimal.valueOf(9.99));
        MediaImpl.Audio audio2 = new MediaImpl.Audio(tags, 0, "address2", 700, uploader2, Duration.ofMinutes(4), BigDecimal.valueOf(14.99));

        // Initialisieren der Medienliste
        ArrayList<MediaImpl.Audio> mediaList = new ArrayList<>();
        mediaList.add(audio1);
        mediaList.add(audio2);

        // Erstellen eines AudioManagers mit einer Gesamtkapazität von 2000
        interfaces.AudioManager audioManager = new AudioManager( 2000);

        // Anzeigen der aktuellen Medienliste
        System.out.println("Initiale Medienliste:");
        System.out.println(audioManager.read());

        // Einfügen einer neuen Audio-Mediendatei
        MediaImpl.Audio audio3 = new MediaImpl.Audio(tags, 0, "address3", 600, uploader1, Duration.ofMinutes(5), BigDecimal.valueOf(19.99));
        try {
            audioManager.insert(audio3);
            System.out.println("Medienliste nach dem Hinzufügen von audio3:");
            System.out.println(audioManager.read());
        } catch (Exception e) {
            System.out.println("Fehler beim Hinzufügen von audio3: " + e.getMessage());
        }

        // Entfernen einer bestehenden Audio-Mediendatei
        audioManager.remove(audio1);
        System.out.println("Medienliste nach dem Entfernen von audio1:");
        System.out.println(audioManager.read());

        // Versuch, eine Mediendatei mit einer bestehenden Adresse hinzuzufügen
        MediaImpl.Audio audio4 = new MediaImpl.Audio(tags, 0, "address2", 800, uploader1, Duration.ofMinutes(6), BigDecimal.valueOf(24.99));
        try {
            audioManager.insert(audio4);
            System.out.println("Medienliste nach dem Hinzufügen von audio4:");
            System.out.println(audioManager.read());
        } catch (Exception e) {
            System.out.println("Fehler beim Hinzufügen von audio4: " + e.getMessage());
        }
        MediaImpl.Audio audio66 = new MediaImpl.Audio(tags, 0, "address66", 800, uploader1, Duration.ofMinutes(6), BigDecimal.valueOf(24.99));
        try {
            audioManager.insert(audio66);
            System.out.println("es hat geklappt");
            System.out.println(audioManager.read());
        } catch (Exception e) {
            System.out.println("es hat nicht geklappt: " + e.getMessage());
        }

        // Anzeigen der Producer-Liste
        System.out.println("Producer-Liste:");

    }
}*/
