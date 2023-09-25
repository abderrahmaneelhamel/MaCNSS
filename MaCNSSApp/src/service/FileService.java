package service;

import dao.RefundFileDAOImpl;
import model.RefundFile;
import model.User;

import java.sql.Connection;
import java.util.List;

public class FileService {

    private RefundFileDAOImpl fileDAO;


    public FileService(Connection connection) {
        fileDAO = new RefundFileDAOImpl(connection);
    }

    public void checkClientFiles(User authenticatedUser) {
        List<RefundFile> refundfiles = fileDAO.getFileByUser(authenticatedUser);
        System.out.println("your files :");
        for (RefundFile refundfile : refundfiles) {
            System.out.println(refundfile.getId() + "- Status : " + refundfile.getStatus() + "- TotalRefund : " + refundfile.getTotalRefund());
        }
    }
}