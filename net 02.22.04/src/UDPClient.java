import jos.AudioStateIO;
import viewControler.Console;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPClient extends Console {
    private DatagramSocket socket;
    private InetAddress ip;
    private AudioStateIO audioStateIO;
    private static final int SERVER_PORT = 8001;
    private final Scanner scanner = new Scanner(System.in);
        public void start() throws IOException {
                socket = new DatagramSocket();
                ip = InetAddress.getLocalHost();
            System.out.println("Connected to server. Type 'exit' to quit.");
            execute();
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
        String message = "INSERT"+ "," + title + "," + uploaderName + "," + size + "," + durationSeconds;
        try {
            senden(message);
        } catch (Exception e) {
            System.out.println("Error sending media file: " + e.getMessage());
        }
        try {
            receive(); // um die Antwort von dem Server zu lesen
        } catch (Exception e) {
            System.out.println("Error adding media file: " + e.getMessage());
        }
    }
// die Methode schickt eine Nachricht an den Server
public void senden(String message) throws IOException {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, ip, SERVER_PORT);
            socket.send(packet);

            }
public void displayAudio() {
    try {
        senden("READ");
    } catch (Exception e) {
        System.out.println("Error reading media files: " + e.getMessage());
    }
        try {
            String response = receive();
            System.out.println(response);                   // um die Antwort von dem Server zu lesen
        } catch (Exception e) {
            System.out.println("Error display media" + e.getMessage());
        }}
public void delAudio() {
        System.out.println("Media file title:");
        String title = scanner.nextLine();
        try {
            senden("REMOVE"+ "," + title);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            String response = receive();
            System.out.println(response);                   // um die Antwort von dem Server zu lesen
        } catch (Exception e) {
            System.out.println("Error remove media file: " + e.getMessage());
        }
    }
public void updateAudio() {
        System.out.println("Media file Title:");
        String title = scanner.nextLine();

        if (title!= null) {
            try {
                senden("UPDATE" + "," +title);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                String response = receive();
                System.out.println(response); // Um die Antwort vom Server zu lesen
            } catch (Exception e) {
                System.out.println("Error updating media file: " + e.getMessage());
            }
        } else {
            System.out.println("Media file not found.");
        }
    }

public void saveAudioManager() {
    //audioStateIO.saveState(audioManager);
}

    @Override
    public void showMenu() {
        super.showMenu();
    }

    @Override
    public void execute() {
        super.execute();
    }

    public void loadAudioManager() {
        /*AudioManager loadedManager =     (AudioManager) audioStateIO.loadState();
        if (loadedManager != null) {
            audioManager = loadedManager;
            System.out.println(audioManager.read());
        }*/
    }




// die Methode empfängt die Nachricht von dem Server und wandelt das in Zeichenkette um
public String receive() throws IOException {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String message = new String(packet.getData());
            return message;
            }
            public void close () throws IOException {
            socket.close();
            }
        }
