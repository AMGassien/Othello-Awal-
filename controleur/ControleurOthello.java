package controleur;

import modele.*;
import modele.othello.*;
import vue.Ihm;
import java.util.List;

public class ControleurOthello extends ControleurJeu {
    private PartieOthello partie;
    private String nbrJoueur;
    private Strategie strategieOrdi;

    public ControleurOthello(Ihm ihm) {
        super(ihm);
    }

    @Override
    protected void initialiserJoueurs() {
        this.j1 = new Joueur(Ihm.demanderNomJoueur(1), 2); // Joueur 1 (blanc)
        this.nbrJoueur = Ihm.combiendeJoueur();

        if (nbrJoueur.equals("1")) {
            this.j2 = new Joueur("Ordinateur", 1); // Joueur 2 (noir)
            if (Ihm.demanderDifficulte().equals("D")) {
                this.strategieOrdi = new OrdinateurDifficile();
            } else {
                this.strategieOrdi = new OrdinateurFacile();
            }
        } else {
            this.j2 = new Joueur(Ihm.demanderNomJoueur(2), 1);
        }
    }

    @Override
    protected void initialiserPartie() {
        this.partie = new PartieOthello(j1, j2);
        partie.initialiserPartie();
    }

    @Override
    protected boolean partieEstTerminee() {
        return partie.estTerminee();
    }

    @Override
    protected void jouerTour() {
        boolean coupValide = false;
        CoupOthello coupCourant = null;
        String coupCourantString = null;
        int couleurJoueurCourant = partie.getJoueurcourant().getCouleur();

        if (couleurJoueurCourant == 1 && this.nbrJoueur.equals("1")) {
            List<CoupOthello> coupsOrdinateurPossible = partie.coupOrdinateur();
            if (partie.aCoupsValides(1)) {
                coupCourant = strategieOrdi.choixCoupIa(coupsOrdinateurPossible, partie);
                coupValide = true;
                ihm.afficherMessage("L'ordinateur joue en " + coupCourant.toString());
            } else {
                ihm.afficherMessage("L'ordinateur passe son tour.");
                coupValide = true;
            }
        } else {
            while (!coupValide) {
                coupCourantString = ihm.demanderCoup(partie.getJoueurcourant().getNom());

                if (coupCourantString.equalsIgnoreCase("P")) {
                    if (partie.aCoupsValides(couleurJoueurCourant)) {
                        ihm.afficherMessage("Impossible de passer son tour, vous avez des coups valides !");
                    } else {
                        coupValide = true;
                        ihm.afficherMessage(partie.getJoueurcourant().getNom() + " passe son tour.");
                    }
                } else {
                    coupCourant = partie.traduireCoup(coupCourantString);
                    if (coupCourant != null && partie.estCoupValide(coupCourant, couleurJoueurCourant)) {
                        coupValide = true;
                    } else {
                        ihm.afficherMessage("Coup invalide, veuillez rejouer.");
                    }
                }
            }
        }

        if (coupCourant != null) {
            partie.appliquerCoup(coupCourant, couleurJoueurCourant);
            partie.retournerPion(coupCourant, couleurJoueurCourant);
        }
    }

    @Override
    protected void changerJoueurCourant() {
        partie.changerjoueurcourant();
    }

    @Override
    protected void afficherResultats() {
        int scoreJ1 = partie.compterPion(j1.getCouleur());
        int scoreJ2 = partie.compterPion(j2.getCouleur());
        String resultat = partie.vainqueur();

        Ihm.afficherResultats(j1.getNom(), j2.getNom(), scoreJ1, scoreJ2, resultat);

        if (resultat.equals(j1.getNom())) {
            j1.setScore();
        } else if (resultat.equals(j2.getNom())) {
            j2.setScore();
        }
    }

    @Override
    protected Joueur getVainqueur() {
        if (j1.getScore() > j2.getScore()) {
            return j1;
        } else if (j2.getScore() > j1.getScore()) {
            return j2;
        }
        return null;
    }

    @Override
    protected void afficherEtatPartie() {
        Ihm.afficherEtatPartie(partie.toString());
    }
}