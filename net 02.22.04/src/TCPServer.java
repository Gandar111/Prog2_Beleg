import MediaImpl.Audio;
import MediaImpl.Uploader;
import MediaManager.AudioManager;
import contract.Tag;
import observerImpl.Subjekt;
import viewControler.Operator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TCPServer extends Thread {
    private static final int SERVER_PORT = 8000;
    private int totalCapacity;
    private AudioManager audioManager ;
    private final Subjekt subjekt = new Subjekt();
    private Operator currentCase;

    public TCPServer(int totalCapacity) {
        this.totalCapacity = totalCapacity;
        this.audioManager=new AudioManager(totalCapacity);
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server is listening on port " + SERVER_PORT);

            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler extends Thread {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

                String request;

                while ((request = reader.readLine()) != null) {
                    String response = handleRequest(request);
                    writer.println(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Die Methode bearbeitet die Antwort von dem Client
        private String handleRequest(String request) {
            if (request != null) {
                String command = request.split(",")[0].toUpperCase();
                try {
                    currentCase = Operator.valueOf(command);
                } catch (IllegalArgumentException e) {
                    return "Invalid command";
                }

                switch (currentCase) {
                    case INSERT:
                        return handleInsertAudioRequest(request);
                    case REMOVE:
                        return handleRemoveAudioRequest(request);
                    case READ:
                        return handleDisplayAudioRequest(request);
                    case UPDATE:
                        return handleUpdateAudioRequest(request);
                    case PERSISTENCE:
                        return "Persistence logic not implemented yet.";
                    case QUIT:
                        System.exit(0);
                        return "Server shutting down.";
                    default:
                        return "Invalid operation.";
                }
            }
            return "Unknown request";
        }
    }
    public String handleUpdateAudioRequest(String request) {
        String[] parts = request.split(",");
        if (parts.length == 2) {
            String title = parts[1].toLowerCase().trim().toString();
            try {
                //audioManager.insert(newAudio);
                updateAudio(title);
                return "Media file successfully updated: " +title;
            } catch (Exception e) {
                return "Error updating media file: " + e.getMessage();
            }
        } else {
            return "Invalid request format.";
        }
    }

    public String handleDisplayAudioRequest(String request){
        return "All media files: "+ audioManager.read();}
    public String handleRemoveAudioRequest(String request) {
        String[] parts = request.split(",");
        if (parts.length == 2) {
            String title = parts[1];
            Audio audio = audioManager.getMediaList().stream()
                    .filter(a -> a.getAddress().equals(title))
                    .findAny().orElse(null);

            if (audio != null) {
                try {
                    audioManager.removeMedia(audio);
                    subjekt.notifyObservers();
                    return "Media file successfully deleted: " + title;
                } catch (Exception e) {
                    return "Error deleting media file: " + e.getMessage();
                }
            } else {
                return "Media file does not exist: " + title;
            }
        } else {
            return "Invalid request format.";
        }
    }


    public String handleInsertAudioRequest(String request) {
        String[] parts = request.split(",");
        if (parts.length == 5) {
            String title = parts[1].toLowerCase().trim();
            String uploaderName = parts[2];
            long size = Long.parseLong(parts[3]);
            long durationSeconds = Long.parseLong(parts[4]);

            Uploader uploader = new Uploader(uploaderName);
            Audio newAudio = new Audio(new ArrayList<>(), 0, title, size, uploader,
                    Duration.ofSeconds(durationSeconds), BigDecimal.ZERO);
            System.out.println(title +", "+ uploaderName +", "+ size +", "+ durationSeconds);
            try {
                //audioManager.insert(newAudio);
                insertAudio(newAudio);
                return "Media file successfully added: " +title +", "+ uploaderName +", "+ size +", "+ durationSeconds;
            } catch (Exception e) {
                return "Error adding media file: " + e.getMessage();
            }
        } else {
            return "Invalid request format.";
        }
    }
    // Methode zum Hinzufügen eines Audio-Objekts
    public void insertAudio(Audio audio) throws Exception {
        if (audio == null) {
            throw new IllegalArgumentException("Audio object cannot be null");
        }
        audioManager.insert(audio);
        subjekt.notifyObservers();
    }
    public void updateAudio(String titel) throws Exception {
        audioManager.update(titel);
        subjekt.notifyObservers();
    }


}
