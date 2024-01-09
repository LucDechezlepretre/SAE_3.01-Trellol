package trellol.trellol.Controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Vues.*;

public class ControleurOuvertureFichier implements EventHandler<ActionEvent> {
    private Modele modele;
    private Stage stage;

    public ControleurOuvertureFichier(Modele m, Stage s){
        this.modele = m;
        this.stage = s;
    }
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
            this.modele.setEnsTache(Modele.charger(selectedFile.getAbsolutePath()));

            this.modele.notifierObservateurs();
            System.out.println(this.modele);
        }

    }
}
