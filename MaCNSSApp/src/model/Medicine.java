package model;
public class Medicine {
    private int id;
    private String title;
    private String refundPercentage;


    // Constructor with all fields
    public Medicine(String title, String refundPercentage) {
        this.title = title;
        this.refundPercentage = refundPercentage;
    }

    // Getters and setters for all properties

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRefundPercentage(String refundPercentage) {
        this.refundPercentage = refundPercentage;
    }


    // Additional getter methods

    public String getBookTitle() {
        return title;
    }

    public String getBookRefundPercentage() {
        return refundPercentage;
    }

    @Override
    public String toString() {
        return "-" + id +
                " | title='" + title + '\'' +
                " | refund Percentage='" + refundPercentage;
    }
}
