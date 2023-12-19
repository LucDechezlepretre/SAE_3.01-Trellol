package trellol.trellol.Controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import trellol.trellol.Importance;
import trellol.trellol.Modele.Model;
import trellol.trellol.Tache;

import java.time.format.DateTimeFormatter;

public class ControleurAjouterTache implements EventHandler<ActionEvent> {
    private Model modele;
    private TextField nom;
    private DatePicker date;
    private TextField duree;
    private TextArea description;
    private ComboBox<String> importance;
    private ComboBox<String> anter;
    private ComboBox<String> parent;

    public ControleurAjouterTache(Model m, TextField nom, DatePicker date, TextField duree, TextArea description, ComboBox<String> importance, ComboBox<String> anter, ComboBox<String> parent){
        this.nom=nom;
        this.date=date;
        this.duree=duree;
        this.description=description;
        this.importance=importance;
        this.anter=anter;
        this.parent=parent;
        this.modele=m;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        //Creation de la tache
        String nom=this.nom.getText(); //NOM DE LA TACHE

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String dateString = this.date.getValue().format(formatter); //DATE DEBUT

        System.out.println(dateString);

        int duree=Integer.parseInt(this.duree.getText()); //DUREE

        String description=this.description.getText(); //DESCRIPTION

        String libelImp=this.importance.getValue();  //IMPORTANCE
        int importance;
        if(libelImp.equals("faible")){
            importance= Importance.FAIBLE;
        }
        else if(libelImp.equals("moyenne")){
            importance=Importance.MOYENNE;
        }
        else{
            importance=Importance.ELEVEE;
        }

        Tache tache=new Tache(nom, dateString, duree,description, importance);

        //Ajout anterieur
        String nomAnter=this.anter.getValue();
        if(nomAnter!=null){
            Tache anter=this.modele.findTacheByName(nomAnter);

            tache.setAntecedant(anter);
        }

        //Ajout parent
        String nomParent=this.parent.getValue();
        if(nomParent!=null){
            Tache parent=this.modele.findTacheByName(nomParent);

            tache.setParent(parent);
        }
        this.modele.ajouterTache(tache);
    }
}
