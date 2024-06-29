package viewControler;
import java.util.Scanner;

public class Console  {
    private static Operator currentCase;
    private final Scanner scanner = new Scanner(System.in);
    // Zeigt dem Benutzer ein Menü mit verfügbaren Befehlen an und der Benutzer kann zwischen verschiedenen Modi wechseln
    public void showMenu() {
        System.out.println("Please enter the desired command to execute the program:");
        System.out.println(":c switch to INSERTION mode");
        System.out.println(":d switch to DELETE mode");
        System.out.println(":r switch to DISPLAY mode");
        System.out.println(":u switch to UPDATE mode");
        System.out.println(":s switch to Persistence mode");
        System.out.println(":e to quit the program");
        System.out.println("\nEnter command here: ");
    }
        // diese Methode führt das Hauptmenü und wartet auf Benutzereingabe.
    public void execute() {
        showMenu();
        try (Scanner s = new Scanner(System.in)) {
            while (true) {
                try {
                    String input = s.nextLine().trim().toLowerCase();

                    if (input.startsWith(":")) {
                        switchCommand(input);
                    } else {
                        try {
                            executeCommand();
                            execute();
                        } catch (Exception e) {
                            System.out.println("Wrong option");
                        }
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("Invalid command! Please enter a valid command.");
                }
            }
        }
    }

    private void executeCommand() throws Exception {
        switch (currentCase) {
            case INSERT:
                insertAudio( );
                break;
            case REMOVE:
                delAudio();
                break;
            case READ:
                displayAudio();
                break;
            case UPDATE:
                updateAudio();
                break;
            case PERSISTENCE:
                System.out.println("type command :\n 1.Save JOS \n 2.Load JOS\n");

                String choice = scanner.nextLine().trim();
                if ("1".equals(choice)) {
                    saveAudioManager();
                } else if ("2".equals(choice)) {
                    loadAudioManager();
                } else {
                    System.out.println("Invalid choice for persistence operations.");
                }
                break;
            case QUIT:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input - execute command");
        }
    }



    private static void switchCommand(String input) {
        Command command = new Command(input);

        switch (command.operator) {
            case INSERT:
                currentCase = Operator.INSERT;
                System.out.println("Switched to INSERTION mode");
                break;
            case REMOVE:
                currentCase = Operator.REMOVE;
                System.out.println("Switched to DELETE mode");
                break;
            case READ:
                currentCase = Operator.READ;
                System.out.println("Switched to DISPLAY mode");
                break;
            case UPDATE:
                currentCase = Operator.UPDATE;
                System.out.println("Switched to UPDATE mode");
                break;
            case PERSISTENCE:
                currentCase = Operator.PERSISTENCE;
                System.out.println("Switched to " + command.operator + " mode");
                break;
            case QUIT:
                currentCase = Operator.QUIT;
                System.out.println("Exit ..");
                break;
            default:
                System.out.println("Error");
        }
    }


    public void insertAudio() {

    }
    public void displayAudio() {
      }
    public void delAudio() {

    }
    public void updateAudio() {

    }
    public void loadAudioManager() {
    }

    public void saveAudioManager() {

    }
}



