package testSuite;

import Backend.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataTest {

    public static Data data;
    public static FileWriter dailyWrite;

    @BeforeEach
    public void setUp() {
        try {
            data = new Data();
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
            dailyWrite = new FileWriter(data.dailyRead);
            new PrintWriter(data.userLoginsPath).close();//delete file contents
            new PrintWriter(data.databasePath).close();//delete file contents
            new PrintWriter(data.dailyPath).close();//delete file contents
        } catch (IOException e) {
            System.out.println("File does not exist, exiting");
            System.exit(0);
        }

    }

    @Test
    public void testOpenUserLogins() {
        //here we can test that we are able to write to a file, and read
        assert data.userLoginsWrite == null;
        data.openUserLoginsW();//make sure we are opening the file for writing
        assert data.userLoginsWrite != null;

        data.userLoginsRead = null;
        data.openUserLoginsR();//make sure we are opening the file for reading
        assert data.userLoginsRead != null;

    }

    @Test
    public void testValidateDatabase() {
        //make sure our regex detects good and bad format
        assertTrue(data.validateDatabase("AuctionSale:ON"));
        assertTrue(data.validateDatabase("AuctionSale:OFF"));
        assertFalse(data.validateDatabase("AuctionSale:"));
        assertFalse(data.validateDatabase(""));
    }

    @Test
    public void testGetBasicUserInfo() {
        //test correct and incorrect outputs
        String info = "          amjad,BS,100056.00,                      COD:050.00:FS|";

        String[] infos = data.getBasicUserInfo(info);

        assertEquals(infos[0], "          amjad");
        assertEquals(infos[1], "BS");
        assertEquals(infos[2], "100056.00");


        //valid but not games
        info = "          amjad,BS,100056.00,";

        infos = data.getBasicUserInfo(info);

        assertEquals(infos[0], "          amjad");
        assertEquals(infos[1], "BS");
        assertEquals(infos[2], "100056.00");


        //valid with games and space in name
        info = "         anas A,BS,099980.00,                    game2:024.00:NFS|";
        infos = data.getBasicUserInfo(info);

        assertEquals(infos[0], "         anas A");
        assertEquals(infos[1], "BS");
        assertEquals(infos[2], "099980.00");

        //invalid
        info = "";
        infos = data.getBasicUserInfo(info);

        assertNull(infos);

        //wrong length name
        info = "anas A,BS,099980.00,                    game2:024.00:NFS|";
        infos = data.getBasicUserInfo(info);

        assertNull(infos);


        info = "anas A,BS,099980.00";
        infos = data.getBasicUserInfo(info);

        assertNull(infos);
    }

    @Test
    public void testGetFullUserInfo() {
        String info = "          amjad,BS,100056.00,                      COD:050.00:NFS|";

        String[] infos = data.getFullUserInfo(info);

        assertEquals(infos[0], "          amjad");
        assertEquals(infos[1], "BS");
        assertEquals(infos[2], "100056.00");
        assertEquals(infos[3], "                      COD:050.00:NFS|");

        info = "          amjad,BS,100056.00,";

        infos = data.getFullUserInfo(info);

        assertEquals(infos[0], "          amjad");
        assertEquals(infos[1], "BS");
        assertEquals(infos[2], "100056.00");
        assertEquals(infos.length, 3);


        info = "         anas A,BS,099980.00,                    game2:024.00:NFS|";
        infos = data.getFullUserInfo(info);

        assertEquals(infos[0], "         anas A");
        assertEquals(infos[1], "BS");
        assertEquals(infos[2], "099980.00");
        assertEquals(infos[3], "                    game2:024.00:NFS|");


        info = "";
        infos = data.getFullUserInfo(info);

        assertNull(infos);


        info = "anas A,BS,099980.00,                    game2:024.00:NFS|";
        infos = data.getFullUserInfo(info);

        assertNull(infos);


        info = "anas A,BS,099980.00";
        infos = data.getFullUserInfo(info);

        assertNull(infos);
    }

    @Test
    public void testGetUserGames() {
        String info = "          amjad,BS,100056.00,                      COD:050.00:NFS|";

        ArrayList<String[]> infos = data.getUserGames(info);

        assertEquals(infos.get(0)[0], "                      COD");
        assertEquals(infos.get(0)[1], "050.00");
        assertEquals(infos.get(0)[2], "NFS");


        info = "          amjad,BS,100056.00,                      COD:050.00:NFS|" +
                "                    game2:024.00:NFS|";

        infos = data.getUserGames(info);

        assertEquals(infos.size(), 2);
        assertEquals(infos.get(0)[0], "                      COD");
        assertEquals(infos.get(0)[1], "050.00");
        assertEquals(infos.get(0)[2], "NFS");
        assertEquals(infos.get(1)[0], "                    game2");
        assertEquals(infos.get(1)[1], "024.00");
        assertEquals(infos.get(1)[2], "NFS");


        info = "          amjad,BS,100056.00,";

        infos = data.getUserGames(info);

        assertEquals(infos.size(), 0);
    }

    @Test
    public void testAddLogin() {

        Buyer user = new Buyer("amjad", 100.00);
        data.addLogin(user);
        assertEquals(data.scanner1.nextLine(), "          amjad,BS,000100.00,");


    }

    @Test
    public void testEditUserLogins() {
        String user1 = "          majed,AA,100124.00,                     FIFA:030.00:20.00:FS|                    game2:030.00:20.00:FS|";
        String user2 = "   ellis strank,SS,001000.00,";
        String user3 = "         anas A,BS,099980.00,                    game2:024.00:NFS|";
        ArrayList<String> infos = new ArrayList<>();
        infos.add(user1);
        infos.add(user2);
        infos.add(user3);
        data.editUserLogins(infos);
        assertEquals(data.scanner1.nextLine(), "          majed,AA,100124.00,                     " +
                "FIFA:030.00:20.00:FS|                    game2:030.00:20.00:FS|");
        assertEquals(data.scanner1.nextLine(), "   ellis strank,SS,001000.00,");
        assertEquals(data.scanner1.nextLine(), "         anas A,BS,099980.00,                    " +
                "game2:024.00:NFS|");

    }

    @Test
    public void testGetUserLine() {

        assertEquals(data.getUserLine("majed"), -1);


        String user1 = "          majed,AA,100124.00,                     FIFA:030.00:20.00:FS|                    game2:030.00:20.00:FS|";
        String user2 = "   ellis strank,SS,001000.00,";
        String user3 = "         anas A,BS,099980.00,                    game2:024.00:NFS|";
        ArrayList<String> infos = new ArrayList<>();
        infos.add(user1);
        infos.add(user2);
        infos.add(user3);

        data.editUserLogins(infos);

        assertEquals(data.getUserLine("majed"), 0);
        assertEquals(data.getUserLine("ellis strank"), 1);
        assertEquals(data.getUserLine("anas"), 2);
    }

    @Test
    public void testUserLoginsToArray() {
        String user1 = "          majed,AA,100124.00,                     FIFA:030.00:20.00:FS|                    " +
                "game2:030.00:20.00:FS|";
        String user2 = "   ellis strank,SS,001000.00,";
        String user3 = "         anas A,BS,099980.00,                    game2:024.00:NFS|";
        ArrayList<String> infos = new ArrayList<>();
        infos.add(user1);
        infos.add(user2);
        infos.add(user3);

        data.editUserLogins(infos);
        ArrayList<String> newInfo = data.userLoginsToArray();
        assertEquals(infos, newInfo);
    }

    @Test
    public void testEditCredit() {
        String info = "         anas A,BS,099980.00,";
        String infoUpdated = data.editCredit(info, 10);

        assertEquals(infoUpdated, "         anas A,BS,099990.00,");


        info = "         anas A,BS,099980.00,                    game2:024.00:NFS|";
        infoUpdated = data.editCredit(info, 10);

        assertEquals(infoUpdated, "         anas A,BS,099990.00,                    " +
                "game2:024.00:NFS|");


        info = "         anas A,BS,099980.00,game2:024.00:NFS|";
        infoUpdated = data.editCredit(info, 10);

        assertNull(infoUpdated);

        info = "";
        infoUpdated = data.editCredit(info, 10);

        assertNull(infoUpdated);
    }

    @Test
    public void testUpdateCreditLogins() {
        Buyer user = new Buyer("amjad", 100.00);
        data.addLogin(user);

        data.updateCreditLogins("amjad", 30);
        assertEquals(data.scanner1.nextLine(), "          amjad,BS,000130.00,");

    }

    @Test
    public void testCreditFormatter() {
        String credit = "10";
        assertEquals(data.creditFormatter(credit, 6), "000010.00");
        assertEquals(data.creditFormatter(credit, 3), "010.00");
        assertEquals(data.creditFormatter(credit, 2), "10.00");

        credit = "10.92";
        assertEquals(data.creditFormatter(credit, 6), "000010.92");
        assertEquals(data.creditFormatter(credit, 3), "010.92");
        assertEquals(data.creditFormatter(credit, 2), "10.92");
    }

    @Test
    public void testWriteBuy() {
        Buyer user = new Buyer("amjad", 100.00);
        Game game = new Game("COD", 50.00, Game.NFS, user.getName());
        Game game1 = new Game("FIFA", 50.00, Game.NFS, user.getName());
        data.addLogin(user);
        data.writeBuy(game);
        data.writeBuy(game1);
        assertEquals(data.scanner1.nextLine(), "          amjad,BS,000000.00,     " +
                "                 COD:050.00:NFS|                     FIFA:050.00:NFS|");

    }

    @Test
    public void testWriteSell() {
        Seller user = new Seller("amjad", 100.00);
        Game game = new Game("COD", 50.00, Game.FS, user.getName());
        game.setDiscount(20.00);
        Game game1 = new Game("FIFA", 50.00, Game.FS, user.getName());
        game1.setDiscount(13.99);
        data.addLogin(user);
        data.writeSell(game);
        data.writeSell(game1);
        assertEquals(data.scanner1.nextLine(), "          amjad,SS,000100.00,     " +
                "                 COD:050.00:20.00:FS|                     FIFA:050.00:13.99:FS|");
    }

    @Test
    public void testRemoveUser() {
        Seller user = new Seller("amjad1", 100.00);
        Game game = new Game("COD", 50.00, Game.FS, user.getName());
        game.setDiscount(20.00);
        Game game1 = new Game("FIFA", 50.00, Game.FS, user.getName());
        game1.setDiscount(13.99);
        data.addLogin(user);
        data.writeSell(game);
        data.writeSell(game1);

        assertEquals(data.scanner1.nextLine(), "         amjad1,SS,000100.00,     " +
                "                 COD:050.00:20.00:FS|                     FIFA:050.00:13.99:FS|");

        assertEquals(data.getUserLine("amjad1"), 0);//will return the first line

        data.removeUser("amjad1");

        assertEquals(data.getUserLine("amjad1"), -1);//if a user does not exist, we return -1
    }

    @Test
    public void testWriteAuction() {
        data.writeAuction("OFF");
        assertTrue(data.scanner2.hasNextLine());
        assertEquals(data.scanner2.nextLine(), "AuctionSale:OFF");
    }

    @Test
    public void testRemoveGameUserLogins() {
        Seller user = new Seller("amjad1", 100.00);
        Game game = new Game("COD", 50.00, Game.FS, user.getName());
        game.setDiscount(20.00);
        Game game1 = new Game("FIFA", 50.00, Game.FS, user.getName());
        game1.setDiscount(13.99);
        data.addLogin(user);
        data.writeSell(game);
        data.writeSell(game1);

        try {
            data.scanner = new Scanner(data.userLoginsRead);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(data.scanner.nextLine(), "         amjad1,SS,000100.00,     " +
                "                 COD:050.00:20.00:FS|                     FIFA:050.00:13.99:FS|");

        data.removeGameUserLogins("amjad1", "FIFA");
        assertEquals(data.scanner1.nextLine(), "         amjad1,SS,000100.00,     " +
                "                 COD:050.00:20.00:FS|");
    }

    @Test
    public void testAddGame() {
        Seller user = new Seller("amjad1", 100.00);
        Seller user1 = new Seller("amjad2", 100.00);
        Game game = new Game("COD", 50.00, Game.FS, user.getName());
        game.setDiscount(20.00);
        Game game1 = new Game("FIFA", 50.00, Game.FS, user1.getName());
        game1.setDiscount(13.99);
        data.addLogin(user);
        data.addLogin(user1);

        data.addGame(game, "amjad1", false);
        data.addGame(game1, "amjad2", true);

        assertEquals(data.scanner1.nextLine(), "         amjad1,SS,000100.00,     " +
                "                 COD:050.00:NFS|");


        assertEquals(data.scanner1.nextLine(), "         amjad2,SS,000100.00,     " +
                "                FIFA:050.00:13.99:FS|");

    }

    @Test
    public void testGetTransaction() {
        String info = "00           majed AA 000958.00";
        assertEquals(Arrays.toString(data.getTransaction(info)),
                Arrays.toString(new String[]{"00", "majed", "AA", "000958.00"}));

        info = "01           majed AA 000958.00";
        assertEquals(Arrays.toString(data.getTransaction(info)),
                Arrays.toString(new String[]{"01", "majed", "AA", "000958.00"}));

        info = "02           majed AA 000958.00";
        assertEquals(Arrays.toString(data.getTransaction(info)),
                Arrays.toString(new String[]{"02", "majed", "AA", "000958.00"}));

        info = "10              ab AA 000958.00";
        assertEquals(Arrays.toString(data.getTransaction(info)),
                Arrays.toString(new String[]{"10", "ab", "AA", "000958.00"}));

        info = "07           bobed AA 000958.00";
        assertEquals(Arrays.toString(data.getTransaction(info)),
                Arrays.toString(new String[]{"07", "bobed", "AA", "000958.00"}));

        info = "06           bobed AA 000999.00";
        assertEquals(Arrays.toString(data.getTransaction(info)),
                Arrays.toString(new String[]{"06", "bobed", "AA", "000999.00"}));


        info = "05           bobed           majed 999999.99";
        assertEquals(Arrays.toString(data.getTransaction(info)),
                Arrays.toString(new String[]{"05", "bobed", "majed", "999999.99"}));

        info = "03 game2                               majed 20.00 030.00";
        assertEquals(Arrays.toString(data.getTransaction(info)),
                Arrays.toString(new String[]{"03", "game2", "majed", "20.00", "030.00"}));

        info = "04 game2                     majed                     *&,am";
        assertEquals(Arrays.toString(data.getTransaction(info)),
                Arrays.toString(new String[]{"04", "game2", "majed", "*&,am"}));


        info = "08 game2                     majed                     *&,am";
        assertEquals(Arrays.toString(data.getTransaction(info)),
                Arrays.toString(new String[]{"08", "game2", "majed", "*&,am"}));

        info = "09 game2                     majed                     *&,am";
        assertEquals(Arrays.toString(data.getTransaction(info)),
                Arrays.toString(new String[]{"09", "game2", "majed", "*&,am"}));

    }

    @Test
    public void testValidatePattern() {
        assertTrue(data.validateInfo("         anas A,BS,099980.00,                    " +
                "game2:024.00:NFS|"));
        assertTrue(data.validateInfo("         anas A,BS,099980.00,"));
        assertFalse(data.validateInfo("    anas A,BS,099980.00,                    " +
                "game2:024.00:NFS|"));
        assertFalse(data.validateInfo(""));
    }


}

