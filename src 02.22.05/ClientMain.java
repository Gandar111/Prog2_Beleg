import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        System.out.println("select the connection type: ");
        System.out.println("1: TCP");
        System.out.println("2: UDP");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                TCPClient tcpClient = new TCPClient();
                tcpClient.start();
                break;
            case "2":
                UDPClient udpClient = new UDPClient();
                udpClient.start();
        }

        TCPClient client = new TCPClient();
        client.start();
    }
}
