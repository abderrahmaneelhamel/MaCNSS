package service;
import model.Patient;
import model.User;

import java.sql.Connection;
import java.util.Scanner;

public class PatientService {
    private final Connection connection;

    private FileService FileService;


    public PatientService(Connection connection,FileService FileService) {
        this.connection = connection;
        this.FileService = FileService;
    }
    public PatientService(Connection connection) {
        this.connection = connection;
    }

    public void showMenu(User authenticatedUser) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nClient Menu:");
            System.out.println("1. check your files state");
            System.out.println("2. Logout");

            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> FileService.checkClientFiles(authenticatedUser);
                    case 2 -> {
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
    public Patient createPatient() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter patient name: ");
        String name = scanner.nextLine();

        System.out.println("Enter patient email: ");
        String email = scanner.nextLine();

        System.out.println("Enter patient password: ");
        String password = scanner.nextLine();

        System.out.println("Enter patient matricule: ");
        int matricule = scanner.nextInt();

        return new Patient(6, name, email, password, matricule);
    }

}

