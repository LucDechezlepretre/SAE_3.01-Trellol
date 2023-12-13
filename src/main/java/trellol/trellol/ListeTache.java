package trellol.trellol;

import BD.DBConnection;

import java.sql.*;
import java.util.ArrayList;

/**
 * Class representant une ListeTache c'est à dire une colonne en affichage colonne ou une ligne en affichage liste
 */
public class ListeTache {

    /**
     * champ nom de la table ListeTache
     */
    private String nom;

    /**
     * id du film
     */
    private int id;

    private ArrayList<Tache> taches;

    public ListeTache(String n){
        this.nom=n;
        this.id=-1;

        this.taches=new ArrayList<>();
    }


    /**
     * Constructeur privé de ListeTache permettant de créer un objet déjà existant dans la bd
     * @param id, id de la ListeTache
     * @param n, nom de la ListeTache
     */
    private ListeTache(int id, String n, ArrayList<Tache> t){
        this.nom=n;
        this.id=id;
        this.taches=t;
    }

    /**
     * Methode static retournant l'objet ListeTache de l'id en param. Retourne null s'il  n'y en a pas
     * @param id, id de la ListeTache cherchée
     * @return la ListeTache d'id en param si elle existe, sinon retourne null
     */
    public static ListeTache findById(int id) throws SQLException {
        Connection connect = DBConnection.getConnection();
        String SQLPrep = "SELECT nom FROM ListeTache WHERE id=?;";
        PreparedStatement statement = connect.prepareStatement(SQLPrep);
        statement.setInt(1, id);

        statement.execute();
        ResultSet rs = statement.getResultSet();
        if(rs.next()){ //Il y a une ListeTache avec cette id dans la bd

            //On recupere les tahces de la listetache
            String n=rs.getString("nom");
            SQLPrep="SELECT idTache FROM Contient WHERE idListeTache=?";
            statement= connect.prepareStatement(SQLPrep);
            statement.setInt(1, id);
            statement.execute();

            rs = statement.getResultSet();

            ArrayList<Tache> taches=new ArrayList<>();
            while(rs.next()){
                taches.add(Tache.findById(rs.getInt("idTache")));
            }

            return new ListeTache(id, n, taches);
        }
        else{
            return null;
        }
    }

    /**
     * Getter de l'attribut nom
     * @return l'attribut nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Getter de l'id
     * @return l'id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter des taches contenues dans la ListeTache
     * @return une ArrayList des taches de ListeTache
     */
    public ArrayList<Tache> getTaches() {
        return taches;
    }


    /**
     * Methode d'insertion de l'objet ListeTache courant dans la table
     * @throws SQLException
     */
    public void save() throws SQLException {
        if(this.id==-1){ //On verifie que c'est bien une ListeTache pas deja dans la bd
            Connection connect = DBConnection.getConnection();
            String SQLPrep = "INSERT INTO ListeTache(nom) VALUES(?);";
            PreparedStatement statement = connect.prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, this.nom);

            statement.executeUpdate();

            int autoInc = -1;
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                autoInc = rs.getInt("id");
            }

            this.id=autoInc;
        }
    }

    /**
     * Methode d'ajout d'une tache à la liste, modifie l'attribut taches et la table Contient de la bd
     * @param t la tache à ajouter
     * @throws SQLException
     */
    public void ajouterTache(Tache t) throws SQLException {
        this.taches.add(t);

        Connection connect=DBConnection.getConnection();
        String SQLPrep = "INSERT INTO Contient(idTache, idListeTache) VALUES(?,?);";
        PreparedStatement statement = connect.prepareStatement(SQLPrep);

        statement.setInt(1, t.getId());
        statement.setInt(2, this.id);
        statement.executeUpdate();
    }

    /**
     * Methode de retrait d'une tache de la ListeTache
     * @param t la tache à retirer
     * @throws SQLException
     */
    public void retirerTache(Tache t) throws SQLException {
        this.taches.remove(t);

        Connection connect=DBConnection.getConnection();
        String SQLPrep = "DELETE FROM Contient WHERE idTache=? AND idListeTache=?";
        PreparedStatement statement = connect.prepareStatement(SQLPrep);

        statement.setInt(1, t.getId());
        statement.setInt(2, this.id);
        statement.executeUpdate();
    }
}
