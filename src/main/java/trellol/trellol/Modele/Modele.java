
package trellol.trellol.Modele;
import trellol.trellol.Exceptions.AjoutTacheException;
import trellol.trellol.Historique;
import trellol.trellol.Vues.Observateur;
import trellol.trellol.Tache;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe correspondant au modele de l'architecture MVC
 */
public class Modele implements Sujet {
	/**
	 * Attribut observateurs ArrayList représentant la liste des observateurs
	 */
	private ArrayList<Observateur> observateurs;
	/**
	 * Attribut historique, Historique représentant l'historique
	 */
	private Historique historique;
	/**
	 * Attribut ensTache, List représentant l'ensemble des tâches du projet
	 */
	private List<Tache> ensTache;
	private int numColonneAffiche;

	/**
	 * Constructeur du modele, initialise la liste d'observateurs et de tâches
	 */
	public Modele(){
		this.observateurs = new ArrayList<Observateur> ();
		this.ensTache = new ArrayList<Tache>();
		historique = new Historique();
	}

	/**
	 * Getter de l'attribut ensTache
	 * @return la liste contenant toutes les tâches
	 */
	public List<Tache> getEnsTache() {
		return this.ensTache;
	}

	/**
	 * Méthode renvoyant tous les enfants d'une tâche
	 * @param tache parent
	 * @return liste de tache ayant le parent passé en paremètre
	 */
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

	/**
	 * Méthode permettant de récupérer tous les successeurs d'une tâche
	 * @param tache tâche dont l'on veut les successeurs
	 * @return la liste des successeurs
	 */
	public List<Tache> getSuccesseurs(Tache tache){
		List<Tache> successeurs = new ArrayList<Tache>();
		for(Tache t : this.ensTache){
			if(t.getAntecedant() != null) {
				if (t.getAntecedant().equals(tache)) {
					successeurs.add(t);
				}
			}
		}
		return successeurs;
	}

	/**
	 * Méthode renvoyant la tache racine, c-à-d la seule tâche sans parent
	 * @return la tache racine
	 */
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

	/**
	 * Méthode permettant l'achivage dans le modèle de la tâche passé en paramètre
	 * @param tache à archiver
	 */
	public void archiverTache(Tache tache) {
		tache.setEtat(Tache.ETAT_ARCHIVE);
		this.archiverEnfants(tache);
		this.getHistorique().addAction(Historique.ARCHIVAGE_ACTION, tache.getNom());
		this.notifierObservateurs();
	}

	/**
	 * Méthode recursive permettant l'achivage des enfants d'une tâche
	 * @param tache parent des enfants à archiver
	 */
	private void archiverEnfants(Tache tache){
		ArrayList<Tache> enfants= (ArrayList)this.getEnfant(tache);
		for(Tache t : enfants){
			t.setEtat(Tache.ETAT_PARENT_ARCHIVE);
			this.archiverEnfants(t);
		}
	}

	/**
	 * Méthode permettant le desarchivage d'une tâche
	 * @param tache tâche à desarchiver
	 */
	public void desarchiverTache(Tache tache) {
		tache.setEtat(Tache.ETAT_INITIAL);
		this.desarchiverEnfants(tache);
		this.getHistorique().addAction(Historique.DESARCHIVAGE_ACTION, tache.getNom());
		this.notifierObservateurs();

	}
	/**
	 * Méthode recursive permettant le desachivage des enfants d'une tâche
	 * @param tache parent des enfants à desarchiver
	 */
	private void desarchiverEnfants(Tache tache){
		ArrayList<Tache> enfants= (ArrayList)this.getEnfant(tache);
		for(Tache t : enfants){
			t.setEtat(Tache.ETAT_INITIAL);
			this.desarchiverEnfants(t);
		}
	}

	/**
	 * Méthode permettant la suppression d'une tâche du modèle
	 * @param tache tâche à supprimer
	 */
	public void suppressionTache(Tache tache) {
		this.getHistorique().addAction(Historique.SUPRESSION_ACTION, tache.getNom());
		this.ensTache.remove(tache);
		this.notifierObservateurs();
	}

	/**
	 * Méthode permettant le déplacement d'une tache, c-à-d le changement de parent
	 * @param tache tâche à déplacer
	 * @param parent nouveau parent de la tâche
	 */
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

	/**
	 * Méthode permettant l'ajout d'une tache dans le modèle
	 * @param tache tâche à ajouter
	 * @throws AjoutTacheException si la tache n'a pas de parent dans un modèle avec une racine déjà présente ou si
	 * le nom de la tâche existe déjà
	 */
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

		Tache.NUMERO+=1;
		this.getHistorique().addAction(Historique.CREATION_TACHE_ACTION, tache.getNom());
		this.notifierObservateurs();
	}

	/**
	 * Méthode toString du modèle
	 * @return String représentant le modèle
	 */
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

	/**
	 * Méthode de calcul de durée d'une tâche en prenant compte de ses sous-tâches (enfants)
	 * @param tache tâche pour laquelle la durée va être calculée
	 * @return int représentant la durée de la tâche
	 */
	public int calculerDureeTache(Tache tache){
		int duree = tache.getDuree();
		for(Tache t : this.getEnfant(tache)){
			duree += 	calculerDureeTache(t);
		}
		return duree;
	}

	/**
	 * Méthode vérifiant l'unicité d'un nom
	 * @param nom nom à vérifier
	 * @return true si aucune tâche n'a le même nom, false sinon
	 */
	public boolean verifierUniciteNom(String nom) {
		for (Tache enfant : this.ensTache) {
			if (enfant.getNom().equals(nom)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Getter de l'attribut numColonneAffiche
	 * @return int numColonneAffiche
	 */
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

	/**
	 * Getter de l'attribut historique
	 * @return l'objet historique
	 */
	public Historique getHistorique() {
		return this.historique;
	}
}
