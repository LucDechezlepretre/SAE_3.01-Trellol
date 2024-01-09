
package trellol.trellol.Modele;
import trellol.trellol.Exceptions.AjoutTacheException;
import trellol.trellol.Historique;
import trellol.trellol.Vues.Observateur;
import trellol.trellol.Tache;

import java.io.*;
import java.util.*;

/**
 * Classe correspondant au modele de l'architecture MVC
 */
public class Modele implements Sujet, Serializable {
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

	private List<Tache> TacheSelectGantt;

	/**
	 * Constructeur du modele, initialise la liste d'observateurs et de tâches
	 */
	public Modele(){
		this.observateurs = new ArrayList<Observateur> ();
		this.ensTache = new ArrayList<Tache>();
		this.TacheSelectGantt = new ArrayList<Tache>();
		historique = new Historique();
	}

	/**
	 * Getter de l'attribut ensTache
	 * @return la liste contenant toutes les tâches
	 */
	public List<Tache> getEnsTache() {
		return this.ensTache;
	}

	public void setEnsTache(List<Tache> ensTache) {
		this.ensTache = ensTache;
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
	 * Méthode recursive permettant le desarchivage d'une tâche et de ses enfants
	 * @param tache tâche à desarchiver
	 */
	public void desarchiverTache(Tache tache) {
		tache.setEtat(Tache.ETAT_INITIAL);

		ArrayList<Tache> enfants= (ArrayList)this.getEnfant(tache);
		for(Tache t : enfants){
			this.desarchiverTache(t);
		}

		this.getHistorique().addAction(Historique.DESARCHIVAGE_ACTION, tache.getNom());
		this.notifierObservateurs();

	}

	/**
	 * Méthode recursive permettant la suppression d'une tâche et de ses enfants du modèle
	 * @param tache tâche à supprimer
	 */
	public void suppressionTache(Tache tache) {
		this.getHistorique().addAction(Historique.SUPRESSION_ACTION, tache.getNom());
		this.ensTache.remove(tache);

		ArrayList<Tache> enfants= (ArrayList)this.getEnfant(tache);

		for(Tache t : enfants){
			this.suppressionTache(t);
		}
		this.notifierObservateurs();
	}

	/**
	 * Méthode récursive permettant de vérifier entre toute l'arborescence d'une tache et de la racine
	 * si il y a des antécédent de la tâche parmis l'arborescence. Si il y a un lien d'antériorité
	 * avec une des tâches parent le lien est effacé.
	 * @param tache tâche pour laquelle on veut vérifier qu'elle n'est pas antécédent parmis l'arborescence
	 * @param parent tâche parent pour laquelle on veut faire la vérification
	 */
	public void parentNotAntecedent(Tache tache, Tache parent){
		if(parent.getAntecedant() != null){
			if(parent.getAntecedant().equals(tache)){
				parent.setAntecedant(null);
			}
			else if(parent.getParent() != null){
				parentNotAntecedent(tache, parent.getParent());
			}
		}
		else if(parent.getParent() != this.getRacine()){
			parentNotAntecedent(tache, parent.getParent());
		}
	}
	/**
	 * Méthode permettant le déplacement d'une tache, c-à-d le changement de parent
	 * @param tache tâche à déplacer
	 * @param parent nouveau parent de la tâche
	 */
	public void deplacerTache(Tache tache, Tache parent) {
		Tache rechercheSiLienParent = tache.getParent();
		if (!tache.equals(this.getRacine())) {
				if (!tache.getNom().equals(parent.getNom())) {
					parentNotAntecedent(tache, parent);
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
			this.generationDate(tache);
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

				this.generationDate(tache);
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
	 * Méthode de calcul de durée d'une tâche en prenant compte de ses sous-tâches (enfants)
	 * @param tache tâche pour laquelle la durée va être calculée
	 * @return int représentant la durée de la tâche
	 */
	public int calculerDureeTache(Tache tache){
		int duree = tache.getDuree();
		for(Tache t : this.getEnfant(tache)){
			if(t.getEtat() == Tache.ETAT_INITIAL) {
				duree += calculerDureeTache(t);
			}
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

	/**
	 * Donne une date automatique valide à une tache ayant un antecedant et/ou un parent
	 */
	public void generationDate(Tache t){
		Date dAntecedent=new Date(0);
		if(t.getAntecedant()!=null){
			Date dateDeb=t.getAntecedant().getDateDebut();
			int duree=t.getAntecedant().getDuree();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateDeb);

			calendar.add(Calendar.DAY_OF_MONTH, duree);
			dAntecedent = calendar.getTime();
		}

		Date dParent=new Date(0);
		if(t.getParent()!=null && t.getParent().equals(this.getRacine())==false){

			dParent=t.getParent().getDateDebut();
		}

		//COMPARAISONS
		Date dateActu=t.getDateDebut();
		if(dateActu.compareTo(dParent)<0){
			dateActu=dParent;
		}

		if(dateActu.compareTo(dAntecedent)<0){
			dateActu=dAntecedent;
		}

		t.setDateDebut(dateActu);
	}

	public Date getDateFinProjet() {
		Date datefin = this.getRacine().getDateDebut();
		Date dateTacheT;
		for (Tache t : this.ensTache) {
			dateTacheT = new Date(t.getDateDebut().getTime() + (1000 * 60 * 60 * 24 * this.calculerDureeTache(t)));
			if (datefin.before(dateTacheT)) {
				datefin = dateTacheT;
			}
		}
		return datefin;
	}

	public void sauvegarder(String chemin, String nom){
		try {
			//Cree un flux de sortie (fichier puis flux d'objet)
			FileOutputStream outputStream = new FileOutputStream(chemin+nom+".trellol");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(this);
			objectOutputStream.close();
			System.out.println("Objet enregistré avec succès dans le fichier.");
		}
		catch (IOException e){
			System.out.println("Erreur d'E/S");
			//System.out.println(e);

		}
		catch (Exception e){
			System.out.println("Erreur hors d'E/S");
		}
	}
	public static Modele charger(String chemin){
		Modele modele = new Modele();
		try{
			//Cree flux de lecture
			FileInputStream in = new FileInputStream(chemin);
			ObjectInputStream oin = new ObjectInputStream(in);
			modele.setEnsTache((List<Tache>) oin.readObject());
		}
		catch (IOException e){
			System.out.println("Erreur d'E/S");
			System.out.println(e.getMessage());
		}
		catch (ClassCastException e){
			System.out.println("Erreur de cast");
		}
		catch (Exception e){
			System.out.println("Erreur hors cast et E/S");
		}
		return modele;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Modele modele = (Modele) o;
		return Objects.equals(historique, modele.historique) && Objects.equals(ensTache, modele.ensTache);
	}

	@Override
	public int hashCode() {
		return Objects.hash(historique, ensTache);
	}
	public void supprimerListeGantt(Tache t) {
		this.TacheSelectGantt.remove(t);
		notifierObservateurs();
	}

	public void ajouterListeGantt(Tache t) {
		this.TacheSelectGantt.add(t);
		notifierObservateurs();
	}

	public List<Tache> getTacheSelectGantt() {
		return TacheSelectGantt;
	}
}
