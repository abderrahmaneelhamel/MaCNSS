package service;

import dao.UserDAOImpl;
import model.Admin;
import model.Agent;
import model.Patient;
import util.tools;
import Enum.UserRole;

import java.util.Scanner;

public class AuthenticationService {
    private final UserDAOImpl userDAO;

    public AuthenticationService(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }

    public Agent agentAuth(Scanner scanner) {
        System.out.println("=== User Sign-In ===");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        // Validate user's email
        while (tools.isValidEmailFormat(email)) {
            System.out.println("Invalid email format. Please enter a valid email:");
            email = new Scanner(System.in).nextLine();
        }
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        // Validate user's password
        while (tools.isValidPassword(password)) {
            System.out.println("Invalid password format. Password must be at least 8 characters long without spaces:");
            password = new Scanner(System.in).nextLine();
        }

        Agent authenticatedAgent = userDAO.authenticateAgent(email, password);

        if (authenticatedAgent != null) {
            System.out.println("Sign-In successful!");
            return authenticatedAgent;
        } else {
            System.out.println("Sign-In failed. Invalid email or password.");
            return null;
        }
    }

    public Patient patientAuth(Scanner scanner) {
        System.out.println("=== User Sign-In ===");
        System.out.print("Enter your matricule: ");
        int matricule = scanner.nextInt();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        // Validate user's password
        while (tools.isValidPassword(password)) {
            System.out.println("Invalid password format. Password must be at least 8 characters long without spaces:");
            password = new Scanner(System.in).nextLine();
        }

        Patient authenticatedPatient = userDAO.authenticatePatient(matricule, password);

        if (authenticatedPatient != null) {
            System.out.println("Sign-In successful!");
            return authenticatedPatient;
        } else {
            System.out.println("Sign-In failed. Invalid email or password.");
            return null;
        }
    }
    public Admin adminAuth(Scanner scanner) {
        System.out.println("=== User Sign-In ===");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        // Validate user's email
        while (tools.isValidEmailFormat(email)) {
            System.out.println("Invalid email format. Please enter a valid email:");
            email = new Scanner(System.in).nextLine();
        }
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        // Validate user's password
        while (tools.isValidPassword(password)) {
            System.out.println("Invalid password format. Password must be at least 8 characters long without spaces:");
            password = new Scanner(System.in).nextLine();
        }

        if (email.equals("admin@gmail.com") && password.equals("admin")) {
            System.out.println("Sign-In successful!");
            return new Admin(1,"admin", "admin@gmail.com", "admin");
        } else {
            System.out.println("Sign-In failed. Invalid email or password.");
            return null;
        }
    }
    public void addAgent(Scanner scanner) {
        System.out.println("=== Agent Registration ===");

        // Collect admin information
        System.out.print("Enter Agent name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Agent email: ");
        String email = scanner.nextLine();
        // Validate user's email
        while (tools.isValidEmailFormat(email)) {
            System.out.println("Invalid email format. Please enter a valid email:");
            email = new Scanner(System.in).nextLine();
        }

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // Validate user's password
        while (tools.isValidPassword(password)) {
            System.out.println("Invalid password format. Password must be at least 8 characters long without spaces:");
            password = new Scanner(System.in).nextLine();
        }

        // Create an admin user with role 1 (admin)
        Agent Agent = new Agent(0,name, email, password);

        // Register the admin user using the userDAO
        if (userDAO.addUser(Agent)) {
            System.out.println("Agent registration successful!");
        } else {
            System.out.println("Agent registration failed. Please check the input.");
        }
    }
    public void addPatient(Scanner scanner) {
        System.out.println("=== Patient Registration ===");

        // Collect admin information
        System.out.print("Enter Patient name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Patient email: ");
        String email = scanner.nextLine();
        // Validate user's email
        while (tools.isValidEmailFormat(email)) {
            System.out.println("Invalid email format. Please enter a valid email:");
            email = new Scanner(System.in).nextLine();
        }

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // Validate user's password
        while (tools.isValidPassword(password)) {
            System.out.println("Invalid password format. Password must be at least 8 characters long without spaces:");
            password = new Scanner(System.in).nextLine();
        }
        int matricule = (int) (Math.random());
        Patient patient = new Patient(0,name, email, password, matricule);

        if (userDAO.addUser(patient)) {
            System.out.println("Patient registration successful!");
        } else {
            System.out.println("Patient registration failed. Please check the input.");
        }
    }
}
