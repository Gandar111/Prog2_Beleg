package MediaManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;
import contract.Audio;
import contract.Uploader;

public class AudioManagerImpl implements interfaces.AudioManager {
    private static Random random= new Random();
    private long totalCapacity;
    private long currentCapacity; // Aktuelle genutzte Kapazität
    private static HashMap<String, Uploader> producerList;
    private ArrayList<MediaImpl.Audio> mediaList;



    public AudioManagerImpl(ArrayList<MediaImpl.Audio> mediaList, HashMap<String, Uploader> producers, int totalCapacity) {
        this.currentCapacity = currentCapacity;
        this.mediaList = mediaList;
        this.producerList = producers;
        this.totalCapacity = totalCapacity;
    }

    @Override
    public ArrayList<MediaImpl.Audio> getMediaList() {
        return mediaList;
    }

    @Override
    public synchronized void insert(MediaImpl.Audio media) {
        if (media==null){
            return;
        }
        if (mediaList.stream().anyMatch(audio -> audio != null && audio.getUploader() != null && audio.getAddress() != null &&
                audio.getUploader().getName().equals(media.getUploader().getName()) &&
                audio.getAddress().equals(media.getAddress()))) {
            System.out.println("The media file already belongs to an existing producer.");
            return;
        }


        if (currentCapacity + media.getSize() > totalCapacity) {
            throw new IllegalStateException("Not enough storage space available");
        }

        if (mediaList.stream().anyMatch(m -> m.getAddress().equals(media.getAddress()))) {
            System.out.println("The address already exists.");
            return;
        }

        mediaList.add(media);
        producerList.put(generateUniqueKey(), media.getUploader());
        currentCapacity += media.getSize();
        System.out.println("Media file successfully added.");
    }



    @Override
    public void remove(MediaImpl.Audio media) {
        if (mediaList.remove(media)) {
            currentCapacity -= media.getSize();
            System.out.println("Media file successfully removed.");
        } else {
            System.out.println("Media file not found or could not be removed.");
        }
    }

    @Override
    public void update(MediaImpl.Audio updateMedia) {
        boolean updated = mediaList.stream()
                .anyMatch(media -> {
                    if (media.getAddress().equals(updateMedia.getAddress())) {
                        mediaList.remove(media);
                        mediaList.add(updateMedia);
                        return true;
                    }
                    return false;
                });

        if (updated) {
            System.out.println("Media file successfully updated.");
        } else {
            System.out.println("Media file not found or could not be updated.");
        }
    }


    @Override
    public String read() {
        return mediaList.stream()
                .map(Audio::toString)
                .collect(Collectors.joining(", "));
    }

    // Zufallsgenerator zur Erstellung der Schlüssel
    public static String generateUniqueKey() {
        return String.format("%04d", random.nextInt(10000));
    }



    public HashMap<String, Uploader> getProducerList() {
        return producerList;
    }

    public long getTotalCapacity() {
        return totalCapacity;
    }
}
