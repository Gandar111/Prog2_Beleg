package Simulation;

import java.util.ArrayList;
import java.util.Random;

public class Simulation_1  extends Thread{
private String generateRandomAudioName(){

    ArrayList<String> randomAudioName = new ArrayList<>();
    String[] audioName = {
            "Bohemian Rhapsody - Queen",
            "Imagine - John Lennon",
            "Billie Jean - Michael Jackson",
            "Hotel California - Eagles",
            "Smells Like Teen Spirit - Nirvana",
            "Like a Rolling Stone - Bob Dylan",
            "Shape of You - Ed Sheeran",
            "Stairway to Heaven - Led Zeppelin",
            "Hey Jude - The Beatles",
            "Rolling in the Deep - Adele",
            "Wonderwall - Oasis"
    };
Random rand = new Random();
int index = rand.nextInt(randomAudioName.size());
return randomAudioName.get(index);

}

    @Override
    public void run() {
        System.out.println("Run Methode Nummer:"+getName());
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Beende Methode Nummer:"+getName());

    }

    public Simulation_1(String name) {
        super(name);


    }
}
