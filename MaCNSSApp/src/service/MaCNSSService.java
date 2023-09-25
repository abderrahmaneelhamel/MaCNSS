package  service;

import db.DatabaseConnection;
import dao.UserDAOImpl;
import model.Admin;
import model.Agent;
import model.Patient;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class MaCNSSService {
    private static MaCNSSService instance;
    private final AuthenticationService authService;
    private final AgentService AgentService;
    private final PatientService PatientService;
    private final AdminService AdminService;
    private final Connection connection;

    private MaCNSSService() throws SQLException {
        connection = DatabaseConnection.getInstance().getConnection();
        authService = new AuthenticationService(new UserDAOImpl(connection));
        AgentService = new AgentService(connection);
        PatientService = new PatientService(connection);
        AdminService = new AdminService(connection);
    }

    public static MaCNSSService getInstance() throws SQLException {
        if (instance == null) {
            instance = new MaCNSSService();
        }
        return instance;
    }
    public void closeConnection() {
        try{
            this.connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        // Display a welcome message and options to log in or sign up
        System.out.println("Welcome to the MaCNSS Management System!");
        System.out.println("Log in as :");
        System.out.println("1-Admin");
        System.out.println("2-Agent");
        System.out.println("3-Patient");
        System.out.print("Enter your choice: ");
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> {
                    Admin admin = authService.adminAuth(scanner);
                    if (admin != null){
                        AdminService.displayMenu(admin);
                    }
                }
                case 2 -> {
                    Agent agent = authService.agentAuth(scanner);
                    if (agent != null){
                        AgentService.showMenu(agent);
                    }
                }
                case 3 -> {
                    Patient patient = authService.patientAuth(scanner);
                    if (patient != null){
                        PatientService.showMenu(patient);
                    }
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } else {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // Consume the invalid input
        }
        // Close resources
        scanner.close();
        closeConnection();
    }
}