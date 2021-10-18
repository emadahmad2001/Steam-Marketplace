package testSuite;

import Backend.Game;
import Backend.StandardUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    public Game g0, g1, g2;
    public StandardUser u0, u1;

    @BeforeEach
    public void setUp(){
        u0 = new StandardUser("Faraz", 1000.0);
        u1 = new StandardUser("Amjad", 900.0);

        g0 = new Game("COD", 20.0, Game.FS, u0.getName());
        g1 = new Game("COD", 20.0, Game.FS, u0.getName());

        g2 = new Game("FIFA", 20.0, Game.FS, u0.getName());
    }

    @Test
    public void testConstructor(){
        assertEquals(g0.getName(), "COD");
        assertEquals(g0.getPrice(), 20.0);
        assertEquals(g0.getStatus(), "FS");
        assertEquals(g0.getOwner(), u0.getName());
    }

    @Test
    public void testEquals(){
        assertEquals(g0, g1);
        assertNotEquals(g1, g2);
        assertNotEquals(g0, g2);
    }

    @Test
    public void testSetters(){
        g1.setName("NBA");
        g1.setPrice(25.0);
        g1.setOwner(u1.getName());
        g1.setDiscount(5.0);

        assertEquals(g1.getName(), "NBA");
        assertEquals(g1.getPrice(), 25.0);
        assertEquals(g1.getOwner(), u1.getName());
        assertEquals(g1.getDiscountPercent(), 5.0);
    }

}



