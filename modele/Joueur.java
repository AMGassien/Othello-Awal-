package modele;

public class Joueur {

    private int score;
    private String nom;
    private static int num;

    private int couleur;


    public Joueur(String nom,int couleur) {
        this.score = 0;
        this.nom = nom;
        this.couleur = couleur;

    }

    public int getScore() {
        return score;
    }

    public String getNom() {
        return nom;
    }

    public int getCouleur() {
        return couleur;
    }

    public void setScore() {
        this.score += 1;
    }
}
