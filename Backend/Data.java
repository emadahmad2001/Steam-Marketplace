package Backend;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class representing the back end of a game selling system, that manipulates the given objects in the system
 */
public class Data {
    public final String userLoginsPattern = "^(?:((.){15}),(AA|SS|FS|BS),(\\d{6}\\.\\d{2}),((?:((.){25}):" +
            "((\\d{3}\\.\\d{2})(:\\d{2}\\.\\d{2})?):(NFS|FS)\\|)+)?)$";
    public static final String ADMIN = "AA", BUYER = "BS", STANDARD = "FS", SELLER = "SS";
    public String userLoginsPath = "Backend/UserLogins.txt";
    public String databasePath = "Backend/Database.txt";
    public String dailyPath = "Backend/daily.txt";
    public Transaction transaction;
    public FileWriter userLoginsWrite;
    public FileWriter databaseWrite;
    public File dailyRead;
    public File userLoginsRead;
    public File databaseRead;
    public Scanner scanner;
    public Scanner scanner1;
    public Scanner scanner2;
    public Scanner scanner3;

    /**
     * Initialize the system by going through the databases, populating the system, and
     * running the transactions in daily.txt
     */
    public void initialStartup() {

        try {
            userLoginsRead = new File(userLoginsPath);
            scanner1 = new Scanner(userLoginsRead);
            while (scanner1.hasNextLine()) {//Loops and creates every user
                String line = scanner1.nextLine();
                if (validateInfo(line)) {
                    String[] userInfo = getBasicUserInfo(line);//gets user basic info (name, type, credit)
                    ArrayList<String[]> gameInfo = getUserGames(line);//gets all his games
                    transaction.createUser(userInfo[0].strip(), userInfo[1],
                            Double.parseDouble(userInfo[2]), gameInfo, false, true);
                } else {//if an entry is invalid, system will close
                    System.out.println("<FATAL ERROR> Invalid entry, exiting system: " + userLoginsRead.getName());
                    System.exit(0);

                }
            }
            scanner1.close();
        } catch (IOException e) {//closes if the file does not exist
            System.out.println("<FATAL ERROR> No such file exists, exiting system");
            System.exit(0);
        }
        try {
            databaseRead = new File(databasePath);
            scanner2 = new Scanner(databaseRead);
            while (scanner2.hasNextLine()) {
                String line = scanner2.nextLine();
                if (validateDatabase(line)) {//if the database is valid, set the auction either on or off
                    transaction.setAuctionOn(line.split(":")[1].equals("ON"));
                    if (line.split(":")[1].equals("ON")) {
                        transaction.auctionSaleInitial();
                    }
                    break;
                } else {//If file is not correctly written, formats it properly
                    databaseWrite = new FileWriter(databasePath, true);
                    new PrintWriter(databasePath).close();
                    databaseWrite.write("AuctionSale:OFF" + System.lineSeparator());
                    databaseWrite.close();

                }
            }
            scanner2.close();
        } catch (IOException e) {//closes if the file does not exist
            System.out.println("<FATAL ERROR> No such file exists, exiting system: " + databaseRead.getName());
            System.exit(0);
        }

        try {
            dailyRead = new File(dailyPath);
            scanner3 = new Scanner(dailyRead);
            while (scanner3.hasNextLine()) {//execute every transaction
                String line = scanner3.nextLine();
                String[] info = getTransaction(line);
                if (info != null) {
                    transaction.processTransaction(info);// lets the Transaction class handle
                    //object manipulation
                }
            }
            scanner3.close();

        } catch (FileNotFoundException e) {//closes if the file does not exist
            System.out.println("<FATAL ERROR> No such file exists, exiting system: " + dailyRead.getName());
            System.exit(0);
        }
    }

    /**
     * Opens up userLogins for writing
     */
    public void openUserLoginsW() {
        try {
            userLoginsWrite = new FileWriter(userLoginsPath, true);
        } catch (IOException ignored) {
        }
    }

    /**
     * Closes userLogins for writing
     */
    public void closeUserLoginsW() {
        try {
            userLoginsWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Opens up userLogins for reading
     */
    public void openUserLoginsR() {
        try {
            userLoginsRead = new File(userLoginsPath);
            scanner = new Scanner(userLoginsRead);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Validate the database's input
     *
     * @param info information to be validated
     * @return True iff the info is valid
     */
    public boolean validateDatabase(String info) {
        Pattern p1 = Pattern.compile("AuctionSale:(ON|OFF)");
        Matcher m1 = p1.matcher(info);
        if (m1.matches()) {
            return m1.matches();
        }
        return false;
    }

    /**
     * Returns the pieces of information present in the transaction line of info, depending on what type
     * it is, buy using regex patterns
     *
     * @param info a transaction line to be read
     * @return Array of information iff the info is a valid transaction line. Exits the system otherwise
     * and returns null
     */
    public String[] getTransaction(String info) {
        String typeT = "^(0[0-9]|10)";//pattern that accepts inputs of 10 or
        // 0X, where X is a digit between [0,9]
        String username = "(?:\\s((.){15}))";//pattern that accepts a space plus a word composed of
        // any character, word is size 15
        String typeU = "(AA|SS|FS|BS)";// The types
        String credit = "(\\d{6}\\.\\d{2})";// Pattern for a credit input: XXXXXX.XX
        String game = "(?:\\s((.){25}))";//pattern that accepts a space plus a word composed of
        // any character, word is size 25
        String saleString = "(\\d{3}\\.\\d{2})";// Pattern for a sale input: XXX.XX
        String discountString = "(\\d{2}\\.\\d{2})";// Pattern for a discount input: XX.XX

        //Pattern for 00-login, 01-create, 02-delete, 06-addcredit, 10-logout, 07-auction
        Pattern p1 = Pattern.compile(typeT + username + "\\s" + typeU + "\\s" + credit + "$");
        Matcher m1 = p1.matcher(info);
        if (m1.matches()) {//if a pattern matches, return the appropriate information needed to
            //process it, and refined if needed (strip())
            return new String[]{m1.group(1), m1.group(2).strip(), m1.group(4), m1.group(5)};
        }
        //Pattern for 05-refund
        Pattern p2 = Pattern.compile(typeT + username + username + "\\s" + credit + "$");
        Matcher m2 = p2.matcher(info);
        if (m2.matches()) {
            return new String[]{m2.group(1), m2.group(2).strip(), m2.group(4).strip(), m2.group(6)};
        }
        //Pattern for 03-sell
        Pattern p3 = Pattern.compile(typeT + game + username +
                "\\s" + discountString + "\\s" + saleString + "$");
        Matcher m3 = p3.matcher(info);
        if (m3.matches()) {
            return new String[]{m3.group(1), m3.group(2).strip(), m3.group(4).strip(), m3.group(6),
                    m3.group(7)};
        }

        //Pattern for 04-buy,08-removegame,09-gift
        Pattern p4 = Pattern.compile(typeT + game + username + username + "$");
        Matcher m4 = p4.matcher(info);
        if (m4.matches()) {
            return new String[]{m4.group(1), m4.group(2).strip(), m4.group(4).strip(),
                    m4.group(6).strip()};
        }
        //if it doesn't match any patterns, it is invalid, thus we exit the system and print a message
        System.out.println("<FATAL ERROR> Invalid entry: " + info);
        System.out.println("Exiting system");
        System.exit(0);
        return null;
    }

    /**
     * Validate a line userLogins
     *
     * @param info a line in userLogins
     * @return True iff it is valid, false otherwise
     */
    public boolean validateInfo(String info) {

        Pattern pattern = Pattern.compile(userLoginsPattern);//compile the pattern

        Matcher matcher = pattern.matcher(info);
        return matcher.matches();//return if it matches
    }

    /**
     * Returns the basic information (name, type, credit) of a line in userLogins
     *
     * @param info a line in userLogins
     * @return an Array of information if info is a valid input , null otherwise
     */
    public String[] getBasicUserInfo(String info) {
        Pattern pattern = Pattern.compile(userLoginsPattern);


        Matcher matcher = pattern.matcher(info);
        if (matcher.matches()) {//if info matches, return appropriate information
            return new String[]{matcher.group(1), matcher.group(3), matcher.group(4)};
        }
        return null;
    }

    /**
     * Returns the full information of a line in userLogins
     *
     * @param info a line to be manipulated
     * @return an Array of information if info is a valid input , null otherwise
     */
    public String[] getFullUserInfo(String info) {
        Pattern pattern = Pattern.compile(userLoginsPattern);

        Matcher m = pattern.matcher(info);
        if (m.matches()) {//if info matches
            if (m.group(5) == null) {//if the user doesn't have games
                return new String[]{m.group(1), m.group(3), m.group(4)};
            } else {//if he does have games, add them
                return new String[]{m.group(1), m.group(3), m.group(4), m.group(5)};
            }
        }
        return null;
    }

    /**
     * Returns the game information of a line in userLogins
     *
     * @param info a line to be manipulated
     * @return an Array of game information if info is a valid input , null otherwise
     */
    public ArrayList<String[]> getUserGames(String info) {
        ArrayList<String[]> gameInfo = new ArrayList<>();
        Pattern p = Pattern.compile("((?:((.){25}):((\\d{3}\\.\\d{2})(:\\d{2}\\.\\d{2})?):(NFS|FS)\\|))");
        Matcher m = p.matcher(info);
        while (true) {
            if (m.find()) {//keep looking in the info for the pattern
                String[] moneyInfo = m.group(4).split(":");
                if (moneyInfo.length == 2) {//if its a game that is for sale (has a discount price and
                    //normal price) return that info as well
                    gameInfo.add(new String[]{m.group(2), moneyInfo[0], moneyInfo[1], m.group(7)});
                } else {//normal game
                    gameInfo.add(new String[]{m.group(2), moneyInfo[0], m.group(7)});
                }
            } else {//when no more game are detected, break out the loop
                break;
            }
        }
        return gameInfo;//return the info
    }

    /**
     * Adds the new user to userLogins
     *
     * @param user a User object to be added
     */
    public void addLogin(User user) {
        String spaces = " ".repeat(15 - user.getName().length());//pad name with spaces to adhere to format
        String credit = creditFormatter(String.valueOf(user.getCredit()), 6);//format
        //the credit to adhere to format
        String text = spaces + user.getName() + "," + user.getType() + "," + credit + ",";//make string
        ArrayList<String> users = userLoginsToArray();//get logins
        users.add(text);//add the text to it
        editUserLogins(users);//write to userLogins
    }

    /**
     * Erases userLogins, takes the array list of info, and writes the info to userLogins
     *
     * @param info an array list of valid lines in to be inputed into userLogins
     */
    public void editUserLogins(ArrayList<String> info) {
        openUserLoginsW();
        try {
            new PrintWriter(userLoginsPath).close();//delete file contents
            for (String str : info) {//loop and write every line
                userLoginsWrite.write(str + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("<FATAL ERROR> File does not exist; exiting system: " + userLoginsPath);
            System.exit(0);
        }
        closeUserLoginsW();
    }

    /**
     * Get the line at which that name occurs at in userLogins
     *
     * @param name the person we are looking for
     * @return the line at which his information is, -1 otherwise
     */
    public int getUserLine(String name) {
        int lineNum = 0;
        openUserLoginsR();
        while (scanner.hasNextLine()) {
            String nameSpaces = scanner.nextLine().substring(0, 15);
            if (nameSpaces.strip().startsWith(name)) {
                return lineNum;
            }
            lineNum += 1;
        }
        return -1;
    }

    /**
     * Convert userLogins to an Arraylist of strings
     *
     * @return userLogins in an arrayList format
     */
    public ArrayList<String> userLoginsToArray() {
        ArrayList<String> fileArray = new ArrayList<>();
        openUserLoginsR();
        while (scanner.hasNextLine()) {
            fileArray.add(scanner.nextLine());
        }
        return fileArray;
    }

    /**
     * Edits the credit of a string info that adheres to the userLogins format
     *
     * @param info   line we are updating
     * @param credit the amount to be update to the user
     * @return the manipulated string with updated credit
     */
    public String editCredit(String info, double credit) {
        String[] infos = getFullUserInfo(info);//coverts info into an array
        if (infos == null) {// if its null return;
            return null;
        }
        //call creditFormatter to format the credit properly
        String creditNum = creditFormatter(String.valueOf(Double.parseDouble(infos[2]) + credit), 6);
        //update it in infos
        infos[2] = creditNum;
        StringBuilder txt = new StringBuilder();
        if (infos.length == 3) {//if the user has no games
            for (String element : infos) {
                txt.append(element).append(",");
            }
            return txt.toString();
        }
        int index = 0;
        for (String element : infos) {//if the user has games, we need to format differently
            if (index < 3) {
                txt.append(element).append(",");
                index += 1;
            } else {
                txt.append(element);
            }
        }
        return txt.toString();
    }

    /**
     * Update a user's credit in userLogins
     *
     * @param name   the user that will have his credit updated
     * @param credit the credit to be added
     */
    public void updateCreditLogins(String name, double credit) {
        ArrayList<String> fileArray = userLoginsToArray();// convert to ArrayList
        int lineNum = getUserLine(name);//get the line where the user is
        if (lineNum == -1) {
            return;
        }
        fileArray.set(lineNum, editCredit(fileArray.get(lineNum), credit));// edit the ArrayList
        editUserLogins(fileArray);//Write that to userLogins

    }

    /**
     * Format the credit with the number of zeroes specified
     *
     * @param credit    credit string to be formatted
     * @param numZeroes number of zeroes that should be present
     * @return the formatted string
     */
    public String creditFormatter(String credit, int numZeroes) {
        int x = (int) Double.parseDouble(credit);//convert the credit from string to double
        String zeroes = "0".repeat(numZeroes - String.valueOf(x).length());//adds the appropriate number of zeroes
        //in front of the credit
        String dennis = String.valueOf(Double.parseDouble(credit));
        double f = Double.parseDouble(dennis);
        String num = String.format("%.2f", new BigDecimal(f));//Format it
        return zeroes + num;
    }

    /**
     * Write the buy; add the game to the appropriate user
     *
     * @param game a Game object
     */
    public void writeBuy(Game game) {
        addGame(game, game.getOwner(), false);//add the game in bought format
        updateCreditLogins(game.getOwner(), -game.getPrice());// update credit of buyer
    }

    /**
     * Write the sell; add the game to the appropriate user
     *
     * @param game a Game object
     */
    public void writeSell(Game game) {
        addGame(game, game.getOwner(), true);
    }

    /**
     * Remove a user from userLogins
     *
     * @param name person to be removed
     */
    public void removeUser(String name) {
        ArrayList<String> fileArray = userLoginsToArray();
        int lineNum = getUserLine(name);
        if (lineNum != -1) {
            fileArray.remove(lineNum);
            editUserLogins(fileArray);
        }
    }

    /**
     * Update auction database with status
     *
     * @param status status of auction
     */
    public void writeAuction(String status) {
        try {
            new PrintWriter(databasePath).close();//delete contents
            File file = new File(databasePath);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("AuctionSale:" + status);//write to the file with the info
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("<FATAL ERROR> File does not exist; exiting system " + databasePath);
            System.exit(0);
        }


    }

    /**
     * Remove a game from a user in userLogins
     *
     * @param userName user to get game removed
     * @param gameName game to be removed
     */
    public void removeGameUserLogins(String userName, String gameName) {
        StringBuilder userLoginsInfo = new StringBuilder();//Create a string that ill add all info to
        StringBuilder gameString = new StringBuilder();//This will be used for each game
        ArrayList<String> userLogins = userLoginsToArray();//Convert userLogins to array of string
        int lineNum = getUserLine(userName);//Get the line at which our user is
        if (lineNum == -1) {
            return;
        }
        ArrayList<String[]> userGames = getUserGames(userLogins.get(lineNum));//Get an array list
        //of String[] that each contain a gaMe
        String[] userInfo = getBasicUserInfo(userLogins.get(lineNum));//Get a String[] of that users basic info
        //In the format of userLogins

        userGames.removeIf(game -> game[0].strip().equals(gameName));//Find that game occurence
        // in userGames and remove it
        for (String info : userInfo) {
            //start rewriting that userLogins line without the game by going through the userInfo and writing
            //it to userLoginsInfo
            userLoginsInfo.append(info).append(",");
        }
        //For each game, we want to create a string that follows the format and add it to the line
        for (String[] gameInfo : userGames) {
            //loop through list of games of that user
            for (String info : gameInfo) {
                //for each game, separate its info by ":"
                gameString.append(info).append(":");
            }
            gameString.deleteCharAt(gameString.length() - 1);//remove the last ":"
            userLoginsInfo.append(gameString);//add this info to the line
            userLoginsInfo.append("|");//add the char to adhere to the format
            gameString = new StringBuilder();//reset the game info for the next game

        }
        userLogins.set(lineNum, userLoginsInfo.toString());//set the users line in our array of userLogins
        //to our new line without that game

        editUserLogins(userLogins);//Write the info to userLogins


    }

    /**
     * Add game to appropriate user
     *
     * @param game game to be added
     * @param name name of user to get game added
     * @param sell A boolean that tells us what format to follow
     */
    public void addGame(Game game, String name, boolean sell) {
        String credit = creditFormatter(String.valueOf(game.getPrice()), 3);//format game price
        String spaces = " ".repeat(25 - game.getName().length());//add necessary whitespaces in front of game name
        //format discount
        String discountFormat = creditFormatter(String.valueOf(game.getDiscountPercent()), 2);
        String gameNew;
        if (sell) {//If it's a game that is being put up fro sale, add the discount price in format
            gameNew = spaces + game.getName() + ":" + credit + ":" + discountFormat + ":" + Game.FS + "|";
        } else {// no discount price otherwise
            gameNew = spaces + game.getName() + ":" + credit + ":" + Game.NFS + "|";
        }
        //Write this info the userLogins
        ArrayList<String> fileArray = userLoginsToArray();
        int lineNum = getUserLine(name);
        fileArray.set(lineNum, fileArray.get(lineNum) + gameNew);
        editUserLogins(fileArray);

    }

}