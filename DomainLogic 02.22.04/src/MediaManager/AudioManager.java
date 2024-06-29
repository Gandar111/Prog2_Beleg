package MediaManager;

import MediaImpl.Audio;

public class AudioManager extends MediaManager<Audio> {
    public AudioManager(int totalCapacity) {
        super(totalCapacity);
    }
}