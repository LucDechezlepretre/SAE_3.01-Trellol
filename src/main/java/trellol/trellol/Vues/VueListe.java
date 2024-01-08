package trellol.trellol.Vues;

import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Modele.Sujet;
import trellol.trellol.Tache;

/**
 * Classe VueBureau permettant la représentation des tâches sous forme d'un
 * affichage "Liste" d'un modèle, héritant de la classe Tab pour permettre
 * un affichage sous la forme d'un onglet
 * et implémentant Observateur pour pouvoir être enregistrer auprès d'un Sujet
 */
public class VueListe extends Tab implements Observateur {
    /**
     * Attribut model, représentant le modèle sur lequel se basera la vue pour se
     * construire
     */
    private static Modele model;

    /**
     * Constructeur de la vue
     * @param nom nom de l'onglet
     * @param s modèle sur lequel se basera la vue
     */
    public VueListe(String nom, Sujet s) {
        super(nom);
        VueListe.model = (Modele)s;
        StackPane conteneur = new StackPane();
        this.setContent(conteneur);
    }
    /**
     *  Redéfinition de la méthode reçu de l'interface Observateur
     *  qui met à jour la vue grâce aux données du modèle
     * @param s sujet sur lequel se base la vue
     */
    @Override
    public void actualiser(Sujet s) {
        VueListe.model =(Modele) s;
        StackPane content = (StackPane) this.getContent();
        content.getChildren().clear();
        // Créer une barre de défilement horizontal et vertical avec un ScrollPane
        content.getChildren().clear();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(this.affichageListe());
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        content.getChildren().add(scrollPane);
    }

    /**
     *  Méthode pour créer un TreeItem à partir d'une tâche
     * @param tache tâche pour laquelle on crée un TreeItem
     */
    private TreeItem<Tache> createTreeItem(Tache tache) {
        TreeItem<Tache> treeItem = new TreeItem<>(tache);
        return treeItem;
    }

    /**
     * Méthode récursive qui crée un TreeItem représentant les tâches et leurs
     * sous-tâches
     * @param parentItem le TreeItem parent, donc celui sur lequel on se basera pour construit le
     *                   prochain
     * @param tache tâche pour laquelle on veut construire un TreeItem
     */
    private void ajouterTacheRecursivement(TreeItem<Tache> parentItem, Tache tache){
        if(VueListe.model.getEnfant(tache).size() == 0){
            return;
        }
        for(Tache enfant : VueListe.model.getEnfant(tache)){
            if(enfant.getEtat()!=Tache.ETAT_ARCHIVE) {
                // Crée un nouvel élément de TreeItem pour la tâche
                TreeItem<Tache> childItem = createTreeItem(enfant);

                // Ajoute l'élément enfant à l'élément parent
                parentItem.getChildren().add(childItem);

                // Appelle récursivement la fonction pour ajouter des sous-tâches à cet élément enfant
                ajouterTacheRecursivement(childItem, enfant);
            }
        }
    }
    public TreeView<Tache> affichageListe(){
        // Crée le TreeView avec l'élément racine
        TreeItem<Tache> racine = createTreeItem(VueListe.model.getRacine());
        this.ajouterTacheRecursivement(racine, VueListe.model.getRacine());
        //Création du TreeView à partir du TreeItem racine
        TreeView<Tache> treeView = new TreeView<>(racine);
        treeView.setEditable(true);
        // On active la modification des TreeView
        treeView.setCellFactory(param -> {
            TreeCell<Tache> cell = new TreeCell<Tache>() {
                @Override
                protected void updateItem(Tache tache, boolean empty) {
                    // une TreeCell peut changer de tâche, donc changer de TreeItem
                    super.updateItem(tache, empty);
                    setText(empty ? null : tache.getNom()+" durée : "+model.calculerDureeTache(tache));
                }
            };
            //Gestion du DnD
            //Détection du Drag
            cell.setOnDragDetected(event -> {
                //On vérifie que la cellule n'est pas viue
                if (!cell.isEmpty()) {
                    //On crée l'objet qui transportera les données contenue dans la cellule
                    Dragboard dragboard = cell.startDragAndDrop(TransferMode.MOVE);
                    //On fait en sorte que les données puissent être envoyé dans le DragBoard
                    ClipboardContent content = new ClipboardContent();
                    content.put(MainAffichage.customFormatListe, cell.getItem());
                    //On met les données dans le DragBoard
                    dragboard.setContent(content);

                    event.consume();
                }
            });

            //Pendant le déplacement des données
            cell.setOnDragOver(event -> {
                Dragboard dragboard = event.getDragboard();
                //Si le contenu n'est pas vide on accepte le transfert
                if (dragboard.hasContent(MainAffichage.customFormatListe) && !cell.isEmpty()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }

                event.consume();
            });

            //Gestion du Drop
            cell.setOnDragDropped(event -> {
                //On récupère le "transporteur"
                Dragboard dragboard = event.getDragboard();
                //Si il contient quelque chose on lance un traitement
                if (dragboard.hasContent(MainAffichage.customFormatListe)) {
                    //On récupère l'objet Tache
                    Tache draggedItem = (Tache) dragboard.getContent(MainAffichage.customFormatListe);
                    //On déplace la tache draggé dans celle sur laquelle elle a été droppé
                    //on récupère la tâche visé avec cell.getItem()
                    VueListe.model.deplacerTache(draggedItem, cell.getItem());
                    //System.out.println(model);
                } else {
                    event.setDropCompleted(false);
                }
                event.consume();
            });
            //Fin de la création de la cellule
            cell.setOnMouseClicked(mouseEvent -> {
                if(!cell.getItem().equals(VueListe.model.getRacine())) {
                    FenetreTache.afficherFormulaireTache(VueListe.model, cell.getItem().getNom(), true);
                }
            });
            return cell;
        });
        treeView.setPrefWidth(600);
        //Le TreeView est construit
        return treeView;
    }
}
