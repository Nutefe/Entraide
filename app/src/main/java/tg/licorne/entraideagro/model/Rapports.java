package tg.licorne.entraideagro.model;

/**
 * Created by Admin on 17/02/2018.
 */

public class Rapports {
    private int id;
    private String sujet;
    private String activiter;
    private String date;
    private String token;

    public Rapports() {
    }

    public Rapports(String sujet) {
        this.sujet = sujet;
    }

    public Rapports(int id, String sujet, String date) {
        this.id = id;
        this.sujet = sujet;
        this.date = date;
    }

    public Rapports(int id, String sujet, String date, String token) {
        this.id = id;
        this.sujet = sujet;
        this.date = date;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActiviter() {
        return activiter;
    }

    public void setActiviter(String activiter) {
        this.activiter = activiter;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }
}
