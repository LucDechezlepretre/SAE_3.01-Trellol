import org.junit.jupiter.api.Test;
import trellol.trellol.Tache;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TacheTest {
    @Test
    public void testSave() throws SQLException {
        Tache test = new Tache("dechez", "13/12/2023", 1, "Test", 1);
        Tache resultat = null;
        try{
            test.save();
        }catch (SQLException e){
            System.out.println("Erreur lors de l'insertion : "+e);
        }
        try{
            resultat = Tache.findById(test.getId());
        }catch (SQLException e){
            System.out.println("Erreur lors de la récupération : "+e);
        }

        assertEquals(test.getId(), resultat.getId(), "Les deux utilisateurs devraient être identique");
        test.delete();
    }

    @Test
    public void testDelete() throws SQLException {
        Tache test = new Tache("dechez", "13/12/2023", 1, "Test", 1);
        Tache resultat = null;
        try{
            test.save();
        }catch (SQLException e){
            System.out.println("Erreur lors de l'insertion : "+e);
        }
        try{
            test.delete();
        }catch (SQLException e){
            System.out.println("Erreur lors de la suppression : "+e);
        }
        try{
            resultat = Tache.findById(test.getId());
        }catch (SQLException e){
            System.out.println("Erreur lors de la récupération : "+e);
        }

        assertEquals(null, resultat, "L'utilisateur devrait être supprimé");
    }

    @Test
    public void testFindById() throws SQLException {
        Tache test = new Tache("dechez", "13/12/2023", 1, "Test", 1);
        Tache resultat = null;
        try{
            test.save();
        }catch (SQLException e){
            System.out.println("Erreur lors de l'insertion : "+e);
        }
        try{
            resultat = Tache.findById(test.getId());
        }catch (SQLException e){
            System.out.println("Erreur lors de la récupération : "+e);
        }

        assertEquals(test.getId(), resultat.getId(), "Les deux utilisateurs devraient être identique");
        test.delete();
    }



}