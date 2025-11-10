package vue;

import java.util.Scanner;

public class Ihm {
    private static Scanner sc = new Scanner(System.in);


    public Ihm() {
        this.sc = sc;
    }



    public static String demanderJeu(){
        System.out.println("O pour jouer à Othello et A pour jouer à Awale");
        String jeu = sc.nextLine();

        while(!jeu.matches("O|A")){
            System.out.println("Entrez O pour jouer à Othello et A pour Awale");
            jeu = sc.nextLine();
        }
        return jeu;
    }

    public static String demanderCoupAwale(String j) {
        System.out.println(j  + ", choisissez une colonne (1-6) :");
        String coup = sc.nextLine() ;

        while (!coup.matches("[1-6]")) {
            System.out.println("Entrée invalide. Assurez-vous de respecter le format");
            coup = sc.nextLine();
        }
        return coup;
    }

    public static String demanderNomJoueur(int i) {
        System.out.println("Veuillez entrer le nom du joueur " + i + " :");
        String joueur = sc.nextLine();

        // Tant que le nom ne contient pas uniquement des lettres, redemander
        while (!joueur.matches("^(?=.*[a-zA-Z])[a-zA-Z0-9]+$")) {
            System.out.println("Le nom du joueur " + i + " n'est pas valide, entrez un nom valide :");
            joueur = sc.nextLine();
        }

        return joueur; // Retourner le nom valide
    } // Demande le nom d'un joueur

    public static String demanderDifficulte(){
        System.out.println("Voulez vous jouer en mode difficile ou mode facile, entrez D pour Difficile et F pour facile");
        String difficulte = sc.nextLine();

        while(!difficulte.matches("D|F")){
            System.out.println("Entrez D pour mode difficile et F pour mode facile");
            difficulte = sc.nextLine();
        }

        return difficulte;
    }

    public static String combiendeJoueur(){
        System.out.println("Voulez vous jouer à 1 ou 2 joueur ? Entrez 1 ou 2.");
        String joueur = sc.nextLine();

        while (!joueur.matches("1|2")){
            System.out.println("Entrez 1 pour jouer tout seul ou 2 pour jouer à deux");
            joueur = sc.nextLine();
        }
        return joueur;
    } // Demande à combien de joueur l'utilisateur(s) veux/veulent jouer

    public static String demanderCoup(String j) {
        System.out.println(j + " à vous de jouer. Saisir une ligne entre 1 et 8 suivie d'une lettre entre A et H (ex : 3 D) ou P pour passer son tour");

        String entree = sc.nextLine();

        while (!entree.matches("^[1-8] [A-Ha-h]$") && !entree.equalsIgnoreCase("P")) {
            System.out.println("Entrée invalide. Assurez-vous de respecter le format");
            entree = sc.nextLine();
        }

        return entree;
    } // Demande le coup à l'utilisateur

    public static String rejoueroustop() {
        System.out.println("Voullez vous rejouer R ou arrêter S ");

        String entree = sc.nextLine();

        while (!entree.matches("R|S")) {
            System.out.println("Choissiez de rejouer R ou d'arrêter S");
            entree = sc.nextLine();
        }

        return entree;
    } // Demande si l'utilisateur veut rejouer ou non

    public void afficherMessage(String message) {
        System.out.println(message);
    } // Permet d'affiche un message

    public static void afficherEtatPartie(String p) {
        System.out.println(p);
    } // Affiche la tableau courant

    public static void afficherResultats(String nomj1, String nomj2, int pionsJoueur1, int pionsJoueur2, String vainqueur) {
        System.out.println(nomj1 + " a " + pionsJoueur1 + " pions.");
        System.out.println(nomj2 + " a " + pionsJoueur2 + " pions.");
        System.out.println("Le vainqueur est : " + vainqueur);
    }  // Affiche le résultat d'une partie

    public static void afficherScoreGlobal(String nomj1, String nomj2, int scoreJoueur1, int scoreJoueur2, String vainqueurGlobal) {
        System.out.println(nomj1 + " a gagné " + scoreJoueur1 + " parties.");
        System.out.println(nomj2 + " a gagné " + scoreJoueur2 + " parties.");
        System.out.println("Le gagnant global est : " + vainqueurGlobal);
    } // Affiche le résultat de toutes les parties


}





