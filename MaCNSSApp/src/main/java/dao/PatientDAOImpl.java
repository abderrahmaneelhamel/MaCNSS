package dao;

import db.DatabaseConnection;
import model.Patient;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PatientDAOImpl extends UserDAOImpl {
    public PatientDAOImpl(Connection connection) {
        super(connection);
    }

    // Add a new patient to the database
    public Patient addPatientToDatabase(Patient patient) {
        String sql = "INSERT INTO patients (name, email, password, matricule) VALUES (?, ?, ?, ?)";
         try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
            preparedStatement.setString(1, patient.getName());
            preparedStatement.setString(2, patient.getEmail());
            preparedStatement.setString(3, patient.getPassword());
            preparedStatement.setInt(4, patient.getMatricule());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    patient.setId(userId);
                }

                return patient;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
