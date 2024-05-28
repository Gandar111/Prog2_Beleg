package contract;

public interface Video extends MediaContent,Uploadable{

    int getResolution();   // Diese Methode gibt die Auflösung des Videos zurück
}
