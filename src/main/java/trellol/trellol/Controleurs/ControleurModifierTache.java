package trellol.trellol.Controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.Stage;
import trellol.trellol.Exceptions.AjoutTacheException;
import trellol.trellol.Historique;
import trellol.trellol.Importance;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Tache;
import trellol.trellol.Vues.MainAffichage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe controleur pour la modification d'une tache
 */
public class ControleurModifierTache implements EventHandler<ActionEvent> {
    /**
     * Attribut fenetre de type Stage représentant la fenetre avec tous les champs à récupérer
     */
    private Stage fenetre;
    /**
     * Attribut modele de type Modele représentant le modele auquel modifier la tâche
     */
    private Modele modele;
    /**
     * Attribut nom représentant le TextField pour récupérer le nom de la tâche
     */
    private TextField nom;
    /**
     * Attribut date représentant le DatePicker pour récupérer la date de début de la tâche
     */
    private DatePicker date;
    /**
     * Attribut duree représentant le TextField pour récupérer la durée de la tache
     */
    private TextField duree;
    /**
     * Attribut description représentant la TextArea pour récupérer la description de la tâche
     */
    private TextArea description;
    /**
     * Attrbut importance représentant la ComboBox pour récupérer l'importance de la tâche
     */
    private ComboBox<String> importance;
    /**
     * Attribut anter représentant la ComboBox pour récupérer la taĉhe antérieur à la tâche
     */
    private ComboBox<String> anter;
    /**
     * Attribut parent représentant la ComboBox pour récupérer la taĉhe parent de la tâche
     */
    private ComboBox<String> parent;
    /**
     * Attribut tacheMod de type Tache représentant la tache à modifier
     */
    private Tache tacheMod;
    /**
     * Attribut erreur représentant le Label pour indiquer une erreur ou un oubli pour la création de la tâche
     */
    private Label erreur;

    /**
     * Constructeur du controleur
     * @param m le modele auquel on modifie une tâche
     * @param t la tache à modifier
     * @param fenetre la fenetre avec tous les champs à récupérer
     * @param nom TextField pour récupérer le nom de la tâche
     * @param date DatePicker pour récupérer la date de début de la tâche
     * @param duree TextField pour récupérer la durée de la tache
     * @param description  TextArea pour récupérer la description de la tâche
     * @param importance ComboBox pour récupérer l'importance de la tâche
     * @param anter ComboBox pour récupérer la taĉhe antérieur à la tâche
     * @param parent ComboBox pour récupérer la taĉhe parent de la tâche
     * @param erreur Label pour indiquer une erreur ou un oubli pour la modification de la tâche
     */
    public ControleurModifierTache(Modele m, Tache t, Stage fenetre, TextField nom, DatePicker date, TextField duree, TextArea description, ComboBox<String> importance, ComboBox<String> anter, ComboBox<String> parent, Label erreur){
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
        this.tacheMod = t;
    }
    /**
     * Redéfinition de la méthode handle pour la gestion des évènements
     * @param actionEvent évènement récupéré par le controleur
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        try{
            modele.getHistorique().addAction(Historique.MODIFICATION_ACTION, tacheMod.getNom());
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
            this.tacheMod.setDuree(duree);
            this.tacheMod.setDescription(description);
            this.tacheMod.setNom(nom);
            this.tacheMod.setImportance(importance);
            this.tacheMod.setDateDebut(dateString);


            //Ajout anterieur
            String nomAnter=this.anter.getValue();
            if(nomAnter!=null){
                Tache anter=this.modele.findTacheByName(nomAnter);

                this.tacheMod.setAntecedant(anter);
            }

            //Ajout parent
            String nomParent=this.parent.getValue();
            if(nomParent!=null){
                Tache parent=this.modele.findTacheByName(nomParent);

                this.tacheMod.setParent(parent);
            }

            modele.generationDate(tacheMod);
            this.fenetre.close();
        }
        catch(AjoutTacheException e){
            this.erreur.setText(e.getMessage());
        }
        MainAffichage.affichageFormTache = false;

        modele.actualiserDates(modele.getRacine());
        modele.notifierObservateurs();
    }

    /**
     * Méthode qui vérifie la non-nullité des données transmisent par les formulaires
     * @param nom le nom de la tâche
     * @param date la date de début de la tâche
     * @param duree la durée de la tache
     * @param importance l'importance de la tache
     * @throws AjoutTacheException lance une exception si une donnée n'est pas bonne
     */
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
