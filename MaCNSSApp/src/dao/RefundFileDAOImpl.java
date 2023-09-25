package dao;

import model.Medicine;
import model.RefundFile;
import model.User;
import model.Document;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileDAOImpl implements FileDAO {
    private final Connection connection;


    public FileDAOImpl(Connection connection) {
        this.connection = connection;
    }

    public boolean createFile(RefundFile file) {
        String sql = "INSERT INTO file (user_id,status,total_refund,creation_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, file.getPatientId());
            preparedStatement.setString(2, file.getStatus());
            preparedStatement.setDouble(3, file.getTotalRefund());
            preparedStatement.setString(4, file.getCreationDate());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<RefundFile> getAllFiles() {
        String sql = "SELECT * FROM file;";
        try (Statement Statement = connection.createStatement()) {
            ResultSet resultSet = Statement.executeQuery(sql);
            List<RefundFile> fileList = new ArrayList<>();
            while (resultSet.next()) {
                RefundFile file = new RefundFile(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("status"),
                        resultSet.getDouble("total_refund"),
                        resultSet.getString("creation_date"));
                fileList.add(file);
            }
            return fileList;
        } catch (SQLException e) {
            return null;
        }
    }

    public List<RefundFile> getFileByUser(User patient) {
        String sql = "SELECT * FROM file WHERE user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, patient.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<RefundFile> fileList = new ArrayList<>();
            while (resultSet.next()) {
                RefundFile file = new RefundFile(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("status"),
                        resultSet.getDouble("total_refund"),
                        resultSet.getString("creation_date"));
                fileList.add(file);
            }
            return fileList;
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean deleteFile(int id) {
        String sql = "DELETE FROM file WHERE id = ?";
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
            preparedStatement.setString(1, file.getStatus());
            preparedStatement.setDouble(2, file.getTotalRefund());
            preparedStatement.setString(3, file.getCreationDate());
            preparedStatement.setInt(4, file.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public void addFile(RefundFile refundFile) throws SQLException {
        DocumentDAOImpl documentDAO = new DocumentDAOImpl();
        List<Document> selectedDocuments = new ArrayList<>();

        while (true) {
            System.out.println("Please select the type of document to add to the refund file:");
            System.out.println("1. Medicines");
            System.out.println("2. Radiography");
            System.out.println("3. Medical Examination");
            System.out.println("4. Medical Analysis");
            System.out.println("5. Finish Adding Documents");
            System.out.print("Enter your choice: ");
            Scanner scanner = null;
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
                case 2:
                    // Radiography radiographyDocument = createRadiographyDocument(scanner);
                    // selectedDocuments.add(radiographyDocument);
                    break;
                // Implement cases for other document types (3, 4)
                case 5:
                    if (selectedDocuments.isEmpty()) {
                        System.out.println("No documents selected. Please add at least one document.");
                    } else {
                       // refundFile.addDocuments(selectedDocuments);
                        System.out.println("Documents added to the refund file.");
                        System.out.println("Total documents: " + selectedDocuments.size());
                        //calculateRefundForDocuments(selectedDocuments);
                        return; // Exit the method when done adding documents
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }


}
