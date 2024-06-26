package Simulation;

import MediaImpl.Uploader;
import contract.Tag;
import MediaImpl.Audio;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import MediaManager.AudioManager;
import observerImpl.Observer;
import observerImpl.Subjekt;

public class Simulation_1 extends Thread {

    private class RetrieveThread extends Thread {
        public void run() {
            while (true) {
                removeRandomAudio();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class InsertThread extends Thread {
        public void run() {
            while (true) {
                try {
                    insertRandomAudio();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(400);  // Simulate delay
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static final String[] RANDOM_PRODUCER = {
            "Queen",
            "John Lennon",
            "Michael Jackson",
            "Eagles",
            "Nirvana",
            "Bob Dylan",
            "Ed Sheeran",
            "Led Zeppelin",
            "The Beatles",
            "Adele",
            "Oasis"
    };

    private static final String[] RANDOM_AUDIO_NAME = {
            "Bohemian Rhapsody",
            "Imagine",
            "Billie Jean",
            "Hotel California",
            "Smells Like Teen Spirit",
            "Like a Rolling Stone",
            "Shape of You",
            "Stairway to Heaven",
            "Hey Jude",
            "Rolling in the Deep",
            "Wonderwall"
    };

    private static final Random RANDOM = new Random();

    public void initialize() {
        List<Tag> tags = generateRandomTags(); // Hier generieren
        contract.Uploader uploader1 = new MediaImpl.Uploader("Produzent1");
        contract.Uploader uploader2 = new MediaImpl.Uploader("Produzent2");

        Audio audio1 = new MediaImpl.Audio(tags, 0, "address1", 100, uploader1, Duration.ofMinutes(3), BigDecimal.valueOf(9.99));
        Audio audio2 = new MediaImpl.Audio(tags, 0, "address2", 150, uploader2, Duration.ofMinutes(4), BigDecimal.valueOf(14.99));
        audioList.add(audio1);
        audioList.add(audio2);
        producerList.put("12345", uploader1);
        producerList.put("12345", uploader2);
        audioManager = new AudioManager(300);
        Observer observer = new Observer(audioManager);
        subjekt.addObserver(observer);
    }

    private Uploader generateRandomProducer() {
        int index = RANDOM.nextInt(RANDOM_PRODUCER.length);
        return new Uploader(RANDOM_PRODUCER[index]);
    }

    private String generateRandomAudioName() {
        int index = RANDOM.nextInt(RANDOM_AUDIO_NAME.length);
        return RANDOM_AUDIO_NAME[index];
    }

    private long generateRandomSize() {
        return 10 * (RANDOM.nextInt(21) + 10); // Zufällige Größe zwischen 100 und 300 KB
    }

    private Duration generateRandomDuration() {
        return Duration.ofSeconds(RANDOM.nextInt(300) + 60); // Zufällige Dauer zwischen 60 und 360 Sekunden
    }

    private BigDecimal generateRandomVolume() {
        return BigDecimal.valueOf(10 * (RANDOM.nextInt(21) + 10));
    }

    private Audio generateRandomAudio() {
        long accessCount = 0; // Startwert für Zugriffszähler
        String address = generateRandomAudioName(); // Zufälliger Audio-Name
        long size = generateRandomSize(); // Zufällige Größe
        Uploader uploader = generateRandomProducer(); // Zufälliger Uploader
        Duration duration = generateRandomDuration(); // Zufällige Dauer
        BigDecimal volume = generateRandomVolume(); // Zufälliges Volumen

        return new Audio(tags, accessCount, address, size, uploader, duration, volume);
    }

    private List<Tag> generateRandomTags() {
        List<Tag> tags = new ArrayList<>();
        int numberOfTags = RANDOM.nextInt(Tag.values().length) + 1;
        Tag[] allTags = Tag.values();

        for (int i = 0; i < numberOfTags; i++) {
            int randomIndex = RANDOM.nextInt(allTags.length);
            tags.add(allTags[randomIndex]);
        }

        return tags;
    }

    @Override
    public void run() {
        initialize();
        System.out.println("Run Methode Nummer: " + getName());
        insertThread.start();
        retrieveThread.start();
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Beende Methode Nummer: " + getName());
    }

    // Methode zum zufälligen Hinzufügen eines Audio-Objekts
    private void insertRandomAudio() throws Exception {
        audioManager.insert(generateRandomAudio());
        subjekt.notifyObservers();
    }

    // Methode zum zufälligen Entfernen eines Audio-Objekts aus der Liste
    public void removeRandomAudio() {
        List<Audio> mediaList = audioManager.getMediaList();

        synchronized (audioManager) {
            if (!mediaList.isEmpty()) {
                int randomIndex = RANDOM.nextInt(mediaList.size());
                Audio removedAudio = mediaList.get(randomIndex);

                if (removedAudio != null) {
                    audioManager.removeMedia(removedAudio);
                    subjekt.notifyObservers();
                }
            }
        }
    }


    private ArrayList<Audio> audioList = new ArrayList<>();
    private HashMap<String, contract.Uploader> producerList = new HashMap<>();
    private AudioManager audioManager = new AudioManager(3);
    private final InsertThread insertThread = new InsertThread();
    private final RetrieveThread retrieveThread = new RetrieveThread();
    private List<Tag> tags = generateRandomTags();
    private final Subjekt subjekt = new Subjekt();

    public Simulation_1(String name) {
        super(name);
    }
}
