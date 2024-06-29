package MediaManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;
import contract.Audio;
import contract.MediaContent;
import contract.Uploadable;
import contract.Uploader;
import java.io.Serializable;
public class MediaManager<T extends MediaContent & Uploadable> implements interfaces.MediaManager<T>, Serializable {
    private static final Random random= new Random();
    private long totalCapacity;
    private long currentCapacity;  // Aktuelle genutzte Kapazität
    static private final  HashMap<String, Uploader> producerList= new HashMap<>();
    private final ArrayList<T> mediaList= new ArrayList<>();

    public MediaManager(int totalCapacity) {
        this.currentCapacity = 0;
        this.totalCapacity = totalCapacity;
    }

    @Override
    public synchronized ArrayList<T> getMediaList() {
        return mediaList;
    }

    public synchronized long getCurrentCapacity() {
        return currentCapacity;
    }


    @Override
    public synchronized void insert(T media) {
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
            throw new IllegalStateException("The address already exists.");
        }
        //Producer hinzufügen aber erstmal überprüfen, ob es einen Uploader mit demselben Namen schon in der producerList existiert
        boolean uploaderExists = producerList.values().stream()
                .anyMatch(uploader -> uploader.getName().equals(media.getUploader().getName()));
        if (!uploaderExists) {
            producerList.put(generateUniqueKey(), media.getUploader());
        }

        mediaList.add(media);

        currentCapacity += media.getSize();
        System.out.println("Media file successfully added.");
    }

    @Override
    public synchronized void removeMedia(T media) {
        if (mediaList.remove(media)) {
            currentCapacity -= media.getSize();
            removeProducerIfOrphaned(media.getUploader());
            System.out.println("Media file successfully removed.");
        } else {
            throw new IllegalStateException("Media file not found or could not be removed.");
        }
    }

    @Override
    public void removeProducer(String producerName) {
         producerList.remove(producerName);

        if (producerList !=null && producerList.containsKey(producerName)) {
            getMediaList().stream()
                    .filter(media -> media.getUploader().getName().equals(producerName))
                    .forEach(media -> removeProducerIfOrphaned(media.getUploader()));
            System.out.println("Producer successfully removed.");
        } else {
            throw new IllegalStateException("Producer not found or could not be removed.");
        }
    }


    //Entfernen von verwaisten Produzenten
    private synchronized void removeProducerIfOrphaned(Uploader uploader) {
        boolean isOrphaned = mediaList.stream()
                .noneMatch(media -> media.getUploader().equals(uploader));

        if (isOrphaned) {
            producerList.values().removeIf(existingUploader -> existingUploader.equals(uploader));
        }
    }


    @Override
    public synchronized void update(String adresse) {
        T existingMedia = mediaList.stream()
                .filter(media -> media.getAddress().equals(adresse))
                .findFirst()
                .orElse(null);

        if (existingMedia != null) {
            int index = mediaList.indexOf(existingMedia);
            mediaList.get(index).incrementAccessCount();
            System.out.println("Media file successfully updated.");
        } else {
            throw new IllegalStateException("Media file not found or could not be updated.");
        }
    }


    @Override
    public synchronized String read() {
        return mediaList.stream()
                .map(T::toString)
                .collect(Collectors.joining(", "));
    }

    // Zufallsgenerator zur Erstellung der Schlüssel
    public synchronized static String generateUniqueKey() {
        return String.format("%04d", random.nextInt(10000));
    }

    //Abruf aller Produzent*innen mit der Anzahl der ihrer Mediadateien
    public HashMap<String, Integer> getProducersWithMediaCount(){
        HashMap<String, Integer>producerMediaCount= new HashMap<>();
        int count = 0;

       for (Uploader producer : producerList.values()) {
           mediaList.stream().filter(audio -> audio.getUploader().equals(producer)).count();
           producerMediaCount.put(producer.toString(), count);
       }

    return producerMediaCount;}

    public synchronized HashMap<String, Uploader> getProducerList() {
        return new HashMap<>(producerList);    // eine Kopie zurückgeben
    }

    public synchronized long getTotalCapacity() {
        return totalCapacity;
    }
}
