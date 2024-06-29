package jos;

import java.io.*;
import MediaManager.MediaManager;

import javax.print.attribute.standard.Media;

public class AudioStateIO implements Serializable {
    private final File file;

    public AudioStateIO(String file) {
        this.file = new File(file);
    }

    public void saveState(MediaManager audioManager) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(audioManager);
            System.out.println("State successfully saved.");
        } catch (IOException e) {
            System.out.println("Error saving state " + e.getMessage());
        }
    }

    public MediaManager loadState() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (MediaManager) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state: " + e.getMessage());
            return null;
        }
    }
}
