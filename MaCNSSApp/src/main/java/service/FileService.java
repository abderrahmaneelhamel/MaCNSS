package service;

import dao.DocumentDAOImpl;
import dao.PatientDAOImpl;
import dao.RefundFileDAOImpl;
import dao.UserDAOImpl;
import model.*;
import util.tools;
import Enum.RefundFileStatus;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileService {

    private final RefundFileDAOImpl refundFileDAOImpl;
    private final DocumentDAOImpl documentDAOImpl;

    private final UserDAOImpl userDAOImpl;
    private final PatientService patientService;
    private final Connection connection;

    public FileService(Connection connection) {
        this.patientService =  new PatientService(connection);
        this.connection = connection;
        this.refundFileDAOImpl= new RefundFileDAOImpl(connection);
        this.documentDAOImpl = new DocumentDAOImpl(connection);
        this.userDAOImpl = new UserDAOImpl(connection);
    }

    public void checkClientFiles(User authenticatedUser) {
        List<RefundFile> refundFiles = refundFileDAOImpl.getFileByUser(authenticatedUser);
        System.out.println("your files :");
        for (RefundFile refundfile : refundFiles) {
            if(refundfile.getStatus() == RefundFileStatus.approved) {
                System.out.println(refundfile.getId() + "- Status : " + refundfile.getStatus() + "- TotalRefund : " + refundfile.getTotalRefund());
            }else {
                System.out.println(refundfile.getId() + "- Status : " + refundfile.getStatus());
            }
        }
    }
    public void getClientFiles(int matricule) {
        Patient patient = userDAOImpl.getUserByMatricule(matricule);
        List<RefundFile> refundfiles = refundFileDAOImpl.getFileByUser(patient);
        System.out.println("your files :");
        for (RefundFile refundfile : refundfiles) {
            System.out.println(refundfile.getId() + "- Status : " + refundfile.getStatus() + "- TotalRefund : " + refundfile.getTotalRefund());
        }
    }
    Document createDocument(String type,Scanner scanner){
        System.out.println("Enter how much paid on the "+type+" : ");
        int amount = scanner.nextInt();
        return new Document(0,type,amount,0.0,type);
    }

    public void addFile(Scanner scanner)  {
        DocumentDAOImpl documentDAO = new DocumentDAOImpl(connection);
        List<Document> selectedDocuments = new ArrayList<>();

        // Create a new patient
        PatientDAOImpl patientDAOImpl = new PatientDAOImpl(this.connection);
        Patient patient = patientService.createPatient();

        // Add the patient to the database
        patient = patientDAOImpl.addPatientToDatabase(patient);
        if (patient !=  null) {
            System.out.println("Patient added with ID: " + patient.getId());
        while (true) {
            System.out.println("Please select the type of document to add to the refund file:");
            System.out.println("1. Medicines");
            System.out.println("2. Radiography");
            System.out.println("3. Medical Examination");
            System.out.println("4. Medical Analysis");
            System.out.println("5. Finish Adding Documents");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    Medicine medicine = documentDAO.createMedicineDocument();
                    if (medicine != null) {
                        selectedDocuments.add(medicine);
                    }
                }
                case 2 -> {
                    Document radiography = createDocument("radio", scanner);
                    if (radiography != null) {
                        selectedDocuments.add(radiography);
                    }
                }
                case 3 -> {
                    // Create a Medical Examination document
                    Document medicalExamination = createDocument("medicalExamination", scanner);
                    if (medicalExamination != null) {
                        selectedDocuments.add(medicalExamination);
                    }
                }
                case 4 -> {
                    // Create a Medical Analysis document
                    Document medicalAnalysis = createDocument("medicalAnalysis", scanner);
                    if (medicalAnalysis != null) {
                        selectedDocuments.add(medicalAnalysis);
                    }
                }
                case 5 -> {
                    if (selectedDocuments.isEmpty()) {
                        System.out.println("No documents selected. Please add at least one document.");
                    } else {
                        System.out.println("Documents added to the refund file.");
                        System.out.println("Total documents: " + selectedDocuments.size());
                        calculateRefundForDocuments(selectedDocuments);
                        System.out.println("Total Amount: " + calculateRefundForDocuments(selectedDocuments));
                        RefundFile refundFile = new RefundFile(patient.getId(), tools.getCurrentDate(), calculateRefundForDocuments(selectedDocuments), RefundFileStatus.pending);
                        for (Document document : selectedDocuments) {
                            documentDAOImpl.addDocument(document,refundFile);
                        }
                        if (refundFileDAOImpl.addRefundFileToDatabase(refundFile)) {
                            System.out.println("Refund file added to the database.");
                        } else {
                            System.out.println("Failed to add the refund file to the database.");
                        }
                        return;
                    }
                }
                default -> System.out.println("Invalid choice. Please select a valid option.");
            }
        }
        } else {
            System.out.println("Failed to add the patient.");
            return; // Exit the method if patient creation fails
        }
    }

    public void editFile(Scanner scanner){
        List<RefundFile> files =refundFileDAOImpl.getAllFiles();
        for (RefundFile file : files) {
            System.out.println(file.getId() + " - " + file.getStatus().toString());
        }
        System.out.println("Please enter the file id you want to edit: ");
        int fileId = scanner.nextInt();
        RefundFile file = refundFileDAOImpl.getFileById(fileId);
        System.out.println("Please enter the new file status: ");
        System.out.println("1- pending\n2- approved\n3- rejected\n");
        int newFileStatus = scanner.nextInt();
        if(newFileStatus == 1){
            file.setStatus(RefundFileStatus.pending);
            refundFileDAOImpl.updateFile(file);
            System.out.println("Refund file status updated.");
        }else if(newFileStatus == 2){
            file.setStatus(RefundFileStatus.approved);
            refundFileDAOImpl.updateFile(file);
            System.out.println("Refund file status updated.");
        }else if(newFileStatus == 3){
            file.setStatus(RefundFileStatus.rejected);
            refundFileDAOImpl.updateFile(file);
            System.out.println("Refund file status updated.");
        }else{
            System.out.println("Invalid choice. Please select a valid option.");
        }

        if(refundFileDAOImpl.updateFile(file)){
            System.out.println("File status updated successfully.");
        }

    }
    public double calculateRefundForDocuments(List<Document> documents) {
        double totalRefund = 0.0;

        for (Document document : documents) {
            // Check if the document is a Medicine
            if (document instanceof Medicine medicine) {
                double price = medicine.getAmountPaid();
                double percentage = medicine.getPercentage();
                // Calculate the refund for this medicine
                double medicineRefund = price * percentage;

                // Add the medicine refund to the total refund
                totalRefund += medicineRefund;
            }else {
                double price = document.getAmountPaid();
                double percentage = document.getPercentage();
                // Calculate the refund for this document
                double documentRefund = price * percentage;

                // Add the document refund to the total refund
                totalRefund += documentRefund;
            }
        }
        return totalRefund;
    }
}