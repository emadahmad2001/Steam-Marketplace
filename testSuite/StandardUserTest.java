package testSuite;

import Backend.Game;
import Backend.StandardUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StandardUserTest {
    public StandardUser b0, b1, b2, u0, u1;
    public Game g0, g1;

    @BeforeEach
    public void setUp(){
        b0 = new StandardUser("Faraz", 1000.0);
        b1 = new StandardUser("majed", 1000.0);
        b2 = new StandardUser("Emad", 100.0);

        u0 = new StandardUser("amjad", 200.0);
        u1 = new StandardUser("amjad", 200.0);


        g0 = new Game("COD", 20.0, Game.FS, b2.getName());
        g1 = new Game("FIFA", 20.0, Game.FS, b2.getName());
    }

    @Test
    public void testBuySell(){
        b2.getInv().addItem(g0);
        b2.getInv().getObj("COD").setSelling(true);

        assertNull(b0.getInv().getObj("COD")); //check buyer's inventory before buying
        assertEquals(g0, b2.getInv().getObj("COD")); //check seller's inventory before selling

        assertEquals(1000.0, b0.getCredit()); // check buyer's credit
        assertEquals(100.0, b2.getCredit()); // check seller's credit

        b0.buy(g0); //let buyer buy the game
        assertEquals(g0, b0.getInv().getObj(g0.getName())); //check buyer's inventory after buying
        assertEquals(g0, b2.getInv().getObj(g0.getName())); //check seller's inventory after selling

        assertEquals(980.0, b0.getCredit()); //check buyer's credit after buying
    }

    @Test
    public void checkUserSelling(){
        b1.getInv().addItem(g1); //add game to user's inventory
        b1.sell(g1.getName(), g1.getPrice(), 0); // call function sell for the user

        assertEquals(1000.0, b1.getCredit()); // check user's credit after selling if changed
        assertEquals(g1, b1.getInv().getObj(g1.getName())); // check if game is still in user's inventory
    }

    @Test
    public void checkUserBuying(){
        u1.getInv().addItem(g0);

        assertNull(b0.getInv().getObj("COD")); //check buyer's inventory before buying
        assertEquals(g0, u1.getInv().getObj("COD")); //check seller's inventory before selling

        u1.sell("COD", 20.0, 0);
        assertEquals(g0, u1.getInv().getObj("COD")); //check seller's inventory after selling
        assertEquals("FS", g0.getStatus()); // check if status is unchanged

        assertEquals(1000.0, b0.getCredit()); //check buyer's credit after calling sell
        assertEquals(200.0, u1.getCredit()); //check seller's credit after calling sell
    }

    @Test
    public void checkObjEquals(){
        assertNotEquals(b1, b0); // check if object b0 is the same as b1
        assertNotEquals(b2, b0); // check if object b0 is the same as b2
        assertNotEquals(b2, b1); // check if object b1 is the same as b2
        assertEquals(u0, u1); // check if object b2 is the same as b3
    }
}
