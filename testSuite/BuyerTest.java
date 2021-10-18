package testSuite;
import Backend.Buyer;
import Backend.Game;
import Backend.Seller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BuyerTest {
    public Buyer b0, b1, b2;
    public Seller s0;
    public Game g0;

    @BeforeEach
    public  void setUp(){
        b0 = new Buyer("majed", 1000.0);
        b1 = new Buyer("majed", 1000.0);
        b2 = new Buyer("amjad", 200.0);

        s0 = new Seller("Emad", 100.0);

        g0 = new Game("COD", 20.0, Game.FS, s0.getName());
    }


    @Test
    public void testBuy(){
        s0.getInv().addItem(g0);
        s0.getInv().getObj("COD").setSelling(true);

        assertNull(b0.getInv().getObj("COD")); //check buyer's inventory before buying
        assertEquals(g0, s0.getInv().getObj("COD")); //check seller's inventory before selling

        assertEquals(1000.0, b0.getCredit()); // check buyer's credit
        assertEquals(100.0, s0.getCredit()); // check seller's credit

        b0.buy(g0); //let buyer buy the game
        assertEquals(g0, b0.getInv().getObj(g0.getName())); //check buyer's inventory after buying
        assertEquals(g0, s0.getInv().getObj(g0.getName())); //check seller's inventory after selling

        assertEquals(980.0, b0.getCredit()); //check buyer's credit after buying
    }


    @Test
    public void checkBuyerSelling(){
        b1.getInv().addItem(g0); //add game to seller's inventory
        b1.sell(g0.getName(), g0.getPrice(), 0); // call function sell for the buyer

        assertEquals(1000.0, b1.getCredit()); // check buyer's credit after selling if changed
        assertEquals(g0, b1.getInv().getObj(g0.getName())); // check if game is still in buyer's inventory
    }

    @Test
    public void checkObjEquals(){
        assertEquals(b1, b0); // check if object b0 is the same as b1
        assertNotEquals(b2, b0); // check if object b0 is the same as b2
        assertNotEquals(b2, b1); // check if object b1 is the same as b2
    }

}
