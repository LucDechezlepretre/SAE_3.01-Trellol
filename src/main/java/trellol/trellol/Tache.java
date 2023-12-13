package trellol.trellol;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tache {
    // Format de la date souhaité
    public static String pattern = "dd/MM/yyyy";

    // Création d'un objet SimpleDateFormat avec le format spécifié
    public static SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
    private int id;
    private String nom;

    private Date dateDebut;
    private int duree;
    private String description;
    private int importance;
    private Tache enfant;
    private Tache antecedant;
    public Tache(String nom, String date, int duree, String description, int importance){
        this.id = -1;
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

    public int getId() {
        return id;
    }

}
