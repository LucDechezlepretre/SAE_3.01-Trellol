package trellol.trellol;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * La classe Tache représent dans l'appication, une tâche et toutes ses informations.
 * Tache implémente l'interface Serializable pour le transfert de données lors du DnD
 */
public class Tache implements Serializable{

    /**
     * Numero de la tache créée
     */
    public static int NUMERO;

    /**
     * Format de la date souhaité
     */
    public final static String pattern = "dd/MM/yyyy";

    /**
     * Création d'un objet SimpleDateFormat avec le format spécifié
     */
    public static SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
    /**
     * Nom de la tache
     */
    private String nom;
    /**
     * Attribut statique représentant l'état archivée pour une tâche
     */
    public static final String ETAT_ARCHIVE = "Archive";
    /**
     * Attribut statique représentant l'état non-archivée pour une tâche
     */
    public static final String ETAT_NON_ARCHIVE = "Non-archive";
    /**
     * Attribut statique représentant l'état initial pour une tâche
     */
    public static final String ETAT_INITIAL = "Initial";
    /**
     * Attribut statique représentant l'état suprimé pour une tâche
     */
    public static final String ETAT_SUPPRIME = "Supprime";
    /**
     * Attribut statique représentant l'état de la tache parent de toutes les tâches archivées
     */
    public static final String ETAT_PARENT_ARCHIVE = "ParentArchive";
    /**
     * Date où commence la tâche
     */
    private Date dateDebut;
    /**
     * Durée de la tâche
     */
    private int duree;
    /**
     * Etat de la tâche à un instant t
     */
    private String etat;
    /**
     * Description de la tâche
     */
    private String description;
    /**
     * Attribut représentant l'importance de la tâche basé sur la classe Importance
     */
    private int importance;
    /**
     * Parent de la tâche actuel
     */
    private Tache parent;
    /**
     * Antécédent de la tâche actuel
     */
    private Tache antecedant;

    /**
     * Constructeur de la classe
     * @param nom nom de la tâche
     * @param date date prise en String puis convertie en objet Date
     * @param duree durée de la tâche
     * @param description description de la tâche
     * @param importance importance de la tâche
     */
    public Tache(String nom, String date, int duree, String description, int importance){
        this.nom = nom;
        try{
            this.dateDebut = Tache.dateFormat.parse(date);
        } catch (ParseException e) {
            System.out.println("Mauvais format de date");
            this.dateDebut = new Date();
        }
        this.duree = duree;
        this.description = description;
        this.importance = importance;
        this.etat = Tache.ETAT_INITIAL;
    }

    /**
     * Getter de l'attribut nom
     * @return String représentant le nom de la tâche
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter pour l'attribut nom
     * @param nom nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
    /**
     * Getter de l'attribut dateDebut
     * @return l'objet Date de la tâche
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * Setter pour l'attribut dateDebut
     * @param dateDebut nouvelle date en String
     */
    public void setDateDebut(String dateDebut) {
        try{
            this.dateDebut = Tache.dateFormat.parse(dateDebut);
        } catch (ParseException e) {
            System.out.println("Mauvais format de date");
            this.dateDebut = new Date();
        }
    }

    /**
     * Setter pour l'attribut dateDebut
     * @param dateDebut nouvelle date en Date
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut=dateDebut;
    }

    /**
     * Getter de l'attribut duree
     * @return la durée de la tâche
     */
    public int getDuree() {
        return duree;
    }

    /**
     * Setter de l'attribut duree
     * @param duree nouvelle duree
     */
    public void setDuree(int duree) {
        this.duree = duree;
    }

    /**
     * Getter de l'attibut etat
     * @return int représentant l'état de la tâche
     */
    public String getEtat() {
    	return this.etat;
    }
    /**
     * Getter de l'attibut Description
     * @return String représentant la description de la tâche
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter de l'attribut description
     * @param description nouvelle description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter de l'attribut importance
     * @return int représentant l'importance
     */
    public int getImportance() {
        return importance;
    }

    /**
     * Setter de l'attribut importance
     * @param importance nouvelle valeur pour importance
     */
    public void setImportance(int importance) {
        this.importance = importance;
    }

    /**
     * Getter de l'attribut parent
     * @return un objet Tache représentant le parent
     */
    public Tache getParent() {
        return parent;
    }

    /**
     * Setter de l'attribut parent
     * @param parent nouvelle Tache parent
     */
    public void setParent(Tache parent) {
        this.parent = parent;
    }

    /**
     * Getter de l'attribut antecedent
     * @return l'antécécent actuel de la tâche
     */
    public Tache getAntecedant() {
        return antecedant;
    }

    /**
     * Setter de l'attribut antecedent
     * @param antecedant le nouvel antécédent
     */
    public void setAntecedant(Tache antecedant) {
        this.antecedant = antecedant;
    }

    /**
     * Setter pour l'attribut etat
     * @param etat nouvel etat
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }

    /**
     * Méthode equals
     * @param o l'objet à comparer avec this
     * @return true si o et this sont égaux false sinon
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tache tache = (Tache) o;
        return duree == tache.duree && importance == tache.importance && Objects.equals(nom, tache.nom) && Objects.equals(etat, tache.etat) && Objects.equals(dateDebut, tache.dateDebut) && Objects.equals(description, tache.description) && Objects.equals(parent, tache.parent) && Objects.equals(antecedant, tache.antecedant);
    }

    /**
     * Méthode retournant le hashCode de la Tache this
     * @return hash code de this
     */
    @Override
    public int hashCode() {
        return Objects.hash(nom, etat, dateDebut, duree, description, importance, parent, antecedant);
    }

    /**
     * toString de this
     * @return String représentant this
     */
    @Override
    public String toString() {
        return "Tache{" + nom + ' '  + duree + '}';
    }

}
