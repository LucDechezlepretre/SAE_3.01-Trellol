package trellol.trellol;

import BD.DBConnection;

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
    private List<Tache> composants;
    private List<Tache> antecedants;
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
        this.composants = new ArrayList<Tache>();
        this.antecedants = new ArrayList<Tache>();
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
    public void save() throws SQLException {
        if(this.id == -1){
            this.saveNew();
        }
        else{
            this.update();
        }
    }

    private void update() throws SQLException {
        DBConnection.setNomDB("Trellol");
        Connection connection = DBConnection.getConnection();
        String SQLprep = "update Tache set nom=?, dateDebut=?, duree=?, description=?, importance=? where id=?;";
        PreparedStatement prep = connection.prepareStatement(SQLprep);
        prep.setString(1, this.nom);
        prep.setString(2, this.dateDebut.toString());
        prep.setInt(3, this.duree);
        prep.setString(4, this.description);
        prep.setInt(5, this.importance);
        prep.setInt(6, this.id);
        prep.execute();
    }

    private void saveNew() throws SQLException {
        DBConnection.setNomDB("Trellol");
        Connection connection = DBConnection.getConnection();
        String SQLPrep = "INSERT INTO Tache(nom, dateDebut, duree, description, importance) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement prep;
        // l'option RETURN_GENERATED_KEYS permet de recuperer l'id (car
        // auto-increment)
        prep = connection.prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
        prep.setString(1, this.nom);
        prep.setString(2, this.dateDebut.toString());
        prep.setInt(3, this.duree);
        prep.setString(4, this.description);
        prep.setInt(5, this.importance);
        prep.executeUpdate();
        ResultSet res = prep.getGeneratedKeys();
        if (res.next()) {
            this.id = (res.getInt(1));
        }
    }
    public void delete() throws SQLException{
        DBConnection.setNomDB("Trellol");
        Connection connection = DBConnection.getConnection();

        String SQLsuppressionComposant = "DELETE FROM ComposeeDe WHERE ComposeeDe.idTacheComposee = ?";
        PreparedStatement prep;
        prep = connection.prepareStatement(SQLsuppressionComposant);
        prep.setInt(1, this.id);

        String SQLsuppressionAntecedant = "DELETE FROM DependanteDe WHERE DependanteDe.idTacheDependante = ?";
        prep = connection.prepareStatement(SQLsuppressionAntecedant);
        prep.setInt(1, this.id);

        String sql = "DELETE FROM Tache WHERE id = ?";
        // l'option RETURN_GENERATED_KEYS permet de recuperer l'id (car
        // auto-increment)
        prep = connection.prepareStatement(sql);
        prep.setInt(1, this.id);
        prep.executeUpdate();
        this.id = -1;
    }
    public static Tache findById(int id) throws SQLException{
        DBConnection.setNomDB("Trellol");
        Connection connection = DBConnection.getConnection();


    }
}
