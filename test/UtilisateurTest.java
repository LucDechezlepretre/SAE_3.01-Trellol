import org.junit.jupiter.api.Test;

import trellol.trellol.Utilisateur;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilisateurTest {
    @Test
    public void testFindById(){
        Utilisateur luc = new Utilisateur("dechez", "luc", "LUC", "1234");
        Utilisateur resultat = null;
        try{
            luc.save();
        }catch (SQLException e){
            System.out.println("Erreur lors de l'insertion : "+e);
        }
        try{
            resultat = Utilisateur.findById(3);
        }catch (SQLException e){
            System.out.println("Erreur lors de la récupération : "+e);
        }

        assertEquals(luc, resultat, "Les deux utilisateurs devraient être identique");
    }
}
