package model;
public class Medicine extends document{
    public Medicine(int id, String title, double amountPaid, double percentage) {
        super(id, title, amountPaid, percentage);
    }
    private String borCode;

    public String getBorCode(){
        return this.borCode;
    }

    public void setBorCode(String barCode){
        this.borCode = barCode;
    }
}
