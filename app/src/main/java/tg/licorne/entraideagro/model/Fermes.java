package tg.licorne.entraideagro.model;

/**
 * Created by Admin on 18/02/2018.
 */

public class Fermes {
    private int id;
    private String nom;
    private String dimenssion;
    private String equipe;
    private String zone;
    private String photo;
    private String token;

    public Fermes() {
    }

    public Fermes(int id, String nom, String dimenssion, String equipe, String zone, String photo, String token) {
        this.id = id;
        this.nom = nom;
        this.dimenssion = dimenssion;
        this.equipe = equipe;
        this.zone = zone;
        this.photo = photo;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDimenssion() {
        return dimenssion;
    }

    public void setDimenssion(String dimenssion) {
        this.dimenssion = dimenssion;
    }

    public String getEquipe() {
        return equipe;
    }

    public void setEquipe(String equipe) {
        this.equipe = equipe;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
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
}
