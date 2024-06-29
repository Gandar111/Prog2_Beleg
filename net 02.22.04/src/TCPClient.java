import MediaManager.AudioManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import MediaImpl.Audio;
import contract.Tag;
import contract.Uploader;
import jos.AudioStateIO;
import viewControler.Console;
import viewControler.Operator;
import viewControler.Command;

public class TCPClient extends Console{
    private AudioManager audioManager;
    private final Scanner scanner = new Scanner(System.in);
    private static Operator currentCase;
    private final String filename = "/Users/ghamdan/Library/Mobile Documents/com~apple~CloudDocs/Desktop/Universiteat/Pro-3/Prog3_ss_24/pro3-uebungen-v3/AudioManager.ser";
    private final AudioStateIO audioStateIO= new AudioStateIO(filename);
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8000;
    private PrintWriter writer;
    private BufferedReader serverReader;




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
                insertAudio( );
                break;
            case REMOVE:
                delAudio();
                break;
            case READ:
                displayAudio();
                break;
            case UPDATE:
                updateAudio();
                break;
            case PERSISTENCE:
                System.out.println("type command :\n 1.Save JOS \n 2.Load JOS\n");

                String choice = scanner.nextLine().trim();
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
    public void start() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Initialisierung von writer außerhalb des try-with-resources Blocks
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.serverReader = serverReader;

            System.out.println("Connected to server. Type 'exit' to quit.");
            execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertAudio() {
        System.out.println("Media File title:");
        String title = scanner.nextLine();
        System.out.println("Uploader name:");
        String uploaderName = scanner.nextLine();
        System.out.println("Size:");
        long size = Long.parseLong(scanner.nextLine());
        System.out.println("Duration (in seconds):");
        long durationSeconds = Long.parseLong(scanner.nextLine()); //Dauer eines Audios
        writer.println("INSERT"+ "," + title + "," + uploaderName + "," + size + "," + durationSeconds);
        try {
            String response = serverReader.readLine();
            System.out.println(response);                   // um die Antwort von dem Server zu lesen
        } catch (Exception e) {
            System.out.println("Error adding media file: " + e.getMessage());
        }
    }

    public void displayAudio() {
        writer.println("READ");
        try {
            String response = serverReader.readLine();
            System.out.println(response);                   // um die Antwort von dem Server zu lesen
        } catch (Exception e) {
            System.out.println("Error display media" + e.getMessage());
        }

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
        writer.println("REMOVE"+ "," + title);
        try {
            String response = serverReader.readLine();
            System.out.println(response);                   // um die Antwort von dem Server zu lesen
        } catch (Exception e) {
            System.out.println("Error remove media file: " + e.getMessage());
        }
    }
    public void updateAudio() {
        System.out.println("Media file Title:");
        String title = scanner.nextLine();

        if (title!= null) {
            writer.println("UPDATE" + "," +title);

            try {
                String response = serverReader.readLine();
                System.out.println(response); // Um die Antwort vom Server zu lesen
            } catch (Exception e) {
                System.out.println("Error updating media file: " + e.getMessage());
            }
        } else {
            System.out.println("Media file not found.");
        }
    }

    public void saveAudioManager() {
        audioStateIO.saveState(audioManager);
    }

    public void loadAudioManager() {
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
