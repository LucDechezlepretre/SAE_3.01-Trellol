
package trellol.trellol.Modele;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import trellol.trellol.Historique;
import trellol.trellol.Vues.Observateur;
import trellol.trellol.Tache;

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
	private static final DataFormat customFormat = new DataFormat("application/x-java-serialized-object");

	public Model(){
		this.observateurs = new ArrayList<Observateur> ();
		this.ensTache = new ArrayList<Tache>();
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
		if (parent != null) {
			Boolean parentModifie = false;
			Date datemin = new Date(9999, 12, 31);
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
				parent.setDateDebut(Tache.dateFormat.format(datemin));
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
	}

	public void afficherHistorique() {
		System.out.println(historique);
	}

	public void ajouterTache(Tache tache) {
		if ((ensTache.size() == 0 && tache.getParent() == null) || tache.getParent() != null) {
			if (this.verifierUniciteNom(tache.getNom())) {
				ensTache.add(tache);
				if (tache.getParent() != null) {
					mettreAJourParent(tache.getParent());
				}
			} else {
				System.out.println("Nom de tâche déjà existant");
			}
		}else{
			System.out.println("Impossible d'ajouter la tache");
		}
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
	// Fonction utilitaire pour créer un TreeItem à partir d'une tâche
	private TreeItem<Tache> createTreeItem(Tache tache) {
		TreeItem<Tache> treeItem = new TreeItem<>(tache);
		return treeItem;
	}
	private void ajouterTacheRecursivement(TreeItem<Tache> parentItem, Tache tache){
		if(this.getEnfant(tache).size() == 0){
			return;
		}
		for(Tache enfant : this.getEnfant(tache)){
			// Crée un nouvel élément de TreeItem pour la tâche
			TreeItem<Tache> childItem = createTreeItem(enfant);

			// Ajoute l'élément enfant à l'élément parent
			parentItem.getChildren().add(childItem);

			// Appelle récursivement la fonction pour ajouter des sous-tâches à cet élément enfant
			ajouterTacheRecursivement(childItem, enfant);
		}
	}
	public TreeView<Tache> affichageListe(){
		TreeItem<Tache> racine = createTreeItem(this.getRacine());
		ajouterTacheRecursivement(racine, this.ensTache.get(0));
		// Crée le TreeView avec l'élément racine
		TreeView<Tache> treeView = new TreeView<>(racine);
		// Enable cell reordering
		treeView.setCellFactory(param -> {
			TreeCell<Tache> cell = new TreeCell<Tache>() {
				@Override
				protected void updateItem(Tache tache, boolean empty) {
					// une TreeCell peut changer de tâche, donc changer de TreeItem
					super.updateItem(tache, empty);
					setText(empty ? null : tache.toString());
				}
			};

			cell.setOnDragDetected(event -> {
				if (!cell.isEmpty()) {
					Dragboard dragboard = cell.startDragAndDrop(TransferMode.MOVE);

					ClipboardContent content = new ClipboardContent();
					content.put(customFormat, cell.getItem());
					dragboard.setContent(content);

					event.consume();
				}
			});

			cell.setOnDragOver(event -> {
				Dragboard dragboard = event.getDragboard();

				if (dragboard.hasContent(customFormat) && !cell.isEmpty()) {
					event.acceptTransferModes(TransferMode.MOVE);
				}

				event.consume();
			});

			cell.setOnDragDropped(event -> {
				Dragboard dragboard = event.getDragboard();

				if (dragboard.hasContent(customFormat)) {
					Tache draggedItem = (Tache) dragboard.getContent(customFormat);
					TreeItem<Tache> draggedTreeItem = new TreeItem<>(draggedItem);

					TreeItem<Tache> parentItem = treeView.getSelectionModel().getSelectedItem();
					System.out.println(draggedItem);
					System.out.println(cell.getItem());
					this.deplacerTache(draggedItem, cell.getItem());
					this.notifierObservateurs();
					System.out.println(this);

					/*// Ensure we are not dropping onto the same item or its children
					if (parentItem != null && !parentItem.equals(draggedTreeItem) && !containsItem(parentItem, draggedItem)) {
						parentItem.getChildren().add(draggedTreeItem);
						event.setDropCompleted(true);

						// Remove the dragged item from its original position
						TreeItem<String> sourceParent = findParent(rootItem, draggedItem);
						if (sourceParent != null) {
							sourceParent.getChildren().remove(draggedTreeItem);
						}*/
				} else {
					event.setDropCompleted(false);
				}

				event.consume();
			});
			return cell;
		});
		return treeView;
	}

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
}
