package tg.licorne.entraideagro.model;

/**
 * Created by Admin on 01/04/2018.
 */

public class Documents {
    private String icon;
    private String titre;
    private String description;
    private String typeFile;

    public Documents(String titre, String description) {
        this.titre = titre;
        this.description = description;
    }

    public Documents(String titre, String description, String typeFile) {
        this.titre = titre;
        this.description = description;
        this.typeFile = typeFile;
    }

    public String getTypeFile() {
        return typeFile;
    }

    public void setTypeFile(String typeFile) {
        this.typeFile = typeFile;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
