package modele;

import modele.Strategie;
import modele.othello.CoupOthello;
import modele.othello.PartieOthello;

import java.util.List;
import java.util.Random;

public class OrdinateurFacile implements Strategie {



    @Override
    public CoupOthello choixCoupIa(List<CoupOthello> coupsOrdinateurPossible, PartieOthello p) {
        CoupOthello coupOthelloIa = null;
        Random aleatoire = new Random();
        int indexAleatoire = aleatoire.nextInt(coupsOrdinateurPossible.size());
        coupOthelloIa = coupsOrdinateurPossible.get(indexAleatoire);
        return coupOthelloIa;
    }


}
