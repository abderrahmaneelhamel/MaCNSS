package service;

import dao.FileDAOImpl;
import model.RefundFile;
import model.User;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class FileService {

    private FileDAOImpl fileDAO;


    public FileService(Connection connection){
        fileDAO = new FileDAOImpl(connection);
    }

    public void checkClientFiles(User authenticatedUser){
        List<RefundFile> refundfiles = fileDAO.getFileByUser(authenticatedUser);
        System.out.println("your files :");
        for(RefundFile refundfile : refundfiles){
            System.out.println(refundfile.getId()+"- Status : "+refundfile.getStatus()+"- TotalRefund : "+refundfile.getTotalRefund());
        }
    }
    public void AddFile(){

    }
    public void editFile(){

    }
    public void deleteFile(Scanner scanner){
        List<RefundFile> files = fileDAO.getAllFiles();
        for(RefundFile refundfile : files){
            System.out.println(refundfile.getId()+"- Status : "+refundfile.getStatus()+"- TotalRefund : "+refundfile.getTotalRefund());
        }
        while (true) {
            System.out.println("enter the id of the file you want to delete :");
            int id = scanner.nextInt();
            if (fileDAO.deleteFile(id)) {
                System.out.println("file deleted successfully");
                break;
            } else {
                System.out.println("file not found");
            }
        }
    }

    public void searchFile(){

    }
}
