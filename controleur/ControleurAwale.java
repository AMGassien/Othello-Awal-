package controleur;

import modele.*;
import modele.awale.*;
import vue.Ihm;

public class ControleurAwale extends ControleurJeu {
    private PartieAwale partie;

    public ControleurAwale(Ihm ihm) {
        super(ihm);
    }

    @Override
    protected void initialiserJoueurs() {
        this.j1 = new Joueur(Ihm.demanderNomJoueur(1), 1); // Joueur 1 (bas)
        this.j2 = new Joueur(Ihm.demanderNomJoueur(2), 2); // Joueur 2 (haut)
    }

    @Override
    protected void initialiserPartie() {
        this.partie = new PartieAwale(j1, j2);
        partie.initialiserPartie();
    }

    @Override
    protected boolean partieEstTerminee() {
        return partie.estTerminee();
    }

    @Override
    protected void jouerTour() {
        boolean coupValide = false;

        while (!coupValide) {
            try {
                int colonne = Integer.parseInt(Ihm.demanderCoupAwale(partie.getJoueurCourant().getNom()));
                int resultat = partie.jouerCoup(colonne);

                switch (resultat) {
                    case 0:
                        coupValide = true;
                        break;
                    case 1:
                        ihm.afficherMessage("Colonne invalide. Choisissez entre 1 et 6.");
                        break;
                    case 2:
                        ihm.afficherMessage("Case vide. Choisissez une case avec des graines.");
                        break;
                    case 3:
                        ihm.afficherMessage("Interdit: ce coup affamerait l'adversaire.");
                        break;
                }
            } catch (NumberFormatException e) {
                ihm.afficherMessage("Veuillez entrer un nombre valide.");
            }
        }
    }

    @Override
    protected void changerJoueurCourant() {
    }

    @Override
    protected void afficherResultats() {
        // 1. Récupération finale des graines
        partie.terminerPartie();

        // 2. Calcul du vainqueur
        Joueur vainqueur = partie.getVainqueur();

        // 3. Récupération des graines totales (greniers + captures)
        int grainesJ1 = partie.getGrainesJoueur1(); // Méthode à créer si nécessaire
        int grainesJ2 = partie.getGrainesJoueur2();

        // 4. Affichage détaillé
        ihm.afficherMessage("Résultat final :");
        ihm.afficherMessage(j1.getNom() + " : " + grainesJ1 + " graines");
        ihm.afficherMessage(j2.getNom() + " : " + grainesJ2 + " graines");

        if (vainqueur != null) {
            ihm.afficherMessage("Vainqueur : " + vainqueur.getNom());
        } else {
            ihm.afficherMessage("Match nul !");
        }

        // 5. Mise à jour scores globaux
        if (vainqueur != null) {
            vainqueur.setScore();
        }
    }

    @Override
    protected Joueur getVainqueur() {
        // Comparer les scores globaux pour déterminer le vainqueur global
        if (j1.getScore() > j2.getScore()) {
            return j1;
        } else if (j2.getScore() > j1.getScore()) {
            return j2;
        }
        return null; // Match nul
    }

    @Override
    protected void afficherEtatPartie() {
        Ihm.afficherEtatPartie(partie.toString());
    }
}