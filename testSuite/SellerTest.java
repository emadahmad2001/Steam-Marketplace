<<<<<<< HEAD:Backend/SellerTest.java
package Backend;

=======
package testSuite;

import Backend.*;
>>>>>>> d33878092b700884ab6eeace16075df532c9344e:testSuite/SellerTest.java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SellerTest {

    public static User u0;
<<<<<<< HEAD:Backend/SellerTest.java
    public static Buyer b0, b1, b2;
    public static Admin a0, a1, a2;
=======
    public static Buyer b0;
    public static Admin a0;
>>>>>>> d33878092b700884ab6eeace16075df532c9344e:testSuite/SellerTest.java
    public static Seller s0, s1, s2;
    public static Game g0, g1;

    @BeforeEach
    public void setUp() {

        u0 = new User("Syed", 1000.0);

        b0 = new Buyer("Majed", 1000.0);

        a0 = new Admin("Amjad", 1000.0);


        s0 = new Seller("Emad", 2000.0);
<<<<<<< HEAD:Backend/SellerTest.java
=======
        s1 = new Seller("Emad", 1000.0);
        s2 = new Seller("amjad", 200.0);
>>>>>>> d33878092b700884ab6eeace16075df532c9344e:testSuite/SellerTest.java

        g0 = new Game("COD", 20.0, Game.FS, s0.getName());
        g1 = new Game("Fifa", 30.0, Game.NFS, s0.getName());

    }

    @Test
    public void testBuy(){

        // Sellers inventory should not update when buying
        b0.getInv().addItem(g0);

        assertNull(s0.getInv().getObj("COD")); //check buyer's inventory before buying
        assertEquals(g0, b0.getInv().getObj("COD")); //check seller's inventory before selling

        s0.buy(g0);
        assertNull(s0.getInv().getObj("COD")); //check seller's inventory after selling

        assertEquals(1000.0, b0.getCredit()); //check buyer's credit after calling sell
        assertEquals(2000.0, s0.getCredit()); //check seller's credit after calling sell
    }

    @Test
    public void checkSellerBuying(){
        s0.getInv().addItem(g0);

        assertNull(b0.getInv().getObj("COD")); //check buyer's inventory before buying
        assertEquals(g0, s0.getInv().getObj("COD")); //check seller's inventory before selling

        s0.sell("COD", 20.0, 0);
        assertEquals(g0, s0.getInv().getObj("COD")); //check seller's inventory after selling
        assertEquals("FS", g0.getStatus()); // check if status is unchanged

        assertEquals(1000.0, b0.getCredit()); //check buyer's credit after calling sell
        assertEquals(2000.0, s0.getCredit()); //check seller's credit after calling sell

    }

<<<<<<< HEAD:Backend/SellerTest.java
=======
    @Test
    public void checkObjEquals(){
        assertEquals(s1, s0); // check if object b0 is the same as b1
        assertNotEquals(s2, s0); // check if object b0 is the same as b2
        assertNotEquals(s2, s1); // check if object b1 is the same as b2
    }
>>>>>>> d33878092b700884ab6eeace16075df532c9344e:testSuite/SellerTest.java
}
