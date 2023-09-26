package service;
import dao.UserDAOImpl;
import model.User;
import dao.RefundFileDAOImpl;

import javax.sound.midi.Soundbank;
import java.sql.Connection;
import java.util.Scanner;

public class AgentService {
    private final Connection connection;
    private final FileService FileService;
    private final RefundFileDAOImpl refundFileDAOImpl;



    public AgentService(Connection connection) {
        this.connection = connection;
        this.FileService = new FileService(connection);
        this.refundFileDAOImpl= new RefundFileDAOImpl(connection);
    }

    public void showMenu(User authenticatedUser) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nAgents Menu:");
            System.out.println("1. Add a New File");
            System.out.println("2. Edit a File");
            System.out.println("3. Search for a File");
            System.out.println("4. Add a new Patient");
            System.out.println("5. Logout");

            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> FileService.addFile(scanner);
                    case 2 -> FileService.editFile(scanner);
                    case 3 -> {
                        System.out.println("enter the patient matricule: ");
                        int matricule = scanner.nextInt();
                        FileService.getClientFiles(matricule);
                    }
                    case 4 -> new AuthenticationService(new UserDAOImpl(this.connection)).addPatient(scanner);
                    case 5 -> {
                        return; // Logout
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }



}

