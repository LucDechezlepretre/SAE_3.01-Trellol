
package trellol.trellol;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe correspondant au modele de l'architecture MVC 
 * de l'exemple compteur_MVC
 *
 */
public class Model implements Sujet {

	/**
	 * Liste des observateurs
	 */
	private ArrayList<main.java.trellol.trellol.Observateur> observateurs;

	private Tache ensTache;


	//private Historique historique;

	public Model(Tache t){
		this.observateurs = new ArrayList<Observateur> ();
		this.ensTache = t;
	}

	public ArrayList<Tache> getEnsTache() {
		ArrayList<Tache> listTache = new ArrayList<Tache>();

		return listTache;
	}


	/**
	 * Ajoute un observateur a la liste
	 */
	public void enregistrerObservateur(Observateur o) {
		this.observateurs.add(o);
	}


	/**
	 * Supprime un observateur a la liste
	 */
	public void supprimerObservateur(Observateur o) {
		int i = this.observateurs.indexOf(o);
		if (i >= 0) {
			this.observateurs.remove(i);
		}
	}


	/**
	 * Informe tous les observateurs de la liste des
	 * modifications du  Trello en appelant leurs methodes actualiser
	 */
	public void notifierObservateurs() {
		for (int i = 0; i < this.observateurs.size(); i++) {
			Observateur observer = this.observateurs.get(i);
			observer.actualiser(this);
		}
	}

	//public void deplacer(int,int,int,int) {}

	public void archiverTache(Tache tache) {

	}
}
