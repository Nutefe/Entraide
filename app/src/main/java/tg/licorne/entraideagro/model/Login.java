package tg.licorne.entraideagro.model;

/**
 * Created by Admin on 30/03/2018.
 */

public class Login {
    private String token;
    private int id_role;

    public Login(String token, int id_role) {
        this.token = token;
        this.id_role = id_role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId_role() {
        return id_role;
    }

    public void setId_role(int id_role) {
        this.id_role = id_role;
    }
}
