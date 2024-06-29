package viewControler;
import MediaManager.AudioManager;
import MediaImpl.Audio;
import contract.Tag;
import contract.Uploader;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import jos.AudioStateIO;

public class CLI {
    private AudioManager audioManager;
    private final Scanner scanner = new Scanner(System.in);
    private static Operator currentCase;
    private final String filename = "/Users/ghamdan/Library/Mobile Documents/com~apple~CloudDocs/Desktop/Universiteat/Pro-3/Prog3_ss_24/pro3-uebungen-v3/AudioManager.ser";
    private final AudioStateIO audioStateIO= new AudioStateIO(filename);
    private String choice;

    public void initialize() {
        List<Tag> tags = new ArrayList<>();
        Uploader uploader1 = new MediaImpl.Uploader("Produzent1");
        Uploader uploader2 = new MediaImpl.Uploader("Produzent2");
        ArrayList<MediaImpl.Audio> mediaList = new ArrayList<>();
        Audio audio1 = new MediaImpl.Audio(tags, 0, "address1", 500, uploader1, Duration.ofMinutes(3), BigDecimal.valueOf(9.99));
        Audio audio2 = new MediaImpl.Audio(tags, 0, "address2", 700, uploader2, Duration.ofMinutes(4), BigDecimal.valueOf(14.99));
        mediaList.add(audio1);
        mediaList.add(audio2);

        HashMap<String, Uploader> producers = new HashMap<>();
        producers.put("upl1", uploader1);
        producers.put("upl2", uploader2);
        audioManager = new AudioManager( 2000);
    }

    public void showMenu() {
        System.out.println("Please enter the desired command to execute the program:");
        System.out.println(":c switch to INSERTION mode");
        System.out.println(":d switch to DELETE mode");
        System.out.println(":r switch to DISPLAY mode");
        System.out.println(":u switch to UPDATE mode");
        System.out.println(":s switch to Persistence mode");
        System.out.println(":e to quit the program");
        System.out.println("\nEnter command here: ");
    }

    public void execute() {
        showMenu();
        try (Scanner s = new Scanner(System.in)) {
            while (true) {
                try {
                    String input = s.nextLine().trim().toLowerCase();

                    if (input.startsWith(":")) {
                        switchCommand(input);
                    } else {
                        try {
                            executeCommand();
                            execute();
                        } catch (Exception e) {
                            System.out.println("Wrong option");
                        }
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("Invalid command! Please enter a valid command.");
                }
            }
        }
    }

    private void executeCommand() throws Exception {
        switch (currentCase) {
            case INSERT:
                insertAudio();
                break;
            case REMOVE:
                System.out.println("type command :\n 1.Audio delete\n 2.Producer delete\n");
                choice = scanner.nextLine().trim();
                if ("1".equals(choice)) {
                    delAudio();
                } else if ("2".equals(choice)) {
                    delProducer();
                } else {
                    System.out.println("Invalid choice for persistence operations.");
                }
                break;
            case READ:
                displayAudio();
                break;
            case UPDATE:
                updateAudio();
                break;
            case PERSISTENCE:
                System.out.println("type command :\n 1.Save JOS \n 2.Load JOS\n");
                choice=null;
                choice = scanner.nextLine().trim();
                if ("1".equals(choice)) {
                    saveAudioManager();
                } else if ("2".equals(choice)) {
                    loadAudioManager();
                } else {
                    System.out.println("Invalid choice for persistence operations.");
                }
                break;
            case QUIT:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input - execute command");
        }
    }

    private static void switchCommand(String input) {
        Command command = new Command(input);

        switch (command.operator) {
            case INSERT:
                currentCase = Operator.INSERT;
                System.out.println("Switched to INSERTION mode");
                break;
            case REMOVE:
                currentCase = Operator.REMOVE;
                System.out.println("Switched to DELETE mode");
                break;
            case READ:
                currentCase = Operator.READ;
                System.out.println("Switched to DISPLAY mode");
                break;
            case UPDATE:
                currentCase = Operator.UPDATE;
                System.out.println("Switched to UPDATE mode");
                break;
            case PERSISTENCE:
                currentCase = Operator.PERSISTENCE;
                System.out.println("Switched to " + command.operator + " mode");
                break;
            case QUIT:
                currentCase = Operator.QUIT;
                System.out.println("Exit ..");
                break;
            default:
                System.out.println("Error");
        }
    }

    public void insertAudio() {
        System.out.println("Media file title:");
        String title = scanner.nextLine();
        System.out.println("Uploader name:");
        String uploaderName = scanner.nextLine();
        System.out.println("Size:");
        long size = Long.parseLong(scanner.nextLine());
        System.out.println("Duration (in seconds):");
        long durationSeconds = Long.parseLong(scanner.nextLine());

        Uploader uploader = new MediaImpl.Uploader(uploaderName);

        Audio newAudio = new MediaImpl.Audio(new ArrayList<>(), 0, title, size, uploader, Duration.ofSeconds(durationSeconds), BigDecimal.ZERO);
        try {
            audioManager.insert(newAudio);
        } catch (Exception e) {
            System.out.println("Error adding media file: " + e.getMessage());
        }
    }

    private void displayAudio() {
        System.out.println("All media files:");
        System.out.println(audioManager.read());
    }

    public void changeAccessCount() {
        System.out.println("Media file title:");
        String title = scanner.nextLine();
        MediaImpl.Audio audio = audioManager.getMediaList().stream().filter(a -> a.getAddress().equals(title)).findAny().orElse(null);
        if (audio != null) {
            audio.incrementAccessCount();
            System.out.println("Media file access count successfully updated.");
        } else {
            System.out.println("Media file does not exist.");
        }
    }

    public void delAudio() {
        System.out.println("Media file title:");
        String title = scanner.nextLine();
        MediaImpl.Audio audio = audioManager.getMediaList().stream().filter(a -> a.getAddress().equals(title)).findAny().orElse(null);
        if (audio != null) {
            audioManager.removeMedia(audio);
        } else {
            System.out.println("Media file does not exist.");
        }
    }

    public void delProducer() {
        System.out.println("Producer title:");
        String title = scanner.nextLine();

        if (title != null && !title.trim().isEmpty()) {
            try {
                audioManager.removeProducer(title);
                System.out.println("Producer successfully deleted.");
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Producer does not exist.");
        }
    }


    public void updateAudio() {
        System.out.println("Media file title:");
        String title = scanner.nextLine();

        if (title != null) {
            audioManager.update(title);
        }

    }
    private void saveAudioManager() {
        audioStateIO.saveState(audioManager);
    }

    private void loadAudioManager() {
       AudioManager loadedManager =     (AudioManager) audioStateIO.loadState();
        if (loadedManager != null) {
            audioManager = loadedManager;
            System.out.println(audioManager.read());
        }
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }
}
