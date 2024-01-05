package trellol.trellol;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Tache implements Serializable{
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
    public static final String ETAT_ARCHIVE = "Archive";
    public static final String ETAT_NON_ARCHIVE = "Non-archive";
    public static final String ETAT_INITIAL = "Initial";
    public static final String ETAT_SUPPRIME = "Supprime";
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

    public void setDateDebut(String dateDebut) {
        try{
            this.dateDebut = Tache.dateFormat.parse(dateDebut);
        } catch (ParseException e) {
            System.out.println("Mauvais format de date");
            this.dateDebut = new Date();
        }
    }
    /**
     * Getter de l'attribut duree
     * @return la durée de la tâche
     */
    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    /**
     * Getter de l'attibut
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

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public Tache getParent() {
        return parent;
    }

    public void setParent(Tache parent) {
        this.parent = parent;
    }

    public Tache getAntecedant() {
        return antecedant;
    }

    public void setAntecedant(Tache antecedant) {
        this.antecedant = antecedant;
    }

    public void setEtat(String archive) {
        this.etat = archive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tache tache = (Tache) o;
        return duree == tache.duree && importance == tache.importance && Objects.equals(nom, tache.nom) && Objects.equals(etat, tache.etat) && Objects.equals(dateDebut, tache.dateDebut) && Objects.equals(description, tache.description) && Objects.equals(parent, tache.parent) && Objects.equals(antecedant, tache.antecedant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, etat, dateDebut, duree, description, importance, parent, antecedant);
    }

    @Override
    public String toString() {
        return "Tache{" + nom + ' '  + duree + '}';
    }

}
