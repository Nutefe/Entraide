package tg.licorne.entraideagro.model;

/**
 * Created by Admin on 18/02/2018.
 */

public class Commentaires {
    private int id;
    private String nom;
    private String date;
    private String avatar;
    private String contenu;

    public Commentaires(int id, String nom, String date, String avatar, String contenu) {
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.avatar = avatar;
        this.contenu = contenu;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
