
package trellol.trellol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe correspondant au modele de l'architecture MVC 
 * de l'exemple compteur_MVC
 *
 */
public class Model implements Sujet {

	/**
	 * Liste des observateurs
	 */
	private ArrayList<Observateur> observateurs;

	private Historique h;
	private Tache ensTache;
	private List<Tache> ensTache;


	//private Historique historique;

	public Model(Tache t){
		this.observateurs = new ArrayList<Observateur> ();
		this.ensTache = new ArrayList<Tache>();
	}

	public List<Tache> getEnsTache() {
		return this.ensTache;
	}

	public List<Tache> getEnfant(Tache tache){
		List<Tache> enfants = new ArrayList<Tache>();
		for(Tache t : this.ensTache){
			if(t.getParent()  == tache){
				enfants.add(t);
			}
		}
		return enfants;
	}
	public Tache getRacine(){
		Tache racine = null;
		for(Tache t: this.ensTache){
			if(t.getParent() == null){
				racine = t;
			}
		}
		return racine;
	}
	public void ajouterEnfant(Tache parent, Tache enfant){

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
		tache.setEtat("Archive");
	}

	public void desarchiverTache(Tache tache) {
		tache.setEtat("NonArchive");
	}

	public void suppressionTache(Tache tache) {
		tache.setEtat("Supprime");
		tache.setParent(null);
		tache.setAntecedant(null);
	}

	public void afficherHistorique() {
		System.out.println(h);
	}

}
