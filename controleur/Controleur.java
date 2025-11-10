package controleur;

import vue.Ihm;

public class Controleur{
    public static ControleurJeu creerControleur(String jeu, Ihm ihm) {
        switch (jeu.toUpperCase()) {
            case "O":
                return new ControleurOthello(ihm);
            case "A":
                return new ControleurAwale(ihm);
            default:
                throw new IllegalArgumentException("Jeu non reconnu");
        }
    }
}