package interfaces;

import contract.Audio;
import java.util.ArrayList;
public interface AudioManager {
    /**
     * Gibt eine Liste von Mediendaten zurück.
     *
     * @return Die Liste von Mediendaten.
     */
    ArrayList<MediaImpl.Audio> getMediaList();

    /**
     * Fügt ein Mediendatum hinzu.
     * @param media Das Mediendatum, das hinzugefügt werden soll.
     */
    void insert(MediaImpl.Audio media);

    /**
     * Entfernt ein Mediendatum.
     * @param media Das Mediendatum, das entfernt werden soll.
     */
    void remove(MediaImpl.Audio media);
    /**
     * Aktualisiert die Informationen eines Mediendatums.
     * @param media Das Mediendatum, dessen Informationen aktualisiert werden sollen.
     */
    void update(MediaImpl.Audio media);

    /**
     * Zeigt alle Mediendaten an.
     *
     * @return
     */
    String read();



}
