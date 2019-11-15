package tg.licorne.entraideagro.model;

/**
 * Created by Admin on 17/02/2018.
 */

public class Activites {
    private int id;
    private String nomActivite;
    private String description;

    public Activites() {
    }

    public Activites(int id, String nomActivite) {
        this.id = id;
        this.nomActivite = nomActivite;
    }

    public Activites(String nomActivite, String description) {
        this.nomActivite = nomActivite;
        this.description = description;
    }

    public Activites(int id, String nomActivite, String description) {
        this.id = id;
        this.nomActivite = nomActivite;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomActivite() {
        return nomActivite;
    }

    public void setNomActivite(String nomActivite) {
        this.nomActivite = nomActivite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
