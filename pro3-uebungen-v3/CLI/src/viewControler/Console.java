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

public class Console {
    private AudioManagerImpl audioManager;
    private final Scanner scanner = new Scanner(System.in);
    private static Operator currentCase;

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
        audioManager = new MediaManager.AudioManagerImpl(mediaList, producers, 2000);
    }

    public void showMenu() {
        System.out.println("Please enter the desired command to execute the program:");
        System.out.println(":c switch to INSERTION mode");
        System.out.println(":d switch to DELETE mode");
        System.out.println(":r switch to DISPLAY mode");
        System.out.println(":u switch to UPDATE mode");
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

    private void insertAudio() {
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
            System.out.println("Media file successfully added.");
        } catch (Exception e) {
            System.out.println("Error adding media file: " + e.getMessage());
        }
    }

    private void displayAudio() {
        System.out.println("All media files:");
        System.out.println(audioManager.read());
    }

    private void changeAccessCount() {
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

    private void delAudio() {
        System.out.println("Media file title:");
        String title = scanner.nextLine();
        MediaImpl.Audio audio = audioManager.getMediaList().stream().filter(a -> a.getAddress().equals(title)).findAny().orElse(null);
        if (audio != null) {
            audioManager.remove(audio);
            System.out.println("Media file successfully deleted.");
        } else {
            System.out.println("Media file does not exist.");
        }
    }
}
