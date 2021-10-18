package testSuite;


import Backend.Game;
import Backend.Inventory;
import Backend.StandardUser;
import Backend.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    public Inventory<Game> I0, I1;
    public Inventory<User> I3, I4;
    public Game g0, g1, g2;
    public StandardUser u0, u1, u2;


    @BeforeEach
    public void setUp() {
        I0 = new Inventory<Game>();
        I1 = new Inventory<Game>();

        I3 = new Inventory<User>();
        I4 = new Inventory<User>();

        g0 = new Game("COD", 20.0, Game.FS, "Faraz");
        g1 = new Game("NBA", 20.0, Game.FS, "Amjad");
        g2 = new Game("FIFA", 20.0, Game.FS, "Emad");

        u0 = new StandardUser("Faraz", 200.0);
        u1 = new StandardUser("Majed", 100.0);
        u2 = new StandardUser("Amjad", 1000.0);
    }

    @Test
    public void testGameInv() {
        I0.addItem(g0);
        I0.addItem(g1);
        I0.addItem(g2);

        ArrayList<Game> x = new ArrayList<Game>();
        x.add(g0);
        x.add(g1);
        x.add(g2);

        assertEquals(I0.getItems(), x);
    }

    @Test
    public void testUserInv() {
        I3.addItem(u0);
        I3.addItem(u1);
        I3.addItem(u2);

        ArrayList<User> x = new ArrayList<User>();
        x.add(u0);
        x.add(u1);
        x.add(u2);

        assertEquals(I3.getItems(), x);
    }

    @Test
    public void testRemoveGame() {
        I0.addItem(g0);
        I0.addItem(g1);
        I0.addItem(g2);
        I0.removeItem(g2);

        ArrayList<Game> x = new ArrayList<Game>();
        x.add(g0);
        x.add(g1);
        x.add(g2);

        assertNotEquals(I0.getItems(), x);

        x.remove(g2);

        assertEquals(I0.getItems(), x);
    }

    @Test
    public void testRemoveUser() {
        I3.addItem(u0);
        I3.addItem(u1);
        I3.addItem(u2);
        I3.removeItem(u2);

        ArrayList<User> x = new ArrayList<User>();
        x.add(u0);
        x.add(u1);
        x.add(u2);

        assertNotEquals(I3.getItems(), x);

        x.remove(u2);

        assertEquals(I3.getItems(), x);
    }

    @Test
    public void testContains() {
        I0.addItem(g0);
        I0.addItem(g1);

        assertFalse(I0.containsObj(g2));
        assertTrue(I0.containsObj(g0));
    }

    @Test
    public void testGetObj() {
        I3.addItem(u0);
        I3.addItem(u1);
        I3.addItem(u2);

        assertEquals(I3.getObj("Faraz"), u0);
        assertNull(I3.getObj("GangBoy"));
    }


}
