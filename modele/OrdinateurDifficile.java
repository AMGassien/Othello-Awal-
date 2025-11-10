package modele;

import modele.Strategie;
import modele.othello.CoupOthello;
import modele.othello.PartieOthello;

import java.util.List;

public class OrdinateurDifficile implements Strategie {

    private static final int PROFONDEUR_MAX = 4; // Profondeur maximale pour l'algorithme Minimax

    @Override
    public CoupOthello choixCoupIa(List<CoupOthello> coupsPossibles, PartieOthello partieOthello) {
        CoupOthello meilleurCoupOthello = null;
        int meilleureValeur = Integer.MIN_VALUE;

        // Parcourir tous les coups possibles pour trouver le meilleur
        for (CoupOthello coupOthello : coupsPossibles) {
            // Simuler le coup
            PartieOthello partieOthelloSimulee = simulerCoup(partieOthello, coupOthello, 1);

            // Appliquer l'algorithme Minimax
            int valeurCoup = minimax(partieOthelloSimulee, PROFONDEUR_MAX, false);

            // Si ce coup est meilleur que les précédents, le retenir
            if (valeurCoup > meilleureValeur) {
                meilleureValeur = valeurCoup;
                meilleurCoupOthello = coupOthello;
            }
        }

        return meilleurCoupOthello;
    }

    /**
     * Simule un coup sur une copie de la partie.
     *
     * @param partieOthello        La partie actuelle.
     * @param coupOthello          Le coup à simuler.
     * @param couleurJoueur La couleur du joueur qui joue le coup.
     * @return Une nouvelle instance de Partie avec le coup appliqué.
     */
    private PartieOthello simulerCoup(PartieOthello partieOthello, CoupOthello coupOthello, int couleurJoueur) {
        // Crée une copie de la partie actuelle
        PartieOthello partieOthelloSimulee = new PartieOthello(partieOthello.getJ1(), partieOthello.getJ2());

        // Copie l'état du plateau
        int[][] plateauOriginal = partieOthello.getPlateau();
        int[][] plateauSimule = new int[plateauOriginal.length][plateauOriginal[0].length];
        for (int i = 0; i < plateauOriginal.length; i++) {
            System.arraycopy(plateauOriginal[i], 0, plateauSimule[i], 0, plateauOriginal[i].length);
        }

        // Applique le coup sur la copie du plateau
        plateauSimule[coupOthello.getLigne()][coupOthello.getColonne()] = couleurJoueur;
        partieOthelloSimulee.setPlateau(plateauSimule);

        // Retourne les pions après avoir appliqué le coup
        partieOthelloSimulee.retournerPion(coupOthello, couleurJoueur);

        return partieOthelloSimulee;
    }

    /**
     * Implémentation de l'algorithme Minimax.
     *
     * @param partieOthello       La partie actuelle.
     * @param profondeur   La profondeur actuelle dans l'arbre de recherche.
     * @param estMaximisant true si c'est le tour de l'ordinateur, false sinon.
     * @return La valeur du meilleur coup.
     */
    private int minimax(PartieOthello partieOthello, int profondeur, boolean estMaximisant) {
        // Cas d'arrêt
        if (profondeur == 0 || partieOthello.estTerminee()) {
            return evaluerPosition(partieOthello, 1); // Évalue pour l'IA
        }

        if (estMaximisant) {
            // Tour de l'IA : cherche le MAX
            int meilleurScore = Integer.MIN_VALUE;
            for (CoupOthello coupOthello : partieOthello.coupOrdinateur()) {
                PartieOthello simulation = simulerCoup(partieOthello, coupOthello, 1);
                int score = minimax(simulation, profondeur - 1, false); // Appel récursif pour l'adversaire
                meilleurScore = Math.max(meilleurScore, score);
            }
            return meilleurScore; // Meilleur score pour l'IA
        } else {
            // Tour de l'adversaire : cherche le MIN (pour l'IA)
            int pireScore = Integer.MAX_VALUE;
            for (CoupOthello coupOthello : partieOthello.coupOrdinateur()) {
                PartieOthello simulation = simulerCoup(partieOthello, coupOthello, 2);
                int score = minimax(simulation, profondeur - 1, true); // Appel récursif pour l'IA
                pireScore = Math.min(pireScore, score);
            }
            return pireScore; // Pire score pour l'IA (meilleur pour l'adversaire)
        }
    }

    /**
     * Évalue la position actuelle du plateau pour un joueur donné.
     *
     * @param partieOthello       La partie à évaluer.
     * @param couleurJoueur La couleur du joueur pour lequel évaluer la position.
     * @return La valeur de la position.
     */
    private int evaluerPosition(PartieOthello partieOthello, int couleurJoueur) {
        if (partieOthello.estTerminee()) {
            String vainqueur = partieOthello.vainqueur();
            if (vainqueur.equals("Ordinateur")) {
                return 1000; // L'ordinateur gagne
            } else if (vainqueur.equals("EX-AEQUO")) {
                return 0; // Égalité
            } else {
                return -1000; // L'adversaire gagne
            }
        }

        int score = 0;
        int[][] plateau = partieOthello.getPlateau();

        // Parcourir le plateau pour attribuer des points en fonction de la position des pions
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                if (plateau[i][j] == couleurJoueur) {
                    if (estCoin(i, j)) {
                        score += 11;
                    } else if (estBord(i, j)) {
                        score += 6;
                    } else {
                        score += 1;
                    }
                }
            }
        }

        return score;
    }

    /**
     * Vérifie si une position est un coin du plateau.
     *
     * @param ligne   La ligne de la position.
     * @param colonne La colonne de la position.
     * @return true si la position est un coin, false sinon.
     */
    private boolean estCoin(int ligne, int colonne) {
        return (ligne == 0 && colonne == 0) ||
                (ligne == 0 && colonne == 7) ||
                (ligne == 7 && colonne == 0) ||
                (ligne == 7 && colonne == 7);
    }

    /**
     * Vérifie si une position est sur un bord du plateau.
     *
     * @param ligne   La ligne de la position.
     * @param colonne La colonne de la position.
     * @return true si la position est sur un bord, false sinon.
     */
    private boolean estBord(int ligne, int colonne) {
        return ligne == 0 || ligne == 7 || colonne == 0 || colonne == 7;
    }
}