package service;

import dao.DocumentDAOImpl;
import dao.PatientDAOImpl;
import dao.RefundFileDAOImpl;
import model.*;
import util.tools;
import Enum.RefundFileStatus;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileService {

    private final RefundFileDAOImpl refundFileDAOImpl;

    private final PatientDAOImpl patientDAOImpl;

    List<Document> documents;

    private final Connection connection;



    public FileService(Connection connection) {
        this.patientDAOImpl =  new PatientDAOImpl(connection);
        this.connection = connection;
        this.refundFileDAOImpl= new RefundFileDAOImpl(connection);
    }

    public void checkClientFiles(User authenticatedUser) {
        List<RefundFile> refundfiles = refundFileDAOImpl.getFileByUser(authenticatedUser);
        System.out.println("your files :");
        for (RefundFile refundfile : refundfiles) {
            System.out.println(refundfile.getId() + "- Status : " + refundfile.getStatus() + "- TotalRefund : " + refundfile.getTotalRefund());
        }
    }

    public void addFile()  {
        DocumentDAOImpl documentDAO = new DocumentDAOImpl();
        List<Document> selectedDocuments = new ArrayList<>();

        // Create a new patient
        PatientDAOImpl patientDAOImpl = new PatientDAOImpl(this.connection);
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
                        this.documents = selectedDocuments;
                        System.out.println("Documents added to the refund file.");
                        System.out.println("Total documents: " + selectedDocuments.size());
                        calculateRefundForDocuments(selectedDocuments);
                        System.out.println("Total Amount: " + calculateRefundForDocuments(selectedDocuments));

                        // Call the method to add the refund file to the database
                        RefundFile refundFile = new RefundFile(patient.getId(), tools.getCurrentDate(), calculateRefundForDocuments(selectedDocuments), RefundFileStatus.pending);
                        if (refundFileDAOImpl.addRefundFileToDatabase(refundFile)) {
                            System.out.println("Refund file added to the database.");
                        } else {
                            System.out.println("Failed to add the refund file to the database.");
                        }
                        return;
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
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
}