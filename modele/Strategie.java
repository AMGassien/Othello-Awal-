package modele;

import modele.othello.CoupOthello;
import modele.othello.PartieOthello;

import java.util.List;

public interface Strategie {

    public CoupOthello choixCoupIa(List<CoupOthello> coupOthello, PartieOthello p);
}
