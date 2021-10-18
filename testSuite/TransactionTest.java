package testSuite;

import Backend.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    public static Transaction t;
    public static Data data;
    public static Admin a0;
    public static Buyer b0, b1;
    public static Seller s0;
    public static StandardUser st0;


    @BeforeEach
    public void setUp() {
        try {
            t = new Transaction();
            data = new Data();
            t.data = data;
            data.userLoginsPath = "testSuite/UserLoginsTest.txt";
            data.databasePath = "testSuite/DatabaseTest.txt";
            data.dailyPath = "testSuite/dailyTest.txt";
            data.userLoginsRead = new File(data.userLoginsPath);
            data.databaseRead = new File(data.databasePath);
            data.dailyRead = new File(data.dailyPath);
            data.scanner1 = new Scanner(data.userLoginsRead);
            data.scanner2 = new Scanner(data.databaseRead);
            data.scanner3 = new Scanner(data.dailyRead);
            data.databaseWrite = new FileWriter(data.databaseRead);
            new PrintWriter(data.userLoginsPath).close();//delete file contents
            new PrintWriter(data.databasePath).close();//delete file contents
            new PrintWriter(data.dailyPath).close();//delete file contents

            a0 = new Admin("majed", 1000.0);
            a0.userlist = t.userList;

            b0 = new Buyer("amjad", 0000.0);
            b1 = new Buyer("omar", 1200.0);

            s0 = new Seller("emad", 100.0);
            s0.sell("game1", 20.00, 20.00);

            st0 = new StandardUser("bob", 100.0);


            t.userList.addItem(a0);
            t.userList.addItem(b0);
            t.userList.addItem(s0);
            t.userList.addItem(b1);

        } catch (IOException e) {
            System.out.println("File does not exist, exiting");
            System.exit(0);
        }

    }

    @Test
    public void testLoginVerification() {

        t.currentUser = null;

        t.loginVerification("amjad", "BS", "1200.0");

        assertEquals(t.currentUser.getName(), "amjad");

        t.currentUser = null;
        t.loginVerification("amjad", "fdnsfndfds", "301232123");

        assertEquals(t.currentUser.getName(), "amjad");

        t.currentUser = null;
        t.loginVerification("ds", "fdnsfndfds", "301232123");

        assertNull(t.currentUser);
    }

    @Test
    public void testLogout() {
        t.currentUser = null;

        t.logout("amjad", "BS", "1200.0");

        assertNull(t.currentUser);

        t.currentUser = t.userList.getObj("amjad");

        t.logout("amjad", "fdnsfndfds", "301232123");

        assertNull(t.currentUser);

        t.currentUser = t.userList.getObj("amjad");

        t.logout("majed", "fdnsfndfds", "301232123");

        assertEquals(t.currentUser.getName(), "amjad");

    }

    @Test
    public void testCheckUserNull() {

        t.currentUser = null;

        assertTrue(t.checkUserNull());

        t.currentUser = t.userList.getObj("amjad");

        assertFalse(t.checkUserNull());
    }

    @Test
    public void testSetAuctionOn() {

        t.setAuctionOn(true);

        assertTrue(t.auctionOn);

        t.setAuctionOn(false);

        assertFalse(t.auctionOn);
    }

    @Test
    public void testVerifyBuy() {
        t.currentUser = null;
        assertFalse(t.verifyBuy("emad", "game1", "amjad"));

        t.currentUser = t.userList.getObj("majed");
        assertFalse(t.verifyBuy("emad", "game1", "amjad"));

        t.currentUser = t.userList.getObj("amjad");
        assertFalse(t.verifyBuy("amjad", "game1", "amjad"));


        assertFalse(t.verifyBuy("faraz", "game1", "amjad"));


        assertFalse(t.verifyBuy("omar", "game1", "amjad"));


        assertFalse(t.verifyBuy("emad", "game", "amjad"));


        assertFalse(t.verifyBuy("emad", "game", "amjad"));

        s0.getInv().getObj("game1").setSelling(false);
        assertFalse(t.verifyBuy("emad", "game1", "amjad"));

        Game game = s0.getInv().getObj("game1");
        s0.getInv().removeItem(game);

        Game game1 = new Game("game1", 0, Game.FS, s0.getName());
        s0.getInv().addItem(game1);
        game1.setSelling(true);
        assertTrue(t.verifyBuy("emad", "game1", "amjad"));
    }

    @Test
    public void testVerifySell() {
        t.currentUser = null;
        assertFalse(t.verifySell("emad", "game1", "20.00", "20.00"));

        t.currentUser = t.userList.getObj("amjad");
        assertFalse(t.verifySell("amjad", "game1", "20.00", "20.00"));

        t.currentUser = t.userList.getObj("emad");
        assertFalse(t.verifySell("emad", "game1", "20.00", "90.01"));

        assertFalse(t.verifySell("emad", "game1", "20.00", "90.00"));

        assertFalse(t.verifySell("emad", "                                                  " +
                "game1", "20.00", "90.01"));

        assertFalse(t.verifySell("emad", "game", "99999.00", "90.00"));

        assertTrue(t.verifySell("emad", "game", "9.00", "90.00"));

    }

    @Test
    public void testDeleteUser() {
        t.currentUser = null;
        t.deleteUsers("amjad");
        assertNotNull(t.userList.getObj("amjad"));


        t.currentUser = t.userList.getObj("amjad");
        t.deleteUsers("amjad");
        assertNotNull(t.userList.getObj("amjad"));


        t.currentUser = t.userList.getObj("majed");
        t.deleteUsers("majed");
        assertNotNull(t.userList.getObj("majed"));


        t.currentUser = t.userList.getObj("majed");
        t.deleteUsers("bob");
        assertNull(t.userList.getObj("bob"));

        t.currentUser = t.userList.getObj("majed");
        assertNotNull(t.userList.getObj("amjad"));
        t.deleteUsers("amjad");
        assertNull(t.userList.getObj("amjad"));


    }

    @Test
    public void testAddCredit() {
        t.currentUser = null;
        t.addCredit("amjad", 20.00);
        assertEquals(t.userList.getObj("amjad").getCredit(), 0);

        t.currentUser = st0;
        t.addCredit("bob", 100);
        assertEquals(st0.getCredit(), 100);

        t.currentUser = t.userList.getObj("amjad");
        t.addCredit("amjad", 100);
        assertEquals(t.userList.getObj("amjad").getCredit(), 100);

        t.currentUser = t.userList.getObj("majed");
        t.addCredit("amjad", 100);
        assertEquals(t.userList.getObj("amjad").getCredit(), 200);
    }

    @Test
    public void testAuctionSale() {
        assertEquals(t.userList.getObj("emad").getInv().getObj("game1").getPrice(), 20);
        t.auctionSaleInitial();
        assertEquals(t.userList.getObj("emad").getInv().getObj("game1").getPrice(), 16);
    }

    @Test
    public void testVerifyRefund() {
        t.currentUser = null;
        assertFalse(t.verifyRefund("amjad", "emad", 20.00));

        t.currentUser = s0;
        assertFalse(t.verifyRefund("amjad", "emad", 20.00));

        t.currentUser = a0;
        assertFalse(t.verifyRefund("amjad1", "emad1", 20.00));


        assertFalse(t.verifyRefund("emad", "amjad", 20.00));


        assertFalse(t.verifyRefund("amjad", "emad", 20000.00));

        b0.setCredit(999999.99);
        assertTrue(t.verifyRefund("amjad", "emad", 20.00));
    }

    @Test
    public void testRemoveGame() {
        assertFalse(t.verifyRemove("bob", "game1"));

        assertFalse(t.verifyRemove("emad", "game"));


        assertFalse(t.verifyRemove("emad", "game1"));

        s0.getInv().getObj("game1").setAddedToday(false);
        assertTrue(t.verifyRemove("emad", "game1"));
    }

    @Test
    public void testVerifyGift() {
        t.currentUser = null;
        assertFalse(t.verifyGift("gamg", "amjad", "emad"));

        t.currentUser = st0;
        assertFalse(t.verifyGift("gamg", st0.getName(), "emad"));

        t.currentUser = s0;
        assertFalse(t.verifyGift("gamg", "emad", "emad11"));

        assertFalse(t.verifyGift("gamg", "emad", "amjad"));

        b0.getInv().addItem(s0.getInv().getObj("game1"));
        assertFalse(t.verifyGift("game1", "emad", "amjad"));

        b0.getInv().removeItem(s0.getInv().getObj("game1"));
        assertFalse(t.verifyGift("game1", "emad", "amjad"));

        s0.getInv().getObj("game1").setAddedToday(false);
        t.currentUser = a0;
        assertTrue(t.verifyGift("game1", "emad", "amjad"));

        t.currentUser = s0;
        assertTrue(t.verifyGift("game1", "emad", "amjad"));
    }

    @Test
    public void createUser() {
        t.currentUser = null;
        t.createUser("amjad1", Data.STANDARD, 20.00, null, true, false);
        assertNull(t.userList.getObj("amjad1"));

        t.currentUser = a0;
        t.createUser("amjad1", Data.STANDARD, 20.00, null, true, false);
        assertNotNull(t.userList.getObj("amjad1"));
        assertEquals(t.userList.getObj("amjad1").getType(), Data.STANDARD);
        t.userList.removeItem(t.userList.getObj("amjad1"));

        t.currentUser = a0;
        t.createUser("amjad1", Data.ADMIN, 20.00, null, true, false);
        assertNotNull(t.userList.getObj("amjad1"));
        assertEquals(t.userList.getObj("amjad1").getType(), Data.ADMIN);
        t.userList.removeItem(t.userList.getObj("amjad1"));

        t.currentUser = a0;
        t.createUser("amjad1", Data.BUYER, 20.00, null, true, false);
        assertNotNull(t.userList.getObj("amjad1"));
        assertEquals(t.userList.getObj("amjad1").getType(), Data.BUYER);
        t.userList.removeItem(t.userList.getObj("amjad1"));

        t.currentUser = a0;
        t.createUser("amjad1", Data.SELLER, 20.00, null, true, false);
        assertNotNull(t.userList.getObj("amjad1"));
        assertEquals(t.userList.getObj("amjad1").getType(), Data.SELLER);
        t.userList.removeItem(t.userList.getObj("amjad1"));


    }
}
