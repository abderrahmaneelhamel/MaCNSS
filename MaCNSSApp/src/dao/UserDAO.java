package dao;
import model.Agent;
import model.Patient;
import model.User;

import java.util.List;

public interface UserDAO {

    public Patient authenticatePatient(String matricule, String password);
    public Agent authenticateAgent(String email, String password);
    public boolean addPatient(Patient patient);

    public boolean addAgent(Agent agent);

}