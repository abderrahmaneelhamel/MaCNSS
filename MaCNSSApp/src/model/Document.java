package model;

public abstract class document {
    int id;
    String title;
    double amountPaid;
    double percentage;

    public document(int id, String title, double amountPaid, double percentage) {
        this.id = id;
        this.title = title;
        this.amountPaid = amountPaid;
        this.percentage = percentage;
    }

    public double CalculateRefundedAmount() {
        return amountPaid * percentage / 100;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
