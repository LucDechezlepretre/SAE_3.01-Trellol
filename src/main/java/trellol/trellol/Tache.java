package trellol.trellol;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Tache {
    // Format de la date souhaité
    public final static String pattern = "dd/MM/yyyy";

    // Création d'un objet SimpleDateFormat avec le format spécifié
    public static SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
    private String nom;
    public static final String ETAT_ARCHIVE = "Archive";
    public static final String ETAT_NON_ARCHIVE = "NonArchive";
    public static final String ETAT_SUPPRIME = "Supprime";
    private Date dateDebut;
    private int duree;
    private String etat;
    private String description;
    private int importance;
    private Tache parent;
    private Tache antecedant;
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
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

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

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getEtat() {
    	return this.etat;
    }

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
        return "Tache{" + nom + '\''  + duree + '}';
    }
}
