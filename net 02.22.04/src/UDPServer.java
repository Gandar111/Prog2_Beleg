import MediaImpl.Audio;
import MediaImpl.Uploader;
import MediaManager.AudioManager;
import contract.Tag;
import observerImpl.Subjekt;
import viewControler.Operator;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class UDPServer {
        private final int SERVER_PORT = 8001;
        private int totalCapacity;
        private AudioManager audioManager ;
        private final Subjekt subjekt = new Subjekt();
        private Operator currentCase;

        public UDPServer(int totalCapacity) {
            this.totalCapacity = totalCapacity;
            this.audioManager=new AudioManager(totalCapacity);
        }

        public void ServerStart() throws IOException {
            try {
                // Step 1 : Create a socket to listen at port 1234
                DatagramSocket datagramSocketServer = new DatagramSocket(SERVER_PORT);
                byte[] receive = new byte[65535];
                System.out.println("Server is listing on port "+SERVER_PORT);

                DatagramPacket datagramPacket = null;
                while (true) {

                    // Step 2 : create a DatgramPacket to receive the data.
                    datagramPacket = new DatagramPacket(receive, receive.length);

                    // Step 3 : revieve the data in byte buffer.
                    datagramSocketServer.receive(datagramPacket);
                    new Thread(new RequestHandler(datagramSocketServer,datagramPacket)).start();// viele Anfragen an dem Server ankommen dann kann er die Anfragen gleichzeitig bearbeiten

                }

            } catch (Exception e) {
                e.printStackTrace();

            }}

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



// eine Unterklasse um die Request von dem Server zu bearbeiten
    public class RequestHandler implements Runnable {
        private final DatagramSocket datagramSocket; // die ermöglicht die Verbindung mit dem Server
        private final DatagramPacket datagramPacket;

        public RequestHandler(DatagramSocket datagramSocket, DatagramPacket dpReceive) {
            this.datagramSocket = datagramSocket;
            datagramPacket = dpReceive;
        }

        // hier wird die Message von dem Client gelesen und bearbeitete
        @Override
        public void run() {
            try {
                String request = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                String response = handleRequest(request);
                byte[] serverResponseBytes = response.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(serverResponseBytes, serverResponseBytes.length,
                        datagramPacket.getAddress(), datagramPacket.getPort());
                datagramSocket.send(sendPacket);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        }
    }


