package  service;

import dao.DatabaseConnection;
import dao.UserDAOImpl;
import model.User;
import util.tools;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class MaCNSSService {
    private static MaCNSSService instance;
    private final AuthenticationService authService;
    private final AgentService AgentService;
    private final ClientService ClientService;

    private final AdminService AdminService;
    private final Connection connection;

    private MaCNSSService() throws SQLException {
        connection = DatabaseConnection.getInstance().getConnection();
        authService = new AuthenticationService(new UserDAOImpl(connection));
        AgentService = new AgentService(connection);
        ClientService = new ClientService(connection);
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

                // User authentication
                User authenticatedUser = authService.signIn(scanner);

                if (authenticatedUser != null) {
                    // Determine user role
                    if (authenticatedUser.getId() == 1) {
                        // Admin menu
                        AdminService.displayMenu(authenticatedUser);
                    } else if (authenticatedUser.getRole() == 1) {
                        // Agent menu
                        AgentService.showMenu(authenticatedUser);
                    } else if (authenticatedUser.getRole() == 2) {
                        // Client menu
                        ClientService.showMenu(authenticatedUser);
                    } else {
                        System.out.println("Invalid user role. Exiting...");
                    }
                } else {
                    System.out.println("Authentication failed. Please try again.");
                }
        // Close resources
        scanner.close();
        closeConnection();
    }

}
