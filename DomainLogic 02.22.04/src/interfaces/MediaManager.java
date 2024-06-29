package interfaces;
import contract.MediaContent;
import contract.Uploadable;

import java.io.Serializable;
import java.util.ArrayList;
public interface MediaManager<T extends MediaContent & Uploadable>  extends Serializable {
    /**
     * Gibt eine Liste von Mediendaten zurück.
     *
     * @return Die Liste von Mediendaten.
     */
    ArrayList<T> getMediaList();

    /**
     * Fügt ein Mediendatum hinzu.
     * @param media Das Mediendatum, das hinzugefügt werden soll.
     */
    void insert(T media);

    /**
     * Entfernt ein Mediendatum.
     * @param media Das Mediendatum, das entfernt werden soll.
     */
    void removeMedia(T media);
    /**
     * Entfernt ein Producer.
     * @param producerName Das producerName, das entfernt werden soll.
     */
    void removeProducer(String producerName);
    /**
     * Aktualisiert die Informationen eines Mediendatums.
     * @param adresse Das Medie dessen Informationen aktualisiert werden sollen.
     */

    void update(String adresse);

    /**
     * Zeigt alle Mediendaten an.
     *
     * @return
     */
    String read();



}
