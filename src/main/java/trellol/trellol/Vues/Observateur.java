package trellol.trellol.Vues;

import trellol.trellol.Modele.Sujet;

/**
 * Interface pour les differents observateurs de Sujet
 *
 */
public interface Observateur {
	public void actualiser(Sujet s);
}
