package service;
import dao.UserDAOImpl;
import model.User;

import java.sql.Connection;
import java.util.Scanner;

public class AgentService {
    private final Connection connection;


    public AgentService(Connection connection) {
        this.connection = connection;
    }

    public void showMenu(User authenticatedUser) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nAgents Menu:");
            System.out.println("1. Add a New File");
            System.out.println("2. Edit a File");
            System.out.println("3. Delete a File");
            System.out.println("4. Search for a File");
            System.out.println("5. Add a new Patient");
            System.out.println("6. Logout");

            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> FileService.AddFile();
                    case 2 -> FileService.editBook();
                    case 3 -> FileService.deleteBook();
                    case 4 -> FileService.search();
                    case 5 -> new AuthenticationService(new UserDAOImpl(connection)).addAdmin(scanner);
                    case 6 -> {
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

