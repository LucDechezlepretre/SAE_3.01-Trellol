package trellol.trellol;

import BD.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String login;
    private String mdp;

    public Utilisateur(String nom, String prenom, String login, String mdp) {
        this.id = -1;
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.mdp = mdp;
    }
    public void save() throws SQLException{
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
        String SQLprep = "update User set nom=?, prenom=?, login=? where id=?;";
        PreparedStatement prep = connection.prepareStatement(SQLprep);
        prep.setString(1, this.nom);
        prep.setString(2, this.prenom);
        prep.setString(3, this.login);
        prep.setInt(4, this.id);
        prep.execute();
    }

    private void saveNew() throws SQLException{
        DBConnection.setNomDB("Trellol");
        Connection connection = DBConnection.getConnection();
        String SQLPrep = "INSERT INTO User(nom, prenom, login, mdp) VALUES (?,?, ?, ?);";
        PreparedStatement prep;
        // l'option RETURN_GENERATED_KEYS permet de recuperer l'id (car
        // auto-increment)
        prep = connection.prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
        prep.setString(1, this.nom);
        prep.setString(2, this.prenom);
        prep.setString(3, this.login);
        prep.setString(4, this.mdp);
        prep.executeUpdate();
        ResultSet res = prep.getGeneratedKeys();
        if(res.next()) {
            this.id = (res.getInt(1));
        }
    }

    public void delete() throws SQLException{
        DBConnection.setNomDB("Trellol");
        Connection connection = DBConnection.getConnection();
        String sql = "DELETE FROM User WHERE id = " + this.id;
        PreparedStatement prep;
        // l'option RETURN_GENERATED_KEYS permet de recuperer l'id (car
        // auto-increment)
        prep = connection.prepareStatement(sql);
        prep.executeUpdate();
        this.id = -1;
    }

    public static ArrayList<Utilisateur> findAll() throws SQLException {
        DBConnection.setNomDB("Trellol");
        Connection connection = DBConnection.getConnection();
        ArrayList<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
        PreparedStatement preparedStatement = connection.prepareStatement("select id, nom, prenom, login, mdp from User");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            Utilisateur user = new Utilisateur(resultSet.getString("nom"), resultSet.getString("prenom"), resultSet.getString("login"), resultSet.getString("mdp"));
            user.id = Integer.valueOf(resultSet.getString("id"));
            utilisateurs.add(user);
        }
        return utilisateurs;
    }

    public static Utilisateur findById(int id) throws SQLException{
        DBConnection.setNomDB("Trellol");
        Connection connection = DBConnection.getConnection();
        PreparedStatement prepareStatement = connection.prepareStatement("select nom, prenom, login, mdp from User where id = ?");
        prepareStatement.setString(1, id+"");
        ResultSet res = prepareStatement.executeQuery();
        Utilisateur user = null;
        if(res.next()){
            user = new Utilisateur(res.getString("nom"), res.getString("prenom"), res.getString("login"), res.getString("mdp"));
            user.id = id;
        }
        return user;
    }

    public static Utilisateur findByNomPrenom(String nom, String prenom) throws SQLException{
        DBConnection.setNomDB("Trellol");
        Connection connection = DBConnection.getConnection();
        PreparedStatement prepareStatement = connection.prepareStatement("select id, nom, prenom, login, mdp from User where nom = ? and prenom = ?");
        prepareStatement.setString(1, nom);
        prepareStatement.setString(2, prenom);
        ResultSet res = prepareStatement.executeQuery();
        Utilisateur user = null;
        if(res.next()){
            user = new Utilisateur(res.getString("nom"), res.getString("prenom"), res.getString("login"), res.getString("mdp"));
            user.id = Integer.valueOf(res.getString("id"));
        }
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return id == that.id && Objects.equals(nom, that.nom) && Objects.equals(prenom, that.prenom) && Objects.equals(login, that.login) && Objects.equals(mdp, that.mdp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prenom, login, mdp);
    }

    public int getId() {
        return id;
    }
}
