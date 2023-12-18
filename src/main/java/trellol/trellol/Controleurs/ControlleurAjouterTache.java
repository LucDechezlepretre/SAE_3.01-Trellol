package trellol.trellol.Controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import trellol.trellol.Modele.Model;
import trellol.trellol.Vues.Affichage;

public class ControlleurAjouterTache implements EventHandler<ActionEvent> {
    private Model modele;
    private TextField nom;
    private DatePicker date;
    private TextField duree;
    private TextArea description;
    private ComboBox<String> importance;
    private ComboBox<String> anter;

    public ControlleurAjouterTache(Model m, TextField nom, DatePicker date, TextField duree, TextArea description, ComboBox<String> importance, ComboBox<String> anter){
        this.nom=nom;
        this.date=date;
        this.duree=duree;
        this.description=description;
        this.importance=importance;
        this.anter=anter;
        this.modele=m;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
    }
}
