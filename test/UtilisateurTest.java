import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilisateurTest {
    @Test
    public void testSave() throws SQLException {
        Utilisateur luc = new Utilisateur("dechez", "luc", "LUC", "1234");
        Utilisateur resultat = null;
        try{
            luc.save();
        }catch (SQLException e){
            System.out.println("Erreur lors de l'insertion : "+e);
        }
        try{
            resultat = Utilisateur.findByNomPrenom("dechez", "luc");
        }catch (SQLException e){
            System.out.println("Erreur lors de la récupération : "+e);
        }

        assertEquals(luc.getId(), resultat.getId(), "Les deux utilisateurs devraient être identique");
        luc.delete();
    }

    @Test
    public void testDelete() throws SQLException {
        Utilisateur luc = new Utilisateur("dechez", "luc", "LUC", "1234");
        Utilisateur resultat = null;
        try{
            luc.save();
        }catch (SQLException e){
            System.out.println("Erreur lors de l'insertion : "+e);
        }
        try{
            luc.delete();
        }catch (SQLException e){
            System.out.println("Erreur lors de la suppression : "+e);
        }
        try{
            resultat = Utilisateur.findByNomPrenom("dechez", "luc");
        }catch (SQLException e){
            System.out.println("Erreur lors de la récupération : "+e);
        }

        assertEquals(null, resultat, "L'utilisateur devrait être supprimé");
    }

    @Test
    public void testFindById() throws SQLException {
        Utilisateur test = new Utilisateur("TEST", "findId", "TEST", "8685");
        Utilisateur resultat = null;
        try{
            test.save();
        }catch (SQLException e){
            System.out.println("Erreur lors de l'insertion : "+e);
        }
        try{
            resultat = Utilisateur.findById(test.getId());
        }catch (SQLException e){
            System.out.println("Erreur lors de la récupération : "+e);
        }

        assertEquals(test.getId(), resultat.getId(), "Les deux utilisateurs devraient être identique");
        test.delete();
    }

    @Test
    public void testFindAll() throws SQLException {
        Utilisateur test = new Utilisateur("TEST", "findAll", "TEST", "8685");
        Utilisateur resultat = null;
        try{
            test.save();
        }catch (SQLException e){
            System.out.println("Erreur lors de l'insertion : "+e);
        }
        try{
            resultat = Utilisateur.findAll().get(Utilisateur.findAll().size()-1);
        }catch (SQLException e){
            System.out.println("Erreur lors de la récupération : "+e);
        }

        assertEquals(test.getId(), resultat.getId(), "Les deux utilisateurs devraient être identique");
        test.delete();
    }
}
