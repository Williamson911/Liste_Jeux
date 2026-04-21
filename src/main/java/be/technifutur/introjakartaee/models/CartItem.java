package be.technifutur.introjakartaee.models;

public class CartItem {
    private Jeux jeu;
    private int quantite;

    public CartItem(Jeux jeu, int quantite) {
        this.jeu = jeu;
        this.quantite = quantite;
    }

    public Jeux getJeu() { return jeu; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public double getSousTotal() {
        return jeu.getPrix() * quantite;
    }
}
