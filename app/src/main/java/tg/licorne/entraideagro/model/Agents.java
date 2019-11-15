package tg.licorne.entraideagro.model;

/**
 * Created by Admin on 06/05/2018.
 */

public class Agents {
    private int id;
    private String nom;
    private String type;
    private String num;

    public Agents(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Agents(String nom, String type, String num) {
        this.nom = nom;
        this.type = type;
        this.num = num;
    }

    public Agents(int id, String nom, String type, String num) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.num = num;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
