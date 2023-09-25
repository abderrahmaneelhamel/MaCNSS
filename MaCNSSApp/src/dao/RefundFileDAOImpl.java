package dao;

import model.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import db.DatabaseConnection;
import java.util.Date;
import Enum.RefundFileStatus;
public class RefundFileDAOImpl implements RefundFileDAO {
    private final Connection connection;

    private List<Document> documents; // Initialize the list here
    public RefundFileDAOImpl(Connection connection) {
        this.connection = connection;
        this.documents = new ArrayList<>(); // Initialize the list in the constructor
    }

    public boolean addRefundFileToDatabase(RefundFile file) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (connection!=null) {
            String sql = "INSERT INTO files (user_id, status, total_refund, creation_date) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, file.getPatientId());
                preparedStatement.setObject(2, RefundFileStatus.pending); // Set the initial status to "pending"
                preparedStatement.setDouble(3, file.getTotalRefund());
                preparedStatement.setString(4, file.getCreationDate());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int fileId = generatedKeys.getInt(1);
                        file.setId(fileId);
                    }

                    return true;
                }
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
    }


    public List<RefundFile> getAllFiles() {
        String sql = "SELECT * FROM files;";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            List<RefundFile> fileList = new ArrayList<>();
            while (resultSet.next()) {
                RefundFileStatus status = RefundFileStatus.valueOf(resultSet.getString("status").toUpperCase());
                // Use RefundFileStatus.valueOf to convert the status string to enum
                RefundFile file = new RefundFile(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("creation_date"),
                        resultSet.getDouble("total_refund"),
                        status
                                );
                fileList.add(file);
            }
            return fileList;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
            return null;
        }
    }


    public List<RefundFile> getFileByUser(User patient) {
        String sql = "SELECT * FROM files WHERE user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, patient.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<RefundFile> fileList = new ArrayList<>();
            while (resultSet.next()) {
                RefundFileStatus status = RefundFileStatus.valueOf(resultSet.getString("status").toUpperCase());
                RefundFile file = new RefundFile(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("creation_date"),
                        resultSet.getDouble("total_refund"),
                        status
                                );
                fileList.add(file);
            }
            return fileList;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
            return null;
        }
    }

    public boolean deleteFile(int id) {
        String sql = "DELETE FROM files WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateFile(RefundFile file) {
        String sql = "UPDATE file SET status = ?, total_refund = ?, creation_date = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
           // preparedStatement.setString(1, file.getStatus());
            preparedStatement.setDouble(2, file.getTotalRefund());
            preparedStatement.setString(3, file.getCreationDate());
            preparedStatement.setInt(4, file.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public void addFile()  {
        DocumentDAOImpl documentDAO = new DocumentDAOImpl();
        List<Document> selectedDocuments = new ArrayList<>();

        // Create a new patient
        PatientDAOImpl patientDAOImpl = new PatientDAOImpl(connection);
        Patient patient = patientDAOImpl.createPatient();

        // Add the patient to the database
        if (patientDAOImpl.addPatientToDatabase(patient)) {
            System.out.println("Patient added with ID: " + patient.getId());
        } else {
            System.out.println("Failed to add the patient.");
            return; // Exit the method if patient creation fails
        }

        // The part where you add documents to the selectedDocuments list goes here

        while (true) {
            System.out.println("Please select the type of document to add to the refund file:");
            System.out.println("1. Medicines");
            System.out.println("2. Radiography");
            System.out.println("3. Medical Examination");
            System.out.println("4. Medical Analysis");
            System.out.println("5. Finish Adding Documents");
            System.out.print("Enter your choice: ");
            Scanner scanner = new Scanner(System.in); // Initialize the scanner

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    Medicine medicine = documentDAO.createMedicineDocument();
                    if (medicine != null){
                        Document document = medicine;
                        selectedDocuments.add(document);
                    }
                    break;
                // Implement cases for other document types (2, 3, 4)
                case 5:
                    if (selectedDocuments.isEmpty()) {
                        System.out.println("No documents selected. Please add at least one document.");
                    } else {
                        addDocuments(selectedDocuments);
                        System.out.println("Documents added to the refund file.");
                        System.out.println("Total documents: " + selectedDocuments.size());
                        calculateRefundForDocuments(selectedDocuments);
                        System.out.println("Total Amount: " + calculateRefundForDocuments(selectedDocuments));

                        // Call the method to add the refund file to the database
                        RefundFile refundFile = new RefundFile(patient.getId(), RefundFileStatus.pending, calculateRefundForDocuments(selectedDocuments), getCurrentDate());
                        if (addRefundFileToDatabase(refundFile)) {
                            System.out.println("Refund file added to the database.");
                        } else {
                            System.out.println("Failed to add the refund file to the database.");
                        }
                        return; // Exit the method when done adding documents
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    public void addDocuments(List<Document> documentsToAdd) {
        documents.addAll(documentsToAdd); // Add the documents to the list
    }

    public double calculateRefundForDocuments(List<Document> documents) {
        double totalRefund = 0.0;

        for (Document document : documents) {
            // Check if the document is a Medicine
            if (document instanceof Medicine) {
                Medicine medicine = (Medicine) document;
                double price = medicine.getPrice();
                double percentage = medicine.getPercentageAsDouble();

                // Calculate the refund for this medicine
                double medicineRefund = price * (percentage / 100.0);

                // Add the medicine refund to the total refund
                totalRefund += medicineRefund;
            }
         }
         return totalRefund;
    }
    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }


}
