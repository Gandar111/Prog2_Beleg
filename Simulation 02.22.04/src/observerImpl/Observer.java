package observerImpl;

import MediaManager.AudioManager;

public class Observer implements interfaces.Observer {
    private AudioManager audioManager;
    private String audioCapacity;

    public Observer(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    @Override
    public void update() {

        if (audioCapacity != null && !audioCapacity.isEmpty()) {
            System.out.println(audioCapacity);
        }

        checkCapacity();
    }

    public void checkCapacity() {
        long capacity = audioManager.getCurrentCapacity();
        double currentSize = audioManager.getMediaList().size();
        double utilizationPercentage=100;
         utilizationPercentage = (currentSize / capacity * 100);
        utilizationPercentage = Math.round(utilizationPercentage * 100.0) ;
        audioCapacity = "Capacity is " +utilizationPercentage+"%";
        if (utilizationPercentage >= 90) {
            audioCapacity = "Warning: Capacity exceeded 90%!";
        }
    }
}
