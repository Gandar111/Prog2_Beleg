package MediaManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import contract.Audio;
import contract.Uploader;

public class AudioManagerImpl implements interfaces.AudioManager {
    private long totalCapacity;
    private long currentCapacity; // Aktuelle genutzte Kapazität
    private HashMap<String, Uploader> producer;
    private ArrayList<MediaImpl.Audio> mediaList;



    public AudioManagerImpl(ArrayList<MediaImpl.Audio> mediaList, HashMap<String, Uploader> producers, int totalCapacity) {
        this.currentCapacity = currentCapacity;
        this.mediaList = mediaList;
        this.producer = producer;
        this.totalCapacity = totalCapacity;
    }

    @Override
    public ArrayList<MediaImpl.Audio> getMediaList() {
        return mediaList;
    }

    @Override
    public void insert(MediaImpl.Audio media) {
        // Überprüfung, ob die Media-Datei zu einem bereits existierenden Produzenten gehört
        if (mediaList.stream().filter(audio -> audio.getUploader().getName().
                equals(media.getUploader().getName())).
                anyMatch(audio -> audio.getAddress() == media.getAddress())) {
            throw new IllegalArgumentException("die Media-Datei gehört zu einer bereits existierenden Produzent*in ");
        }

        // Überprüfung, ob die Gesamtkapazität nicht überschritten wird
        if (currentCapacity + media.getSize() > totalCapacity) {
            throw new IllegalStateException("Nicht genug Speicher verfügbar");
        }

        // Überprüfung, ob die Adresse bereits existiert
        if (mediaList.stream().anyMatch(m -> m.getAddress().equals(media.getAddress()))) {
            throw new IllegalArgumentException("Die Adresse existiert bereits");
        }

        mediaList.add(media);
        currentCapacity += media.getSize();
    }

    @Override
    public void remove(MediaImpl.Audio media) {
        if (mediaList.remove(media)) {
            currentCapacity -= media.getSize();
        }
    }

    @Override
    public void update(MediaImpl.Audio updateMedia) {
        mediaList.stream()
                .filter(media -> media.getAddress().equals(updateMedia.getAddress()))
                .findFirst()
                .ifPresent(media -> {
                    mediaList.remove(media);
                    mediaList.add(updateMedia);
                });
    }

    @Override
    public String read() {
        return mediaList.stream()
                .map(Audio::toString)
                .collect(Collectors.joining(", "));
    }

    public HashMap<String, Uploader> getProducer() {
        return producer;
    }

    public long getTotalCapacity() {
        return totalCapacity;
    }
}
