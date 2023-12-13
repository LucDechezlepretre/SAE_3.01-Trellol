package BD;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionTest {
    @Test
    void getConnection() throws SQLException {
        DBConnection.setNomDB("Trellol");
        Connection c1 = DBConnection.getConnection();
        Connection c2 = DBConnection.getConnection();
        System.out.println(c1);

        assertEquals(c1, c2, "Les deux connexions doivent être les mêmes");
        assertNotEquals(null, c1, "ne doit pas etre null");
    }
}