package trellol.trellol.Controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.Stage;
import trellol.trellol.Exceptions.AjoutTacheException;
import trellol.trellol.Importance;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Tache;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ControleurAjouterTache implements EventHandler<ActionEvent> {
    private Stage fenetre;
    private Modele modele;
    private TextField nom;
    private DatePicker date;
    private TextField duree;
    private TextArea description;
    private ComboBox<String> importance;
    private ComboBox<String> anter;
    private ComboBox<String> parent;

    private Label erreur;

    public ControleurAjouterTache(Modele m, Stage fenetre, TextField nom, DatePicker date, TextField duree, TextArea description, ComboBox<String> importance, ComboBox<String> anter, ComboBox<String> parent, Label erreur){
        this.fenetre=fenetre;
        this.nom=nom;
        this.date=date;
        this.duree=duree;
        this.description=description;
        this.importance=importance;
        this.anter=anter;
        this.parent=parent;
        this.erreur=erreur;
        this.modele=m;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        try{
            //Recuperation des donnees des fields
            String nom=this.nom.getText(); //NOM DE LA TACHE

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date=this.date.getValue(); //DATE DEBUT

            String description=this.description.getText(); //DESCRIPTION

            String libelImp=this.importance.getValue();  //IMPORTANCE

            String dureeText=this.duree.getText(); //DUREE

            this.verifierForm(nom, date, dureeText, libelImp); //LANCEMENT DE LA VERIFICATION DES DONNES


            //Creation de la tache
            int duree=Integer.parseInt(this.duree.getText());
            String dateString = date.format(formatter);
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

            this.modele.notifierObservateurs();
            this.fenetre.close();
        }
        catch(AjoutTacheException e){
            this.erreur.setText(e.getMessage());
        }
    }


    public void verifierForm(String nom, LocalDate date, String duree, String importance) throws AjoutTacheException{
        if(nom==""){
            throw new AjoutTacheException("Nom manquant");
        }
        if(date==null){
            throw new AjoutTacheException("Date de début manquante");
        }
        if(duree==""){
            throw new AjoutTacheException("Durée manquante");
        }
        if(importance==null){
            throw new AjoutTacheException("Importance manquante");
        }
    }
}
