package tg.licorne.entraideagro.model;

/**
 * Created by Admin on 15/02/2018.
 */

public class Artilces {

    private int id;
    private String nomUser;
    private String avatar;
    private String sujet;
    private String contenu;
    private String date;
    private String heure;
    private String photo1;
    private String photo2;
    private String photo3;
    private String photo4;
    private int nbrCommentaire;
    private String token;
    private int id_user;

    public Artilces(int id, String sujet, String contenu, String date, String token) {
        this.id = id;
        this.sujet = sujet;
        this.contenu = contenu;
        this.date = date;
        this.token = token;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public Artilces(int id, String nomUser, String avatar, String sujet, String contenu, String date, String photo1, int nbrCommentaire, String token, int id_user) {
        this.id = id;
        this.nomUser = nomUser;
        this.avatar = avatar;
        this.sujet = sujet;
        this.contenu = contenu;
        this.date = date;
        this.photo1 = photo1;
        this.nbrCommentaire = nbrCommentaire;
        this.token = token;
        this.id_user = id_user;

    }

    public Artilces(int id, String nomUser, String avatar, String sujet, String contenu, String date, String heure, String photo1, int nbrCommentaire, String token, int id_user) {
        this.id = id;
        this.nomUser = nomUser;
        this.avatar = avatar;
        this.sujet = sujet;
        this.contenu = contenu;
        this.date = date;
        this.heure = heure;
        this.photo1 = photo1;
        this.nbrCommentaire = nbrCommentaire;
        this.token = token;
        this.id_user = id_user;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto4() {
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public int getNbrCommentaire() {
        return nbrCommentaire;
    }

    public void setNbrCommentaire(int nbrCommentaire) {
        this.nbrCommentaire = nbrCommentaire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
