package service;
import model.User;

import java.util.Scanner;

public class AdminService {


    public AdminService() {

    }

    public void displayMenu(User authenticatedUser) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome, " + authenticatedUser.getName() + "!");

        while (true) {
            System.out.println("Admin Menu:");
            System.out.println("1. Add an Agent");
            System.out.println("2. Exit");

            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> ;
                    case 2 -> {
                        System.out.println("Thank you for using the CNSS service. Goodbye!");
                        return;
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

