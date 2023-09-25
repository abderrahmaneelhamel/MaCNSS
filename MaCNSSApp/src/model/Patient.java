package model;

public class Patient extends User{
    private String matricule;

    public Patient(  int id, String name, String email, String password, String matricule) {
        super(  id,   name, email, password);
        this.matricule = matricule;
    }


    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
    public String getMatricule() {
        return this.matricule;
    }

}
