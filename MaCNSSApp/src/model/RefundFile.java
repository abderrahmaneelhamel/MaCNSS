package model;

public class RefundFile {
    private int id;
    private int userId;
    private double totalRefund;
    private String creationDate;
    private String status;

    public RefundFile(int id,int userId, String creationDate,double totalRefund, String status) {
        this.id = id;
        this.userId = userId;
        this.creationDate = creationDate;
        this.totalRefund = totalRefund;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return userId;
    }

    public void setPatientId(int userId) {
        this.userId = userId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public double getTotalRefund() {
        return totalRefund;
    }

    public void setTotalRefund(double totalRefund) {
        this.totalRefund = totalRefund;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

