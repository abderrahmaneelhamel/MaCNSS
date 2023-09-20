package model;

public class RefundFile {
    private int id;
    private int medicineId;
    private int patientId;
    private double amount;
    private String creationDate;
    private String status;

    public RefundFile(int medicineId, int patientId, String creationDate,double amount, String status) {
        this.medicineId = medicineId;
        this.patientId = patientId;
        this.creationDate = creationDate;
        this.amount = amount;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMedicineId() {
        return medicineId;
    }
    public int getPatientId() {
        return patientId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

