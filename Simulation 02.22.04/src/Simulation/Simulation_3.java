package Simulation;

import MediaImpl.Audio;
import MediaImpl.Uploader;
import MediaManager.AudioManager;
import MediaManager.AudioManager;
import contract.Tag;
import observerImpl.Observer;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Simulation_3 extends Thread {
    private class RetrieveThread extends Thread {
        public void run() {
            while (true) {
                removeMinAudio();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class AccessThread extends Thread {
        public void run() {
            while (true) {
                try {
                    accessMedia();
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
            "Queen", "John Lennon", "Michael Jackson", "Eagles", "Nirvana",
            "Bob Dylan", "Ed Sheeran", "Led Zeppelin", "The Beatles", "Adele", "Oasis"
    };

    private static final String[] RANDOM_AUDIO_NAME = {
            "Bohemian Rhapsody", "Imagine", "Billie Jean", "Hotel California",
            "Smells Like Teen Spirit", "Like a Rolling Stone", "Shape of You",
            "Stairway to Heaven", "Hey Jude", "Rolling in the Deep", "Wonderwall"
    };

    private static final Random RANDOM = new Random();

    public void initialize() {
        List<Tag> tags = generateRandomTags();
        Uploader uploader1 = new Uploader("Producer1");
        Uploader uploader2 = new Uploader("Producer2");

        Audio audio1 = new Audio(tags, 0, "address1", 500, uploader1, Duration.ofMinutes(3), BigDecimal.valueOf(9.99));
        Audio audio2 = new Audio(tags, 0, "address2", 700, uploader2, Duration.ofMinutes(4), BigDecimal.valueOf(14.99));

        audioList.add(audio1);
        audioList.add(audio2);
        producerList.put("12345", uploader1);
        producerList.put("12345", uploader2);

        audioManager = new AudioManager( 200000);
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
        return 10 * (RANDOM.nextInt(21) + 10); // Random size between 10 and 30 KB
    }

    private Duration generateRandomDuration() {
        return Duration.ofSeconds(RANDOM.nextInt(300) + 60); // Random duration between 60 and 360 seconds
    }

    private BigDecimal generateRandomVolume() {
        return BigDecimal.valueOf(10 * (RANDOM.nextInt(21) + 10));
    }

    private Audio generateRandomAudio() {
        long accessCount = 0; // Initial access count
        String address = generateRandomAudioName(); // Random audio name
        long size = generateRandomSize(); // Random size
        Uploader uploader = generateRandomProducer(); // Random uploader
        Duration duration = generateRandomDuration(); // Random duration
        BigDecimal volume = generateRandomVolume(); // Random volume

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
    private void accessMedia() {
        if (!audioList.isEmpty()) {
            int randomIndex = RANDOM.nextInt(audioList.size());
            Audio audio = audioList.get(randomIndex);
            audio.incrementAccessCount();
            notifyObservers("Thread" + n + ": Access counter for " + audio.getAddress() + " has been increased");
        } else {
            notifyObservers("Thread" + n + ": The audio list is empty. No access counter incremented.");
        }
    }


    @Override
    public void run() {
        initialize();
        System.out.println("Run Method Number: " + getName());
        new InsertThread().start();
        new RetrieveThread().start();
        new AccessThread().start();
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("End Method Number: " + getName());
    }

    // Method to randomly add an audio object.
    private boolean insertRandomAudio() throws Exception {
        synchronized (audioManager) {
            audioManager.insert(generateRandomAudio());
            notifyObservers("Thread"+n+": Added Audio: " + generateRandomAudioName());
        }
        return true;
    }

    // Method to remove audio object with minimum access count.
    public void removeMinAudio() {
        synchronized (audioManager) {
            if (!audioList.isEmpty()) {
                Audio removedAudio = audioList.stream().min((o1, o2) -> Long.compare(o1.getAccessCount(), o1.getAccessCount()))
                        .orElse(null);
                audioList.remove(removedAudio);
                notifyObservers("Thread"+ n+": Removed Audio: " + removedAudio.getAddress());
            } else {
                notifyObservers("Thread"+ n+": The list of audio files is empty. No audio to remove.");
            }
        }
    }

    private void addObserver(interfaces.Observer observer) {
        observers.add(observer);
    }

    private void removeObserver(interfaces.Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String message) {
        for (interfaces.Observer observer : observers) {
            observer.update();
        }
    }

    private final ArrayList<Audio> audioList = new ArrayList<>();
    private HashMap<String, contract.Uploader> producerList = new HashMap<>();
    private AudioManager audioManager;
    private final List<Tag> tags = generateRandomTags();
    private final int n;
    private final List<interfaces.Observer> observers = new ArrayList<>();
    private boolean running = false;

    public Simulation_3(String name, int n) {
        super(name);
        this.n = n; // n inserting and n deleting threads
        Observer consoleObserver = new Observer(audioManager);
        observers.add(consoleObserver);
    }
}
