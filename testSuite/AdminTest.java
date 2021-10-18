package testSuite;
import Backend.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {
    public Admin a0;
    public Buyer b0, b1;
    public Seller s0, s1;
    public Game g0, g1, g2;
    public Inventory<User> userList;

    @BeforeEach
    public void setUp() {
        userList = new Inventory<>();
        a0 = new Admin("majed", 1000.0);
        a0.userlist = userList;

        b0 = new Buyer("amjad", 1200.0);
        b1 = new Buyer("anas", 999999.99);

        s0 = new Seller("emad", 100.0);
        s1 = new Seller("syed", 100);

        userList.addItem(a0);
        userList.addItem(b0);
        userList.addItem(s0);

        g0 = new Game("COD", 10.0, Game.FS, b0.getName());
        b0.getInv().addItem(g0);
        g1 = new Game("FIFA", 20.0, Game.FS, s0.getName());
        s0.getInv().addItem(g1);
        g2 = new Game("RL", 30.0, Game.FS, a0.getName());
        a0.getInv().addItem(g2);
    }

    @Test
    public void testUpdateUserCredit(){
        a0.updateCredit(30.0, a0);
        a0.updateCredit(12.0, b0);
        a0.updateCredit(20, s0);

        assertEquals(1030, a0.getCredit());
        assertEquals(1212, b0.getCredit());
        assertEquals(120, s0.getCredit());
    }

    @Test
    public void testRefund(){
        a0.refund(s1,b1,100.0);
        assertEquals(999999.99, b1.getCredit()); // max out credit

        a0.refund(s0,b0, 20.0);
        assertEquals(1220, b0.getCredit());
    }

    @Test
    public void testUserExist(){
        assertTrue(a0.userExists("majed"));
        assertTrue(a0.userExists("amjad"));
        assertTrue(a0.userExists("emad"));
        assertFalse(a0.userExists("syed"));
        assertFalse(a0.userExists("ahmad"));
    }

    @Test
    public void testDeleteUser(){
        a0.deleteUser("majed");
        assertFalse(a0.userExists("majed"));

        a0.deleteUser("amjad");
        assertFalse(a0.userExists("amjad"));

        a0.deleteUser("emad");
        assertFalse(a0.userExists("emad"));
    }

    @Test
    public void testSetAuctionSalePrices(){
        g0.setSelling(true);
        g0.setDiscount(50);
        a0.setAuctionPrices(true);
        assertEquals(5, g0.getPrice());

        g1.setSelling(false);
        g0.setDiscount(50);
        a0.setAuctionPrices(true);
        assertEquals(20, g1.getPrice());

        g2.setSelling(true);
        g0.setDiscount(50);
        a0.setAuctionPrices(false);
        assertEquals(30, g2.getPrice());
    }
}
