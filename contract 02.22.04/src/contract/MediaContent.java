package contract;

import java.util.Collection;

public interface MediaContent {
    String getAddress();
    Collection<Tag> getTags();
    long getAccessCount();       //Gibt den Zugriffszähler für das Audio-Medium zurück
    void incrementAccessCount(); //Erhöht die Zugriffsanzahl
    long getSize();
}
