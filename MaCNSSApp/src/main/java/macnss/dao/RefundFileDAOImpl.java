package macnss.dao;

import macnss.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import macnss.Enum.RefundFileStatus;

public class RefundFileDAOImpl implements RefundFileDAO {
    private final Connection connection;
    public RefundFileDAOImpl(Connection connection) {
        this.connection = connection;
    }

    public boolean addRefundFileToDatabase(RefundFile file) {
            String sql = "INSERT INTO files (user_id, status, total_refund, creation_date) VALUES (?, ?::refund_file_status, ?, '"+file.getCreationDate()+"')";
            try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, file.getPatientId());
                preparedStatement.setString(2, RefundFileStatus.pending.toString());
                preparedStatement.setDouble(3, file.getTotalRefund());

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
                e.printStackTrace();
            }
            return false;
    }


    public List<RefundFile> getAllFiles() {
        String sql = "SELECT * FROM files;";
        try (Statement statement = this.connection.createStatement()) {
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
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
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
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateFile(RefundFile file) {
        String sql = "UPDATE file SET status = ?, total_refund = ?, creation_date = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
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
}
