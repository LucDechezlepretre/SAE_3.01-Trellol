
package trellol.trellol;
import java.util.ArrayList;
import java.util.Date;
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
	private Historique historique;
	private List<Tache> ensTache;

	public Model(){
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


	public void archiverTache(Tache tache) {
		tache.setEtat(Tache.ETAT_ARCHIVE);
	}

	public void desarchiverTache(Tache tache) {
		tache.setEtat(Tache.ETAT_NON_ARCHIVE);
	}

	public void suppressionTache(Tache tache) {
		tache.setEtat(Tache.ETAT_SUPPRIME);
		tache.setParent(null);
		tache.setAntecedant(null);
	}

	public void deplacerTache(Tache tache, Tache parent) {
		Tache ancienParent = tache.getParent();
		int index = ensTache.indexOf(tache);
		Tache t = ensTache.get(index);
		t.setParent(parent);
		ensTache.set(index, t);
		mettreAJourParent(parent);
		mettreAJourParent(ancienParent);
	}

	public void mettreAJourParent(Tache parent) {
		Boolean parentModifie = false;
		Date datemin = new Date(9999,12,31);
		int priomax = 0;
		int dureeTotale = 0;
		for (Tache enfant : this.getEnfant(parent)) {
			if (datemin.after(enfant.getDateDebut())) {
				datemin = enfant.getDateDebut();
			}
			if (priomax < enfant.getImportance()) {
				priomax = enfant.getImportance();
			}
			dureeTotale += enfant.getDuree();
		}
		if (!parent.getDateDebut().equals(datemin)) {
			parent.setDateDebut(datemin.toString());
			parentModifie = true;
		}
		if (parent.getImportance() != priomax) {
			parent.setImportance(priomax);
			parentModifie = true;
		}
		if (parent.getDuree() != dureeTotale) {
			parent.setDuree(dureeTotale);
			parentModifie = true;
		}
		if (parentModifie) {
			this.mettreAJourParent(parent.getParent());
		}
	}

	public void afficherHistorique() {
		System.out.println(historique);
	}

	public void ajouterTache(Tache tache) {
		if (ensTache.size() == 0 && tache.getParent() == null) {
			ensTache.add(tache);
		} else if (tache.getParent() != null) {
			ensTache.add(tache);
			mettreAJourParent(tache.getParent());
		}else{
			System.out.println("Impossible d'ajouter la tache");
		}
	}
}
