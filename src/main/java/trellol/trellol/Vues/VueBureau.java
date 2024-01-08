package trellol.trellol.Vues;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Modele.Sujet;
import trellol.trellol.Tache;

/**
 * Classe VueBureau permettant la représentation des tâches sous forme d'un
 * affichage "Bureau" d'un modèle, héritant de la classe Tab pour permettre
 * un affichage sous la forme d'un onglet
 * et implémentant Observateur pour pouvoir être enregistrer auprès d'un Sujet
 */
public class VueBureau extends Tab implements Observateur {
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
    public VueBureau(String nom, Sujet s) {
        super(nom);
        VueBureau.model = (Modele) s;
        StackPane conteneur = new StackPane();
        if(VueBureau.model.getEnsTache().size() > 0) {
            conteneur = new StackPane(this.createRecursiveGridPane(model.getRacine()));
        }
        this.setContent(conteneur);
    }
    /**
     * Redéfinition de la méthode reçu de l'interface Observateur
     * @param s sujet qui servira à la modélisation des données
     */
    @Override
    public void actualiser(Sujet s) {
        VueBureau.model =(Modele) s;
        StackPane content = (StackPane) this.getContent();
        // Créer une barre de défilement horizontal et vertical avec un ScrollPane
        content.getChildren().clear();
        ScrollPane scrollPane = new ScrollPane();
        if(VueBureau.model.getEnsTache().size() > 0) {
            scrollPane.setContent(this.createRecursiveGridPane(VueBureau.model.getRacine()));
        }
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        content.getChildren().add(scrollPane);
    }

    /**
     * Méthode qui construit récursivement un GridPane représentant les tâches du
     * sujet, chaque tâche est représenté par un GridPane
     * @param tache la tâche pour laquelle on construit un GridPane (et donc récursivement
     *              pour ses enfants aussi)
     * @return GridPane représentation de toutes les tâches
     */
    private GridPane createRecursiveGridPane(Tache tache) {

        GridPane gp = new GridPane();

        String nomTache = tache.getNom();
        this.model.getEnfant(this.model.getRacine());
        gp.add(new Label(tache.getNom()),1,1);

        gp.setHgap(5);
        gp.setVgap(5);
        gp.setPadding(new Insets(10));

        gp.setStyle("-fx-border-color: black;"); // Ajout d'une bordure pour mieux visualiser
        boolean rang2 = this.model.getEnfant(this.model.getRacine()).contains(tache);
        boolean racine = tache == this.model.getRacine();
        int nbColonne = 0;
        if (this.model.getEnfant(tache).size() > 0) {
            int colonne = 1;
            if (racine) {
                colonne++;
            }
            int ligne = 2;
            // Appel récursif pour le VBox interne
            for (Tache t : this.model.getEnfant(tache)) {
                if (t.getEtat().equals(Tache.ETAT_INITIAL)){
                    GridPane gpt = createRecursiveGridPane(t);
                    gpt.setOnMouseClicked(mouseEvent -> {
                        FenetreTache.afficherFormulaireTache(VueBureau.model, t.getNom(), true);
                    });
                    gp.add(gpt, colonne, ligne);
                    if (racine) {
                        colonne++;
                    } else {
                        ligne++;
                    }
                }
                nbColonne++;
            }
        }
        if(rang2) {
            Button bAjoutTache = new Button("Ajouter Tache");
            gp.add(bAjoutTache,1,gp.getRowCount());
            bAjoutTache.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    FenetreTache.afficherFormulaireTache(VueBureau.model,tache.getNom(), false);
                }
            });
        }
        //Gestion du DnD
        //Détection du Drag
        gp.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //On met les données dans un objet ClipboardContent
                Dragboard db = gp.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.put(MainAffichage.customFormatListe,model.findTacheByName(nomTache));
                //Puis on donne les données au Dragboard pour le transfert
                db.setContent(content);
                mouseEvent.consume();
            }
        });
        //Pendant le déplacement des données
        gp.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                //On accepte le déplacement
                dragEvent.acceptTransferModes(TransferMode.MOVE);
                dragEvent.consume();
            }
        });
        //Gestion du Drop
        gp.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                //On récupère le contenu du DragBoard
                Dragboard db = dragEvent.getDragboard();
                Tache t = (Tache) db.getContent(MainAffichage.customFormatListe);
                boolean success = false;
                if (t != null) {
                    //On met à jour le modèle
                    model.deplacerTache(t, model.findTacheByName(nomTache));
                    model.notifierObservateurs();
                    success = true;
                }
                //Fin du Drag and Drop
                dragEvent.setDropCompleted(success);
                dragEvent.consume();
            }
        });
        return gp;
    }
}
