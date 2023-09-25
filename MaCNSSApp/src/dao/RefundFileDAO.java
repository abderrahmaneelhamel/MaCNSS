package dao;

import model.RefundFile;
import model.User;

import java.util.List;

public interface FileDAO {
    public boolean createFile(RefundFile file);

    public List<RefundFile> getAllFiles();

    public List<RefundFile> getFileByUser(User patient);

    public boolean deleteFile(int id);

    public boolean updateFile(RefundFile file);
}
