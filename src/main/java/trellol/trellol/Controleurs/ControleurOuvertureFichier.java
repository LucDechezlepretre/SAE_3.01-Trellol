package trellol.trellol.Controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Vues.*;

/**
 * Classe controleur permettant le controle lors de l'ouverture d'un fichier
 */
public class ControleurOuvertureFichier implements EventHandler<ActionEvent> {
    /**
     * Attribut modele que le controleur modifiera
     */
    private Modele modele;
    /**
     * Attribut stage depuis lequel le contrôleur ouvrir l'explorateur de fichier
     */
    private Stage stage;

    /**
     * Constructeur du controleur
     * @param m Modele
     * @param s Stage
     */
    public ControleurOuvertureFichier(Modele m, Stage s){
        this.modele = m;
        this.stage = s;
    }

    /**
     * Redéfinition de la méthode handle
     * @param actionEvent evenement
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        // Créer une boîte de dialogue de choix de fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");

        // Définir l'extension de fichier obligatoire
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers trellol (*.trellol)", "*.trellol");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher la boîte de dialogue et attendre que l'utilisateur choisisse un fichier
        java.io.File selectedFile = fileChooser.showOpenDialog(this.stage);

        // Si un fichier est choisi, afficher le chemin du fichier
        if (selectedFile != null) {
            System.out.println("Fichier choisi : " + selectedFile.getAbsolutePath());
            //On change la liste de tâche du modèle actuel par la nouvelle
            this.modele.setEnsTache(Modele.charger(selectedFile.getAbsolutePath()));

            this.modele.notifierObservateurs();
        }

    }
}
