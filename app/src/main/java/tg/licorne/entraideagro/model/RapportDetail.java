package tg.licorne.entraideagro.model;

/**
 * Created by Admin on 08/05/2018.
 */

public class RapportDetail {
    private int id;
    private String photo;
    private String nom;
    private String token;
    private int id_user;

    public RapportDetail(int id, String photo, String nom, String token, int id_user) {
        this.id = id;
        this.photo = photo;
        this.nom = nom;
        this.token = token;
        this.id_user = id_user;
    }

    public RapportDetail(int id, String photo, String nom, String token) {
        this.id = id;
        this.photo = photo;
        this.nom = nom;
        this.token = token;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
