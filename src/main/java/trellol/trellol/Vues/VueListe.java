package trellol.trellol.Vues;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import trellol.trellol.Modele.Model;
import trellol.trellol.Modele.Sujet;
import trellol.trellol.Tache;

public class VueListe extends Pane implements Observateur{
    private static Model model;
    private static final DataFormat customFormat = new DataFormat("application/x-java-serialized-object");
    @Override
    public void actualiser(Sujet s) {
        VueListe.model = (Model) s;
        this.getChildren().clear();
        this.getChildren().add(this.affichageListe());
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
                    VueListe.model.deplacerTache(draggedItem, cell.getItem());
                    VueListe.model.notifierObservateurs();
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
}
