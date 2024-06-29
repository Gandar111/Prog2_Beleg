import java.io.IOException;
import java.util.Scanner;

public class ServerMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Starting servers...");

        // Start UDP server in a new thread
        Thread udpThread = new Thread(() -> {
            try {
                new UDPServer(3000).ServerStart();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Start TCP server in a new thread
        Thread tcpThread = new Thread(() -> {
                new TCPServer(3000).start();

        });

        udpThread.start();
        tcpThread.start();

        System.out.println("UDP and TCP servers are running.");
    }
}
