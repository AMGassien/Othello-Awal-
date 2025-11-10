package modele.othello;

import modele.Joueur;

import java.util.ArrayList;

import java.util.List;


public class PartieOthello {

    private int[][] Plateau;

    private static int taillePlateau = 8;


    private Joueur j1;

    private Joueur j2;

    private Joueur joueurcourant;

    private String pionNoir = "\u26AB";

    private String pionBlanc = "\u26AA";

    private String caseVide = "..";

    /**
     * Constructeur de la classe Partie.
     * Initialise le plateau et les joueurs.
     *
     * @param j1 Le premier joueur.
     * @param j2 Le deuxième joueur.
     */
    public PartieOthello(Joueur j1, Joueur j2) {
        this.Plateau = new int[taillePlateau][taillePlateau];
        this.j1 = j1;
        this.j2 = j2;
        this.joueurcourant = j1;


    }

    /**
     * Initialise le plateau de jeu avec les pions initiaux.
     */
    public void initialiserPartie() {

        for (int i = 0; i < Plateau.length; i++) {
            for (int j = 0; j < Plateau[i].length; j++) {
                Plateau[i][j] = 0; // cases vides
            }
        }


        int milieu = taillePlateau / 2;

        // Placer les pions initiaux
        Plateau[milieu - 1][milieu - 1] = 1;
        Plateau[milieu - 1][milieu] = 2;
        Plateau[milieu][milieu - 1] = 2;
        Plateau[milieu][milieu] = 1;
    }

    /**
     * Retourne une représentation textuelle du plateau de jeu.
     *
     * @return Une chaîne de caractères représentant le plateau.
     */

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();


        s.append("  ");
        for (char c = 'A'; c < 'A' + taillePlateau; c++) {
            s.append(c).append("  ");
        }
        s.append("\n");


        for (int i = 0; i < taillePlateau; i++) {

            s.append((i + 1)).append(" ");


            for (int j = 0; j < taillePlateau; j++) {
                int cas = Plateau[i][j];
                if (cas == 0) {
                    s.append(caseVide).append(" ");
                } else if (cas == 1) {
                    s.append(pionBlanc).append(" ");
                } else if (cas == 2) {
                    s.append(pionNoir).append(" ");
                }
            }
            s.append("\n");
        }

        return s.toString();
    }

    /**
     * Change le joueur courant (alternance entre les deux joueurs).
     */

    public void changerjoueurcourant() {
        if (this.joueurcourant == j1) {
            joueurcourant = j2;
        } else {
            joueurcourant = j1;
        }
    }

    /**
     * Retourne le joueur courant.
     *
     * @return Le joueur actuellement en train de jouer.
     */
    public Joueur getJoueurcourant() {
        return joueurcourant;
    }

    /**
     * Explore une direction à partir d'une position donnée pour vérifier si un coup est valide.
     *
     * @param ligne         La ligne de départ.
     * @param colonne       La colonne de départ.
     * @param deltaLigne    La direction verticale à explorer.
     * @param deltaColonne  La direction horizontale à explorer.
     * @param couleurJoueur La couleur du joueur courant.
     * @param retourner     Indique si les pions doivent être retournés.
     * @return true si le coup est valide dans cette direction, false sinon.
     */
    private boolean explorerDirection(int ligne, int colonne, int deltaLigne, int deltaColonne, int couleurJoueur, boolean retourner) {
        int couleurAdverse = (couleurJoueur == 1) ? 2 : 1;
        int x = ligne + deltaLigne;
        int y = colonne + deltaColonne;
        boolean pionAdverseTrouve = false;
        int nbPionsARetourner = 0;
        // Explore la direction jusqu'à sortir du plateau ou trouver une case vide
        while (x >= 0 && x < taillePlateau && y >= 0 && y < taillePlateau) {
            if (Plateau[x][y] == couleurAdverse) {
                pionAdverseTrouve = true;
                nbPionsARetourner++;

                // Si on trouve un pion du joueur actuel et qu'un pion adverse a été trouvé avant
            } else if (Plateau[x][y] == couleurJoueur && pionAdverseTrouve) {
                // retourner = true on retourne les pions
                if (retourner) {
                    for (int j = 1; j <= nbPionsARetourner; j++) {
                        Plateau[ligne + deltaLigne * j][colonne + deltaColonne * j] = couleurJoueur;
                    }
                }
                // sinon renvoye true pour dire que le coup est valide
                return true;

                // Si la case est vide ou ne contient pas un pion adverse, on arrête l'exploration
            } else {
                break;
            }

            // Passe à la case suivante dans la direction
            x += deltaLigne;
            y += deltaColonne;
        }
        return false; // coup invalide dans cette direction
    }

    /**
     * Vérifie si un coup est valide pour un joueur donné.
     *
     * @param coupOthello          Le coup à vérifier.
     * @param couleurJoueur La couleur du joueur.
     * @return true si le coup est valide, false sinon.
     */
    public boolean estCoupValide(CoupOthello coupOthello, int couleurJoueur) {
        int ligne = coupOthello.getLigne();
        int colonne = coupOthello.getColonne();

        if (Plateau[ligne][colonne] != 0) {
            return false;
        }

        int[] directionsLigne = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] directionsColonne = {-1, 0, 1, -1, 1, -1, 0, 1};

        // utilise la methode explorerdirection pour verifier la validité d'un coup
        for (int i = 0; i < 8; i++) {
            if (explorerDirection(ligne, colonne, directionsLigne[i], directionsColonne[i], couleurJoueur, false)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne les pions adverses après avoir joué un coup.
     *
     * @param coupOthello          Le coup joué.
     * @param couleurJoueur La couleur du joueur.
     */
    public void retournerPion(CoupOthello coupOthello, int couleurJoueur) {
        int ligne = coupOthello.getLigne();
        int colonne = coupOthello.getColonne();

        int[] directionsLigne = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] directionsColonne = {-1, 0, 1, -1, 1, -1, 0, 1};

        // utilise la méthode explorerdirection pour retourner les pions après avoir appliqué le coup.
        for (int i = 0; i < 8; i++) {
            explorerDirection(ligne, colonne, directionsLigne[i], directionsColonne[i], couleurJoueur, true);
        }
    }

    /**
     * Vérifie si un joueur a des coups valides disponibles.
     *
     * @param couleur La couleur du joueur.
     * @return true si le joueur a des coups valides, false sinon.
     */
    public boolean aCoupsValides(int couleur) {

        for (int i = 0; i < taillePlateau; i++) {
            for (int j = 0; j < taillePlateau; j++) {

                if (Plateau[i][j] == 0) {

                    if (estCoupValide(new CoupOthello(i, j), couleur)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * Applique un coup sur le plateau.
     *
     * @param c       Le coup à appliquer.
     * @param couleur La couleur du joueur.
     */
    public void appliquerCoup(CoupOthello c, int couleur) {
        int ligne = c.getLigne();
        int colonne = c.getColonne();
        Plateau[ligne][colonne] = couleur;
    }

    /**
     * Vérifie si la partie est terminée.
     *
     * @return true si la partie est terminée, false sinon.
     */
    public boolean estTerminee() {
        if (aCoupsValides(j1.getCouleur()) && aCoupsValides(j2.getCouleur())) {
            return false;
        }
        return true;
    }

    /**
     * Détermine le vainqueur de la partie en cours.
     *
     * @return Le nom du joueur gagnant ou "EX-AEQUO" en cas d'égalité.
     */
    public String vainqueur() {
        int pionblanc = compterPion(1);
        int pionnoir = compterPion(2);

        if (pionblanc > pionnoir) {
            j2.setScore();
            return j2.getNom();
        } else if (pionnoir > pionblanc) {
            j1.setScore();
            return j1.getNom();
        }
        return "EX-AEQUO";
    }

    /**
     * Compte le nombre de pions d'une couleur donnée sur le plateau.
     *
     * @param couleur La couleur des pions à compter.
     * @return Le nombre de pions de la couleur spécifiée.
     */
    public int compterPion(int couleur) {
        int pion = 0;
        for (int i = 0; i < taillePlateau; i++) {
            for (int j = 0; j < taillePlateau; j++) {
                if (Plateau[i][j] == couleur) {
                    pion += 1;
                }
            }
        }
        return pion;

    }

    /**
     * Détermine le vainqueur global en fonction des scores des joueurs.
     *
     * @return Le nom du joueur gagnant ou "EX-AEQUO" en cas d'égalité.
     */
    public String vainqueurglobal() {
        if (j1.getScore() > j2.getScore()) {
            return j1.getNom();
        } else if (j2.getScore() > j1.getScore()) {
            return j2.getNom();
        }
        return "EX-AEQUO";
    }

    /**
     * Traduit une chaîne de caractères en un coup.
     *
     * @param c La chaîne de caractères représentant le coup.
     * @return Un objet Coup correspondant.
     */
    public CoupOthello traduireCoup(String c) {
        String[] parties = c.split(" ");

        int ligne = Integer.parseInt(parties[0]) - 1;

        int colonne = parties[1].charAt(0) - 'A';

        return new CoupOthello(ligne, colonne);
    }

    /**
     * Retourne une liste de coups valides pour l'ordinateur.
     *
     * @return Une liste de coups valides.
     */
    public List<CoupOthello> coupOrdinateur() {
        List<CoupOthello> coupsPossibles = new ArrayList<>();
        for (int i = 0; i < taillePlateau; i++) {
            for (int j = 0; j < taillePlateau; j++) {
                if (Plateau[i][j] == 0 && estCoupValide(new CoupOthello(i, j), 1)) {
                    CoupOthello ordi = new CoupOthello(i, j);
                    coupsPossibles.add(ordi);
                }
            }
        }

        return coupsPossibles; // Retourne tous les coups valides trouvés
    }


    public Joueur getJ1() {
        return j1;
    }

    public Joueur getJ2() {
        return j2;
    }

    public int[][] getPlateau() {
        return Plateau;
    }

    public void setPlateau(int[][] plateau) {
        Plateau = plateau;
    }


}




