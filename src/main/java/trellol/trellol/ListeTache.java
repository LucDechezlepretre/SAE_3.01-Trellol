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
        //Recupere personne d'id passé en paramètre
        String SQLPrep = "SELECT nom FROM ListeTache WHERE id=?;";
        PreparedStatement statement = connect.prepareStatement(SQLPrep);
        statement.setInt(1, id);


        ResultSet rs = statement.getResultSet();
        if(rs.next()){ //Il y a une ListeTache avec cette id dans la bd

            //On recupere les tahces de la listetache
            String n=rs.getString("nom");
            SQLPrep="SELECT idTache FROM Contient WHERE idListeTache=?";
            statement= connect.prepareStatement(SQLPrep);
            statement.setInt(1, id);

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
}
