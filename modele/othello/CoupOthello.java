package modele.othello;

public class CoupOthello {

    private int ligne;
    private int colonne;

    public CoupOthello(int ligne, int colonne) {
        this.ligne = ligne;
        this.colonne = colonne;
    }

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }



    @Override
    public String toString() {
        // Convertit la colonne en une lettre (A, B, C, etc.)
        char colonneLettre = (char) ('A' + colonne);
        return "Coup [" + (ligne + 1)  + colonneLettre + "]";
    }


}
