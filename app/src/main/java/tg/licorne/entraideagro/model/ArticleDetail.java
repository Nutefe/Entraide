package tg.licorne.entraideagro.model;

/**
 * Created by Admin on 30/04/2018.
 */

public class ArticleDetail {
    private int id;
    private String photo;
    private int nbrCommentair;
    private String token;
    private int id_user;

    public ArticleDetail(int id, String photo, int nbrCommentair, String token, int id_user) {
        this.id = id;
        this.photo = photo;
        this.nbrCommentair = nbrCommentair;
        this.token = token;
        this.id_user = id_user;
    }

    public ArticleDetail(int id, String photo) {
        this.id = id;
        this.photo = photo;
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

    public int getNbrCommentair() {
        return nbrCommentair;
    }

    public void setNbrCommentair(int nbrCommentair) {
        this.nbrCommentair = nbrCommentair;
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
