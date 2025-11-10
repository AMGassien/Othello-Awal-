package modele.awale;

public class CoupAwale {
    private final int colonne; // Seule la colonne compte pour l'Awalé

    public CoupAwale(int colonne) {
        this.colonne = colonne;
    }

    public int getColonne() {
        return colonne;
    }

    @Override
    public String toString() {
        return "Colonne: " + (colonne + 1); // +1 pour affichage 1-6 au lieu de 0-5
    }
}
