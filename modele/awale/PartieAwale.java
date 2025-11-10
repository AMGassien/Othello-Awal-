/**
 * Cette classe représente une partie du jeu Awale.
 * Elle gère le déroulement du jeu, les coups des joueurs, les captures de graines et la détermination du vainqueur.
 */
package modele.awale;

import modele.Joueur;
import java.util.Arrays;

public class PartieAwale {
    private int[][] plateau;
    private int[] greniers;
    private Joueur j1;
    private Joueur j2;
    private Joueur joueurCourant;

    /**
     * Constructeur d'une partie d'Awale.
     * @param j1 Le premier joueur
     * @param j2 Le deuxième joueur
     */
    public PartieAwale(Joueur j1, Joueur j2) {
        this.j1 = j1;
        this.j2 = j2;
        this.joueurCourant = j1;
        this.plateau = new int[2][6];
        this.greniers = new int[2];
    }

    /**
     * Initialise la partie avec 4 graines dans chaque case et vide les greniers.
     */
    public void initialiserPartie() {
        for (int i = 0; i < 6; i++) {
            plateau[0][i] = 4; // Joueur 1 (bas)
            plateau[1][i] = 4; // Joueur 2 (haut)
        }
        Arrays.fill(greniers, 0);
    }

    /**
     * Joue un coup pour le joueur courant.
     * @param colonne La colonne choisie (1 à 6)
     * @return 0 si le coup est valide, 1 si colonne invalide, 2 si case vide, 3 si coup affame l'adversaire
     */
    public int jouerCoup(int colonne) {
        int ligne = (joueurCourant == j1) ? 0 : 1;
        colonne--;

        if (colonne < 0 || colonne >= 6) {
            return 1; // Erreur: colonne invalide
        }
        if (plateau[ligne][colonne] == 0) {
            return 2; // Erreur: case vide
        }
        if (affameAdversaire(ligne, colonne)) {
            return 3; // Erreur: affame l'adversaire
        }

        distribuerGraines(ligne, colonne);
        changerJoueur();
        return 0; // Succès
    }

    /**
     * Vérifie si un coup est valide.
     * @param ligne La ligne du coup
     * @param colonne La colonne du coup
     * @return true si le coup est valide, false sinon
     */
    private boolean estCoupValide(int ligne, int colonne) {
        return colonne >= 0 && colonne < 6
                && plateau[ligne][colonne] > 0
                && !affameAdversaire(ligne, colonne);
    }

    /**
     * Distribue les graines d'une case sélectionnée dans les cases suivantes selon les règles de l'Awale.
     *
     * @param ligneDepart   La ligne (0 ou 1) de la case de départ
     * @param colonneDepart La colonne (0 à 5) de la case de départ
     * Processus:
     * 1. Récupère le nombre de graines de la case de départ et la vide
     * 2. Parcourt les cases suivantes dans le sens anti-horaire
     * 3. Dépose une graine dans chaque case sauf la case de départ
     * 4. Après distribution, vérifie si une capture est possible
     */
    private void distribuerGraines(int ligneDepart, int colonneDepart) {
        // On prend toutes les graines de la case sélectionnée
        int graines = plateau[ligneDepart][colonneDepart];
        plateau[ligneDepart][colonneDepart] = 0; // On vide la case de départ

        int ligne = ligneDepart;
        int colonne = colonneDepart;

        // Distribution des graines une par une
        while (graines > 0) {
            // Passe à la case suivante
            colonne = (colonne + 1) % 6;
            // Changement de ligne quand on dépasse la dernière colonne
            if (colonne == 0) {
                ligne = (ligne == 0) ? 1 : 0; // Alterne entre ligne 0 et 1
            }

            // Ne pas remettre de graine dans la case de départ
            if (!(ligne == ligneDepart && colonne == colonneDepart)) {
                plateau[ligne][colonne]++; // Ajoute une graine
                graines--; // Décrémente le compteur
            }
        }
        // Après distribution, vérifie si la dernière case permet une capture
        verifierCapture(ligne, colonne);
    }

    /**
     * Vérifie si les conditions pour une capture sont remplies après une distribution.
     *
     * @param ligneFinale   Ligne de la dernière graine déposée
     * @param colonneFinale Colonne de la dernière graine déposée
     *
     * Conditions de capture:
     * 1. La dernière graine doit être du côté adversaire
     * 2. La case doit contenir 2 ou 3 graines après distribution
     */
    private void verifierCapture(int ligneFinale, int colonneFinale) {
        // Vérifie si on est côté adversaire et si la case a 2 ou 3 graines
        if (ligneFinale != (joueurCourant.getCouleur() == 1 ? 0 : 1)
                && (plateau[ligneFinale][colonneFinale] == 2 || plateau[ligneFinale][colonneFinale] == 3)) {
            capturerGraines(ligneFinale, colonneFinale); // Lance la capture
        }
    }

    /**
     * Capture les graines selon les règles de l'Awale (rafle).
     *
     * @param ligne    Ligne où commencer la capture
     * @param colonne  Colonne où commencer la capture
     *
     * Mécanisme:
     * 1. Capture la case cible (2 ou 3 graines)
     * 2. Continue à capturer vers la gauche si les cases adjacentes contiennent 2 ou 3 graines
     * 3. Ajoute les graines capturées au grenier du joueur courant
     */
    private void capturerGraines(int ligne, int colonne) {
        int joueurIndex = joueurCourant.getCouleur() - 1; // 0 pour J1, 1 pour J2
        int totalCapture = 0;

        // Capture la case initiale
        totalCapture += plateau[ligne][colonne];
        plateau[ligne][colonne] = 0; // Vide la case

        // Rafle vers la gauche (cases précédentes)
        int colonneRafle = colonne - 1;
        while (colonneRafle >= 0) {
            if (plateau[ligne][colonneRafle] == 2 || plateau[ligne][colonneRafle] == 3) {
                totalCapture += plateau[ligne][colonneRafle]; // Ajoute au butin
                plateau[ligne][colonneRafle] = 0; // Vide la case
                colonneRafle--; // Continue à gauche
            } else {
                break; // Arrête si la case ne contient pas 2 ou 3 graines
            }
        }

        greniers[joueurIndex] += totalCapture; // Stocke les graines capturées
    }

    /**
     * Vérifie si un coup affamerait l'adversaire (interdit par les règles).
     *
     * @param ligne    Ligne du coup envisagé
     * @param colonne  Colonne du coup envisagé
     * @return true si le coup priverait l'adversaire de toutes ses graines, false sinon
     *
     * Méthode:
     * 1. Simule le coup sur une copie du plateau
     * 2. Vérifie si l'adversaire aurait encore des graines après le coup
     */
    private boolean affameAdversaire(int ligne, int colonne) {
        // Crée une copie temporaire du plateau pour simulation
        int[][] plateauTemp = new int[2][6];
        for (int i = 0; i < plateau.length; i++) {
            System.arraycopy(plateau[i], 0, plateauTemp[i], 0, plateau[i].length);
        }

        // Simulation de la distribution
        int graines = plateauTemp[ligne][colonne];
        plateauTemp[ligne][colonne] = 0;
        int ligneCourante = ligne;
        int colonneCourante = colonne;

        while (graines > 0) {
            colonneCourante--;
            if (colonneCourante < 0) {
                colonneCourante = 5;
                ligneCourante = (ligneCourante == 0) ? 1 : 0; // Changement de ligne
            }
            if (!(ligneCourante == ligne && colonneCourante == colonne)) {
                plateauTemp[ligneCourante][colonneCourante]++;
                graines--;
            }
        }

        // Vérification des graines adverses après simulation
        int ligneAdverse = (joueurCourant.getCouleur() == 1) ? 1 : 0;
        for (int g : plateauTemp[ligneAdverse]) {
            if (g > 0) return false; // L'adversaire a encore des graines
        }
        return true; // L'adversaire n'a plus de graines -> coup interdit
    }



    /**
     * Change le joueur courant.
     */
    public void changerJoueur() {
        joueurCourant = (joueurCourant == j1) ? j2 : j1;
    }

    /**
     * Vérifie si la partie est terminée.
     * @return true si la partie est terminée, false sinon
     */
    public boolean estTerminee() {
        boolean joueur1Vide = Arrays.stream(plateau[0]).allMatch(g -> g == 0);
        boolean joueur2Vide = Arrays.stream(plateau[1]).allMatch(g -> g == 0);
        return joueur1Vide || joueur2Vide;
    }

    /**
     * Termine la partie et attribue les graines restantes au vainqueur si nécessaire.
     */
    public void terminerPartie() {
        if (!estTerminee()) return;

        int totalRestant = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                totalRestant += plateau[i][j];
                plateau[i][j] = 0;
            }
        }

        if (totalRestant > 0) {
            Joueur vainqueur = getVainqueurSansGrainesRestantes();
            if (vainqueur != null) {
                greniers[vainqueur.getCouleur() - 1] += totalRestant;
            }
        }
    }

    /**
     * Détermine le vainqueur sans tenir compte des graines restantes.
     * @return Le joueur gagnant ou null en cas d'égalité
     */
    private Joueur getVainqueurSansGrainesRestantes() {
        if (greniers[0] > greniers[1]) return j1;
        if (greniers[1] > greniers[0]) return j2;
        return null; // Égalité
    }

    /**
     * Détermine le vainqueur de la partie.
     * @return Le joueur gagnant ou null en cas d'égalité
     */
    public Joueur getVainqueur() {
        if (greniers[0] > greniers[1]) {
            return j1;
        } else if (greniers[1] > greniers[0]) {
            return j2;
        }
        return null; // Égalité
    }

    /**
     * Retourne le joueur courant.
     * @return Le joueur dont c'est le tour
     */
    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    /**
     * Retourne une représentation textuelle du plateau de jeu.
     * @return Une chaîne représentant l'état du jeu
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Joueur : " + j2.getNom() + " (Haut) - Grenier: ").append(greniers[1]).append("\n");
        for (int i = 5; i >= 0; i--) {
            sb.append(plateau[1][i]).append(" ");
        }
        sb.append("\n");
        for (int i = 0; i < 6; i++) {
            sb.append(plateau[0][i]).append(" ");
        }
        sb.append("\n  Joueur : " + j1.getNom() + " (Bas) - Grenier: ").append(greniers[0]);
        return sb.toString();
    }

    /**
     * Retourne le nombre de graines du joueur 1 dans son grenier.
     * @return Le score du joueur 1
     */
    public int getGrainesJoueur1() {
        return greniers[0];
    }

    /**
     * Retourne le nombre de graines du joueur 2 dans son grenier.
     * @return Le score du joueur 2
     */
    public int getGrainesJoueur2() {
        return greniers[1];
    }
}