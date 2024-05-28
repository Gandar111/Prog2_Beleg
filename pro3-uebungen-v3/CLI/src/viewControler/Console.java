package viewControler;

import MediaManager.AudioManagerImpl;
import MediaImpl.Audio;
import contract.Tag;
import contract.Uploader;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Handler;

public class Console {
    private static AudioManagerImpl audioManager;
    private static Scanner scanner = new Scanner(System.in);
    private static Operator currentCase;
    private static List<Handler> handlerList = new ArrayList<>();

    public static void main(String[] args) {
        initialize();
        execute();
    }

    private static void initialize() {
        List<Tag> tags = new ArrayList<>();
        Uploader uploader1 = new MediaImpl.Uploader("Produzent1") {
        };
        Uploader uploader2 = new MediaImpl.Uploader("Produzent2");
        HashMap<String, Uploader> producers = new HashMap<>();
        producers.put(uploader1.getName(), uploader1);
        producers.put(uploader2.getName(), uploader2);

        ArrayList<Audio> mediaList = new ArrayList<>();
        mediaList.add(new Audio(tags, 0, "address1", 500, uploader1, Duration.ofMinutes(3), BigDecimal.valueOf(9.99)));
        mediaList.add(new Audio(tags, 0, "address2", 700, uploader2, Duration.ofMinutes(4), BigDecimal.valueOf(14.99)));

        audioManager = new MediaManager.AudioManagerImpl(mediaList, producers, 2000);
    }

    private static void execute() {
        System.out.println("Please enter desired command to execute the program:");
        System.out.println(":c switch to INSERTION mode");
        System.out.println(":d switch to DELETE mode");
        System.out.println(":r switch to DISPLAY mode");
        System.out.println(":u switch to UPDATE mode");
        System.out.println(":e to quit the program");
        System.out.println("\nEnter command here: ");
        try (Scanner s = new Scanner(System.in)) {
            while (true) {
                try {
                    String input = s.nextLine().trim().toLowerCase();

                    if (input.startsWith(":")) {
                        switchCommand(input);
                    } else {
                        try {
                            executeCommand(input);
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

    private static void executeCommand(String input) throws Exception {
        switch (currentCase) {
            case INSERT:
                insertAudio();
                break;
            case REMOVE:
                delAudio();
                break;
            case READ:
                displayAudio();
                break;
            case UPDATE:
                changeAccessCount();
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
            case QUIT:
                currentCase = Operator.QUIT;
                System.out.println("Exit ..");
                break;
            default:
                System.out.println("Error");
        }
    }

    private static void insertAudio() {
        System.out.println("Mediadatei-Title:");
        String title = scanner.nextLine();
        System.out.println("Uploader-Name:");
        String uploaderName = scanner.nextLine();
        System.out.println("Size:");
        long size = Long.parseLong(scanner.nextLine());
        System.out.println("Duration (in seconds):");
        long durationSeconds = Long.parseLong(scanner.nextLine());

        Uploader uploader = audioManager.getProducer().get(uploaderName);
        if (uploader == null) {
            System.out.println("Uploader existiert nicht.");
            return;
        }

        Audio newAudio = new Audio(new ArrayList<>(), 0, title, size, uploader, Duration.ofSeconds(durationSeconds), BigDecimal.ZERO);
        try {
            audioManager.insert(newAudio);
            System.out.println("Mediadatei erfolgreich hinzugefügt.");
        } catch (Exception e) {
            System.out.println("Fehler beim Hinzufügen der Mediendatei: " + e.getMessage());
        }
    }

    private static void displayAudio() {
        System.out.println("Alle Mediendateien:");
        System.out.println(audioManager.read());
    }

    private static void changeAccessCount() {
        System.out.println("Mediadatei-Title:");
        String title = scanner.nextLine();
        Audio accessCount = audioManager.getMediaList().stream().filter(audio -> audio.getAddress().equals(title)).findAny().orElse(null);
        if (accessCount != null) {
            accessCount.incrementAccessCount();
            System.out.println("Mediadatei-Zugriffszähler wurde erfolgreich geändert.");
        } else {
            System.out.println("Mediadatei existiert nicht.");
        }
    }

    private static void delAudio() {
        System.out.println("Mediadatei-Title:");
        String title = scanner.nextLine();
        Audio audio = audioManager.getMediaList().stream().filter(a -> a.getAddress().equals(title)).findAny().orElse(null);
        if (audio != null) {
            audioManager.remove(audio);
            System.out.println("Mediadatei wurde erfolgreich gelöscht.");
        } else {
            System.out.println("Mediadatei existiert nicht.");
        }
    }
}
