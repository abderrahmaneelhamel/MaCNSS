package model;

public class Patient extends User{
    public Patient(int id, String name, String email, String password, int matricule) {
        super(id, name, email, password);
        this.matricule = matricule;
    }

    private int matricule;

    public void setMatricule(int matricule) {
        this.matricule = matricule;
    }
    public int getMatricule() {
        return this.matricule;
    }

}
