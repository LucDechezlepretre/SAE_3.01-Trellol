
package trellol.trellol.Modele;
import trellol.trellol.Exceptions.AjoutTacheException;
import trellol.trellol.Historique;
import trellol.trellol.Vues.Observateur;
import trellol.trellol.Tache;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe correspondant au modele de l'architecture MVC 
 * de l'exemple compteur_MVC
 *
 */
public class Modele implements Sujet {
	/**
	 * Liste des observateurs
	 */
	private ArrayList<Observateur> observateurs;
	private Historique historique;
	private List<Tache> ensTache;
	private int numColonneAffiche;

	public Modele(){
		this.observateurs = new ArrayList<Observateur> ();
		this.ensTache = new ArrayList<Tache>();
		historique = new Historique();
	}
	public List<Tache> getEnsTache() {
		return this.ensTache;
	}

	public List<Tache> getEnfant(Tache tache){
		List<Tache> enfants = new ArrayList<Tache>();
		for(int i = 1; i < this.ensTache.size(); i++){
			Tache t = this.ensTache.get(i);
			if (t.getParent().equals(tache)) {
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
		this.archiverEnfants(tache);
		this.getHistorique().addAction(Historique.ARCHIVAGE_ACTION, tache.getNom());
		this.notifierObservateurs();
	}

	private void archiverEnfants(Tache tache){
		ArrayList<Tache> enfants= (ArrayList)this.getEnfant(tache);
		for(Tache t : enfants){
			t.setEtat(Tache.ETAT_PARENT_ARCHIVE);
			this.archiverEnfants(t);
		}
	}

	public void desarchiverTache(Tache tache) {
		tache.setEtat(Tache.ETAT_INITIAL);
		this.getHistorique().addAction(Historique.DESARCHIVAGE_ACTION, tache.getNom());
		this.notifierObservateurs();

	}

	public void suppressionTache(Tache tache) {
		tache.setEtat(Tache.ETAT_SUPPRIME);
		tache.setParent(null);
		tache.setAntecedant(null);
		this.getHistorique().addAction(Historique.SUPRESSION_ACTION, tache.getNom());
		this.ensTache.remove(tache);
		this.notifierObservateurs();
	}

	public void deplacerTache(Tache tache, Tache parent) {
		if (!tache.equals(this.getRacine())) {
			if (!tache.getNom().equals(parent.getNom())) {
				if (parent.getParent() != null && parent.getParent().equals(tache)) {
					Tache ancienParent = tache.getParent();
					parent.setParent(ancienParent);
				}
				int index = ensTache.indexOf(tache);
				Tache t = ensTache.get(index);
				t.setParent(parent);
				ensTache.set(index, t);
				this.getHistorique().addAction(Historique.DEPLACEMENT_ACTION, tache.getNom());
				this.notifierObservateurs();
			}
		}
	}

	public void afficherHistorique() {
		System.out.println(historique);
	}

	public void ajouterTache(Tache tache) throws AjoutTacheException {
		if ((ensTache.size() == 0 && tache.getParent() == null) || tache.getParent() != null) {
			if (this.verifierUniciteNom(tache.getNom())) {
				ensTache.add(tache);
			} else {
				throw new AjoutTacheException("Nom de tâche déjà existant");
			}
		}else{
			throw new AjoutTacheException("Parent manquant");
		}
		this.getHistorique().addAction(Historique.CREATION_TACHE_ACTION, tache.getNom());
		this.notifierObservateurs();
	}

	@Override
	public String toString() {
		return afficherTache(this.getRacine(), 0);
	}
	private String afficherTache(Tache t, int profondeur){
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < profondeur; i++) {
			res.append("| ");
		}
		res.append(t+"\n");
		for(Tache enfant : this.getEnfant(t)){
			res.append(afficherTache(enfant, profondeur+1));
		}
		return res.toString();
	}

	/**
	 * Recupère la liste des tâches archivé, mais dans le diagramme c'est void alors jsp ce qu'on en fait
	 */
	public void ouvrirArchive() {
		ArrayList<Tache> archive = new ArrayList<Tache>();
		for (Tache enfant : this.ensTache) {
			if (enfant.getEtat().equals(Tache.ETAT_ARCHIVE)) {
				archive.add(enfant);
			}
		}
	}

	public int calculerDureeTache(Tache tache){
		int duree = tache.getDuree();
		for(Tache t : this.getEnfant(tache)){
			duree += 	calculerDureeTache(t);
		}
		return duree;
	}

	public boolean verifierUniciteNom(String nom) {
		for (Tache enfant : this.ensTache) {
			if (enfant.getNom().equals(nom)) {
				return false;
			}
		}
		return true;
	}

	public int getNumColonneAffiche() {
		return numColonneAffiche;
	}

	/**
	 * Methode retournant une tache du trello selon son nom
	 * @param nom nom de la tache recherchee
	 * @return la tache du nom recherche, null si elle n'existe pas
	 */
	public Tache findTacheByName(String nom){
		for(Tache t : this.ensTache){
			if(t.getNom().equals(nom)){
				return t;
			}
		}

		return null; //Aucune tache à ce nom n'a été trouvée
	}

	public Historique getHistorique() {
		return this.historique;
	}
}
