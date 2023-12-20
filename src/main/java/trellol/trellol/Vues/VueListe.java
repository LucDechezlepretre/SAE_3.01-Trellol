package trellol.trellol.Vues;

import javafx.scene.control.Tab;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Modele.Sujet;
import trellol.trellol.Tache;

public class VueListe extends Tab implements Observateur {
    private static Modele model;
    private static final DataFormat customFormat = new DataFormat("application/x-java-serialized-object");

    public VueListe(Sujet s) {
        VueListe.model = (Modele)s;
        StackPane conteneur = new StackPane(this.affichageListe());
        this.setContent(conteneur);
    }

    @Override
    public void actualiser(Sujet s) {
        VueListe.model =(Modele) s;
        StackPane content = (StackPane) this.getContent();
        content.getChildren().clear();
        content.getChildren().add(this.affichageListe());
    }

    // Fonction utilitaire pour créer un TreeItem à partir d'une tâche
    private TreeItem<Tache> createTreeItem(Tache tache) {
        TreeItem<Tache> treeItem = new TreeItem<>(tache);

        return treeItem;
    }
    private void ajouterTacheRecursivement(TreeItem<Tache> parentItem, Tache tache){
        if(VueListe.model.getEnfant(tache).size() == 0){
            return;
        }
        for(Tache enfant : VueListe.model.getEnfant(tache)){
            // Crée un nouvel élément de TreeItem pour la tâche
            TreeItem<Tache> childItem = createTreeItem(enfant);

            // Ajoute l'élément enfant à l'élément parent
            parentItem.getChildren().add(childItem);

            // Appelle récursivement la fonction pour ajouter des sous-tâches à cet élément enfant
            ajouterTacheRecursivement(childItem, enfant);
        }
    }
    public TreeView<Tache> affichageListe(){
        TreeItem<Tache> racine = createTreeItem(VueListe.model.getRacine());
        ajouterTacheRecursivement(racine, VueListe.model.getRacine());
        // Crée le TreeView avec l'élément racine
        TreeView<Tache> treeView = new TreeView<>(racine);
        treeView.setEditable(true);
        // Enable cell reordering
        treeView.setCellFactory(param -> {
            TreeCell<Tache> cell = new TreeCell<Tache>() {
                @Override
                protected void updateItem(Tache tache, boolean empty) {
                    // une TreeCell peut changer de tâche, donc changer de TreeItem
                    super.updateItem(tache, empty);
                    setText(empty ? null : tache.getNom()+" durée : "+model.calculerDureeTache(tache));
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

                    VueListe.model.deplacerTache(draggedItem, cell.getItem());
                    VueListe.model.notifierObservateurs();
                    //System.out.println(model);
                } else {
                    event.setDropCompleted(false);
                }
                event.consume();
            });
            return cell;
        });
        return treeView;
    }
}
