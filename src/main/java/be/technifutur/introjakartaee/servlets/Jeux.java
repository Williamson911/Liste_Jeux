package be.technifutur.introjakartaee.servlets;

public class Jeux {
    private int id;
    private String titre;
    private int annee;
    private String description;
    private String imageUrl;

    public Jeux(int id, String titre, int annee, String description, String imageUrl) {
        this.id = id;
        this.titre = titre;
        this.annee = annee;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public int getId() { return id; }
    public String getTitre() { return titre; }
    public int getAnnee() { return annee; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}