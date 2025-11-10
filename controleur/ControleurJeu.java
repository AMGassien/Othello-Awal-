package controleur;

import modele.Joueur;
import vue.Ihm;

public abstract class ControleurJeu {
    protected Joueur j1;
    protected Joueur j2;
    protected Ihm ihm;
    protected String rejouerOuArreter;

    public ControleurJeu(Ihm ihm) {
        this.ihm = ihm;
    }

    public final void jouer() {
        initialiserJoueurs();

        boolean continuer = true;
        while (continuer) {
            initialiserPartie();

            while (!partieEstTerminee()) {
                afficherEtatPartie();
                jouerTour();
                changerJoueurCourant();
            }

            afficherResultats();
            continuer = demanderRejouer();
        }

        afficherScoreGlobal();
    }

    protected abstract void initialiserPartie();
    protected abstract boolean partieEstTerminee();
    protected abstract void jouerTour();
    protected abstract void changerJoueurCourant();
    protected abstract void afficherResultats();
    protected abstract Joueur getVainqueur();

    protected void initialiserJoueurs() {
        this.j1 = new Joueur(Ihm.demanderNomJoueur(1), 1);
        this.j2 = new Joueur(Ihm.demanderNomJoueur(2), 2);
    }

    protected boolean demanderRejouer() {
        return !"S".equals(Ihm.rejoueroustop());
    }

    protected void afficherScoreGlobal() {
        Ihm.afficherScoreGlobal(j1.getNom(), j2.getNom(),
                j1.getScore(), j2.getScore(), getVainqueur().getNom());
    }

    protected void afficherEtatPartie() {
    }
}