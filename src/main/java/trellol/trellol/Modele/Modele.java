
package trellol.trellol.Modele;
import trellol.trellol.Exceptions.AjoutTacheException;
import trellol.trellol.Historique;
import trellol.trellol.Vues.Observateur;
import trellol.trellol.Tache;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

	private Set<Tache> TacheSelectGantt;

	/**
	 * Constructeur du modele, initialise la liste d'observateurs et de tâches
	 */
	public Modele(){
		this.observateurs = new ArrayList<Observateur> ();
		this.ensTache = new ArrayList<Tache>();
		this.TacheSelectGantt = new HashSet<Tache>();
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
	 * Setter de l'attribut ensTache
	 * @param ensTache la nouvelle liste de tache
	 */
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
		boolean premierAppel=tache.getEtat().equals(Tache.ETAT_ARCHIVE);

		tache.setEtat(Tache.ETAT_INITIAL);

		ArrayList<Tache> enfants= (ArrayList)this.getEnfant(tache);
		for(Tache t : enfants){
			this.desarchiverTache(t);
		}

		this.getHistorique().addAction(Historique.DESARCHIVAGE_ACTION, tache.getNom());

		if(premierAppel) {
			this.notifierObservateurs();
		}

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
		if (parent == null){
			return;
		}
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
			if(t.getEtat().equals(Tache.ETAT_INITIAL)){

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
			int duree= this.calculerDureeTache(t.getAntecedant());

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

	/**
	 * Calcule la date à laquelle la dernière tache du projet finira
	 * @return la date de fin du projet
	 */
	public Date getDateFinTache(Tache tache) {
		Date datefin = this.getRacine().getDateDebut();
        if (this.getEnfant(tache).size() == 0) {
            datefin = new Date(tache.getDateDebut().getTime() + (1000 * 60 * 60 * 24 * this.calculerDureeTache(tache)));
        } else {
            for (Tache t : this.getEnfant(tache)) {
                if (datefin.before(getDateFinTache(t))) {
                    datefin = getDateFinTache(t);
                }
            }
        }
		if (TimeUnit.DAYS.convert(Math.abs(datefin.getTime() - tache.getDateDebut().getTime()), TimeUnit.MILLISECONDS) < this.calculerDureeTache(tache)) {
			datefin = new Date(tache.getDateDebut().getTime() + (1000 * 60 * 60 * 24 * this.calculerDureeTache(tache)));
		}
		System.out.println(tache.getNom()+ " "+ datefin);
		return datefin;
	}

	/**
	 * Méthode permettant la sauvegarde de la liste des tâches dans un fichier
	 * @param chemin chemin absolue ou relatif vers lequel on veut enregistrer la liste des tâches
	 */
	public void sauvegarder(String chemin){
		try {
			//Cree un flux de sortie (fichier puis flux d'objet)
			FileOutputStream outputStream = new FileOutputStream(chemin);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(this.getEnsTache());
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

	/**
	 * Méthode permettant le chargement d'une liste de tâches depuis un fichier
	 * @param chemin chemin du fichier à charger
	 * @return la liste des tâches chargées
	 */
	public static List<Tache> charger(String chemin){
		List<Tache> ensTache = new ArrayList<Tache>();
		try{
			//Cree flux de lecture
			FileInputStream in = new FileInputStream(chemin);
			ObjectInputStream oin = new ObjectInputStream(in);
			ensTache = (List<Tache>) oin.readObject();
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
		return ensTache;
	}

	/**
	 * Méthode equals pour vérifier l'égalité entre this et un autre objet modele
	 * @param o objet avec lequel comparer this
	 * @return true si les objets sont égaux, false sinon
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Modele modele = (Modele) o;
		return Objects.equals(ensTache, modele.ensTache);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ensTache);
	}

	/**
	 * Retire une tache et ses enfants de la liste des tâches à afficher dans le diagramme de Gantt
	 * @param t la tâche à supprimer du diagramme (ainsi que ses enfants)
	 */
	public void supprimerListeGantt(Tache t) {
		this.TacheSelectGantt.remove(t);
		for (Tache tache: this.getEnfant(t)) {
			this.supprimerListeGantt(tache);
		}
		notifierObservateurs();
	}

	/**
	 * Ajoute une tache et ses enfants de la liste des tâches à afficher dans le diagramme de Gantt
	 * @param t la tâche à ajouter du diagramme (ainsi que ses enfants)
	 */
	public void ajouterListeGantt(Tache t) {
		this.TacheSelectGantt.add(t);
		for (Tache tache: this.getEnfant(t)) {
			this.ajouterListeGantt(tache);
		}
		notifierObservateurs();
	}

	/**
	 * Retire les tâches en double et archivée de la liste des tâches à afficher dans le diagramme de Gantt
	 */
	public void filtrerGantt() {
		HashSet<Tache> nouvTacheSelectGannt = new HashSet();
		for (Tache t: this.TacheSelectGantt) {
			if (t.getEtat().equals(Tache.ETAT_INITIAL) && this.ensTache.contains(t)) {
				nouvTacheSelectGannt.add(t);
			}
		}
		this.TacheSelectGantt = nouvTacheSelectGannt;
	}

	/**
	 * Met à jour les dates des tâches du modèle
	 */
	public void actualiserDates(Tache racine){
		this.generationDate(racine);

		for(Tache t : this.getEnfant(racine)){
			this.actualiserDates(t);
		}
	}

	/**
	 * Getter de la liste des tâches à afficher dans le diagramme de Gantt
	 * @return
	 */
	public Set<Tache> getTacheSelectGantt() {
		return TacheSelectGantt;
	}
}
