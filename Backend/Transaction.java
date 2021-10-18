package Backend;


import java.util.ArrayList;

/**
 * A class representing the back end of a game selling system, that manipulates the given objects in the system
 */
public class Transaction {
    final double maxCredit = 999999.99;//max credit
    final double dailyCredit = 1000.00;//max daily credit
    public Data data;
    public Inventory<User> userList = new Inventory<>();
    public User currentUser = null;
    public boolean auctionOn = false;

    public void initialStartupT() {
        this.data = new Data();
        data.transaction = this;
        data.initialStartup();
    }

    /**
     * Process a given transaction
     *
     * @param info Array of information
     */
    public void processTransaction(String[] info) {
        switch (info[0]) {//depending on the type, pick a specific transaction, with all the needed info
            case "00":
                loginVerification(info[1], info[2], info[3]);
                break;
            case "01":
                createUser(info[1], info[2], Double.parseDouble(info[3]), null, true, false);
                break;
            case "02":
                deleteUsers(info[1]);
                break;
            case "03":
                processSell(info[2], info[1], info[4], info[3]);
                break;
            case "04":
                processBuy(info[2], info[1], info[3]);
                break;
            case "05":
                processRefund(info[1], info[2], Double.parseDouble(info[3]));
                break;
            case "06":
                addCredit(info[1], Double.parseDouble(info[3]));
                break;
            case "07":
                auctionSale();
                break;
            case "08":
                processRemoveGame(info[2], info[1]);
                break;
            case "09":
                processGift(info[1], info[2], info[3]);
                break;
            case "10":
                logout(info[1], info[2], info[3]);
                break;
        }
    }

    /**
     * Verify if this user can login
     *
     * @param user   user to login
     * @param type   his type
     * @param credit the credit he has
     */
    public void loginVerification(String user, String type, String credit) {
        if (!checkUserNull()) {//check if a user is logged in
            System.out.println("A user is already logged in");
            return;
        }
        if (userList.getObj(user) != null) {//if the user exits, log him in
            currentUser = userList.getObj(user);
            System.out.println("You are now logged in: " + user);
            //Print warnings if his type/credit is wrong
            if (!currentUser.getType().equals(type)) {
                System.out.println("Warning : input accepted but type in daily.txt invalid");
            }
            if (currentUser.getCredit() != Double.parseDouble(credit)) {
                System.out.println("Warning : input accepted but credit in daily.txt invalid");
            }
        } else {
            System.out.println("User does not exist");
        }

    }

    /**
     * Log a user out of the session
     *
     * @param user   user to logout
     * @param type   his account type
     * @param credit his credit balance
     */
    public void logout(String user, String type, String credit) {
        if (checkUserNull()) {// if the current user is null, no one can loggout
            System.out.println("No one is logged in");
            return;
        }
        if (currentUser.getName().equals(user)) {//if the current user is the person that wants to logout
            //print warning if he has invalid info
            if (!currentUser.getType().equals(type)) {
                System.out.println("Warning : input accepted but type in daily.txt invalid");
            }
            if (currentUser.getCredit() != Double.parseDouble(credit)) {
                System.out.println("Warning : input accepted but credit in daily.txt invalid");
            }
            currentUser = null;//log him out
            System.out.println("You are now logged out: " + user);
        } else {//otherwise, an invalid person is trying to log him out, don't let him
            System.out.println("That user is not the current user that is logged in; cannot logout");
        }
    }

    /**
     * Check if the current user is null
     *
     * @return if the current user is null, True if yes, False otherwise
     */
    public boolean checkUserNull() {
        return currentUser == null;
    }

    /**
     * Turn the auction on/off
     *
     * @param auctionOn boolean that tells us wether we turn the auction on/off
     */
    public void setAuctionOn(boolean auctionOn) {
        this.auctionOn = auctionOn;
    }


    /**
     * Return a boolean expression stating whether a transaction is able to be completed or
     * not, true if a transaction can be done, and false otherwise.
     *
     * @param seller - a seller object
     * @param game   - a game object
     * @return True if and only if a transaction can be completed
     */
    public boolean verifyBuy(String seller, String game, String buyer) {
        if (checkUserNull()) {
            System.out.println("No user currently logged in; cannot buy");
            return false;//makes sure user is not null
        }
        if (!currentUser.getName().equals(buyer)) {
            System.out.println("The user trying to buy, is not the one logged in; cannot buy");
            return false;// makes sure the user is the buyer
        }
        if (buyer.equals(seller)) {//you can't buy from yourself
            System.out.println("Can't buy your own game; cannot buy");
            return false;
        }
        User seller0 = userList.getObj(seller);//get the seller
        if (seller0 == null) {
            System.out.println("User does not exist; cannot buy");
            return false;//makes sure the seller exists
        }
        if (seller0.getType().equals(Data.BUYER)) {
            System.out.println("Seller is not of appropriate type; cannot buy");
            return false;//Make sure seller is a seller and not a buyer
        }
        Game game0 = seller0.getInv().getObj(game);//get the game
        if (game0 == null) {
            System.out.println("Seller is not selling this game; cannot buy");
            return false;//makes sure the game in the seller's inventory exists
        }
        if (currentUser.getInv().getObj(game) != null) {
            System.out.println("User already has this game; cannot buy");
            return false; //checks if buyer already has that game
        }
        if (!game0.getStatus().equals(Game.FS)) {
            System.out.println("Game is not for sale; cannot buy");
            //makes sure the game is able to be bought
            return false;
        }
        if (!game0.isSelling()) {
            System.out.println("Game not available for sale today; cannot buy");
            //checks that the game is available to be bought today
            return false;
        }
        return (game0.getPrice() <= currentUser.getCredit());
        //returns true if the buyer has enough to buy it
    }


    /**
     * process the transaction between the buyer and the seller if and only if
     * the transaction is verified.
     *
     * @param seller person that is selling game
     * @param game   the game being sold
     * @param buyer  the purchaser of teh game
     */
    public void processBuy(String seller, String game, String buyer) {
        if (verifyBuy(seller, game, buyer)) {// if the transaction is verified
            User seller0 = userList.getObj(seller);//Get the seller and the game objects
            Game game0 = seller0.getInv().getObj(game);
            currentUser.buy(game0);//let the current user buy the game
            data.writeBuy(currentUser.getInv().getObj(game0.getName()));//write it to the database
            System.out.println(buyer + " has successfully bought: " + game);
            if (seller0.getCredit() + game0.getPrice() > maxCredit) {//if a seller reached his max credit
                //print a warning and max his balance
                System.out.println("Warning: " + seller0.getName() + " has reached his maximum credit limit.");
                data.updateCreditLogins(seller, maxCredit - seller0.getCredit());//update database
                seller0.setCredit(maxCredit);//set his credit in the object
            } else {//otherwise just update his credit accordingly
                seller0.setCredit(seller0.getCredit() + game0.getPrice());
                data.updateCreditLogins(seller, game0.getPrice());
            }
        }
    }

    /**
     * Verify a sell transaction
     *
     * @param seller          person that is trying to sell
     * @param gameName        the name of the game he is trying to sell
     * @param price           the price of the game
     * @param discountPercent the discount %
     * @return True, iff the transaction is valid, false otherwise
     */
    public boolean verifySell(String seller, String gameName, String price, String discountPercent) {
        if (checkUserNull()) {
            System.out.println("No user currently logged in; cannot sell");
            return false; //check for null user
        }
        if (!currentUser.getName().equals(seller)) {
            System.out.println("Current user is not the one selling; cannot sell");
            return false; // checks if the current user is the user trying to sell
        }
        if (currentUser.type.equals(Data.BUYER)) {
            System.out.println("Buyer cannot sell");
            return false; //makes sure the current user is not a buyer
        }
        if (Double.parseDouble(discountPercent) > 90.0) {
            System.out.println("Discount is larger than 90%; cannot sell");
            return false;//make sure discount is less than 90%
        }
        if (currentUser.getInv().getObj(gameName) != null) {
            System.out.println("You are already selling this game; cannot sell");
            return false; //makes sure he is not selling an item that he is already selling
        }
        return gameName.length() <= 25 && Double.parseDouble(price) <= 999.99; //checks length and price
    }

    /**
     * Process a sell transaction
     *
     * @param seller          person that is trying to sell
     * @param gameName        the name of the game he is trying to sell
     * @param price           the price of the game
     * @param discountPercent the discount %
     */
    public void processSell(String seller, String gameName, String price, String discountPercent) {
        if (verifySell(seller, gameName, price, discountPercent)) {
            currentUser.sell(gameName, Double.parseDouble(price), Double.parseDouble(discountPercent));
            data.writeSell(currentUser.getInv().getObj(gameName));
            System.out.println(seller + " has successfully put " + gameName + " up for sale for " + price + "$");
        }
    }

    /**
     * delete users from the inventory user list and delete all games related to them
     *
     * @param name - name of the user
     */
    public void deleteUsers(String name) {
        if (checkUserNull()) {//if the current use is null break
            System.out.println("No user logged in; cannot delete");
            return;
        }

        if (!currentUser.getType().equals(Data.ADMIN)) {//make sure this is an admin
            System.out.println("Not an admin, cannot delete");
            return;
        }
        if (currentUser.getName().equals(name)) {//make sure the admin is not trying to delete himself
            System.out.println("Cannot delete yourself");
            return;
        }
        User person = userList.getObj(name);
        if (person == null) {//make sure that person exists
            System.out.println("User you are trying to delete does no exist");
            return;
        }
        ((Admin) currentUser).deleteUser(name);//update objects
        data.removeUser(name);//update database
        System.out.println(person.getName() + " has been successfully deleted from the user list.");

    }

    /**
     * AddCredit function, that adds credit to a specified account
     *
     * @param name   name of user
     * @param credit credit trying to be added
     */
    public void addCredit(String name, double credit) {
        if (checkUserNull()) {//Checks null user
            System.out.println("No user currently logged in; Cannot add credit");
            return;
        }

        if (userList.getObj(name) == null) {//Makes sure the user exists
            System.out.println("User does not exist");
            return;
        }

        if (currentUser.getName().equals(name)) {//check if its a user trying to add to himself
            dailyCreditHelper(credit, name, false);
        } else if (currentUser.getType().equals(Data.ADMIN)) {//check if its a admin trying to add to a user
            dailyCreditHelper(credit, name, true);
        } else {//Print warning saying the current user cant add to anyone else
            System.out.println(currentUser.getName() + " cannot add money to: " + name);
        }

    }

    /**
     * Helper for addCredit
     *
     * @param name   name of user
     * @param credit credit trying to be added
     * @param admin  specifies if an admin is committing this transaction
     */
    public void dailyCreditHelper(Double credit, String name, boolean admin) {
        if (currentUser.getDailyCredit() == dailyCredit) {//check if daily credit limit reached
            System.out.println("Daily add credit limit reached");
            return;
        }
        if (currentUser.getDailyCredit() + credit > dailyCredit) {//If the amount that is being added exceeds
            //1000.00, max it out, and say its reached its max
            currentUser.setDailyCredit(dailyCredit);
            System.out.println("Daily add credit limit reached");
        } else {//else, set his daily amount to his current daily credit plus the amount he is adding
            currentUser.setDailyCredit(currentUser.getDailyCredit() + credit);
        }
        //in the last two if/else, we will updated the credit in the object, and in the database
        addCreditHelper(name, credit, admin);
    }

    /**
     * Helper for addCredit
     *
     * @param name   name of user
     * @param credit credit trying to be added
     * @param admin  specifies if an admin is committing this transaction
     */
    public void addCreditHelper(String name, Double credit, boolean admin) {
        if (!admin) {// if its not an admin, were gonna call the setCredit() of user
            if (currentUser.getCredit() + credit > maxCredit) {//If the credit added is more than the max
                //allowed balance, we set it to the max, and print out a warning
                currentUser.setCredit(maxCredit);
                data.updateCreditLogins(name, maxCredit - currentUser.getCredit());
                System.out.println("Credit amount exceeds max; your balance is now maxed");
            } else {
                currentUser.setCredit(currentUser.getCredit() + credit);
                data.updateCreditLogins(name, credit);
                System.out.println("Successfully added: " + " $" + credit + "to " + name + ". Current balance now: "
                        + currentUser.getCredit());
            }
        } else {//if its an admin, were gonna use the updateCredit of admin instead
            User user = userList.getObj(name);
            if (currentUser.getCredit() + credit > maxCredit) {
                ((Admin) currentUser).updateCredit(maxCredit - user.getCredit(), user);
                data.updateCreditLogins(name, maxCredit - user.getCredit());
                System.out.println("Credit amount exceeds max; your balance is now maxed");
            } else {
                ((Admin) currentUser).updateCredit(credit, user);
                data.updateCreditLogins(name, credit);
                System.out.println("Successfully added: " + " $" + credit + "to " + name + ". Current balance now: "
                        + user.getCredit());
            }
        }
    }

    /**
     * Set all the games to discount price in the initial startup (if necessary)
     */
    public void auctionSaleInitial() {
        for (User user : userList.getItems()) {//loop through the users
            for (Game game : user.getInv().getItems()) {//get their games
                if (game.isSelling() && game.getDiscountPercent() != 0) {//if its a game that is selling, set it's
                    // discount
                    game.setPrice((game.getPrice() - (game.getPrice() * game.getDiscountPercent() / 100)));
                }
            }
        }
    }

    /**
     * Turns on/off an auction sale (button)
     */
    public void auctionSale() {
        if (!checkUserNull() && currentUser.getType().equals(Data.ADMIN)) {// if the currenr user is not null
            //and it is an admin
            if (auctionOn) {//if it was on, turn it off, and vice versa
                setAuctionOn(false);
                data.writeAuction("OFF");//update our database
                ((Admin) currentUser).setAuctionPrices(false);//update all the game prices accordingly
                System.out.println("Auction sale now OFF");
            } else {
                setAuctionOn(true);
                data.writeAuction("ON");
                ((Admin) currentUser).setAuctionPrices(true);
                System.out.println("Auction sale now ON");
            }

        }
    }

    /**
     * Return true if a user refund is able to be completed, false otherwise
     *
     * @param buyer  - a buyer object
     * @param seller - a seller object
     * @return True if and only if a transaction can be complete
     */
    public boolean verifyRefund(String buyer, String seller, Double credit) {
        if (checkUserNull()) {
            System.out.println("No user currently logged in; cannot refund");
            return false;
        }
        if (!currentUser.type.equals(Data.ADMIN)) {//make sure an admin is refunding
            System.out.println("Cannot refund, current user not an admin");
            return false;
        }
        User buyer0 = userList.getObj(buyer);
        User seller0 = userList.getObj(seller);
        if (buyer0 == null || seller0 == null) {//make sure these users exist
            System.out.println("Cannot refund, Buyer or Seller does not exists");
            return false;
        }
        if (buyer0.getType().equals(Data.SELLER) || seller0.getType().equals(Data.BUYER)) {
            System.out.println("Wrong type of account in refund; cannot refund");
            return false;//if the seller we are given is a buyer account, he is of wrong typ
            //and vice versa for the buyer
        }
        if (seller0.getCredit() - credit < 0) {//Make sure the seller has enough money to refund
            System.out.println("Seller has insufficient funds, cannot refund");
            return false;
        }
        if (buyer0.getCredit() + credit > maxCredit) {//just print out a statement if the refund
            //maxes out the buyer's balance
            System.out.println(buyer + " max credit reached, balance is now maxed out");
            return true;
        }
        return true;
    }

    /**
     * Return true if a user refund is able to be completed, false otherwise
     *
     * @param buyer  - a buyer name
     * @param seller - a seller name
     */
    public void processRefund(String buyer, String seller, Double credit) {
        if (verifyRefund(buyer, seller, credit)) {
            User seller0 = userList.getObj(seller);//Get the user objects
            User buyer0 = userList.getObj(buyer);
            data.updateCreditLogins(seller, -credit);
            if (buyer0.getCredit() == maxCredit) {//if his balance is maxed, write the max amount to the
                //database
                data.updateCreditLogins(buyer, maxCredit - buyer0.getCredit());
            } else {//else, just add the amount to his balance in the database
                data.updateCreditLogins(buyer, credit);
            }

            ((Admin) currentUser).refund(seller0, buyer0, credit);//update the objects
            System.out.println("Refund successful");
        }
    }

    public void processRemoveGame(String userName, String gameName) {
        if (checkUserNull()) {//check user null
            System.out.println("No user logged in; cannot remove game");
            return;
        }
        if (currentUser.getType().equals(Data.ADMIN)) {//make sure the user is either an admin or the user given
            if (verifyRemove(userName, gameName)) {
                System.out.println("Admin removed " + gameName + " from " + userName);
                return;
            }
            return;
        } else if (currentUser.getName().equals(userName)) {
            if (verifyRemove(userName, gameName)) {
                System.out.println(userName + " removed " + gameName + " from himself");
                return;
            }
            return;
        }
        System.out.println("User logged in is not the one trying to remove game; cannot remove game");
    }

    /**
     * Verifies a removeGame transaction
     *
     * @param userName the name of the user
     * @param gameName the game he is trying to remove
     * @return True, iff this is a valid removeGame transaction
     */
    public boolean verifyRemove(String userName, String gameName) {
        if (userList.getObj(userName) != null) {// check to see if the user exists
            if (userList.getObj(userName).getInv().getObj(gameName) != null) {//check to see if the game exists
                User user = userList.getObj(userName);
                Game game = userList.getObj(userName).getInv().getObj(gameName);
                if (!game.isAddedToday()) {//make sure it was not added today
                    data.removeGameUserLogins(userName, gameName);
                    user.getInv().removeItem(game);
                    return true;
                }
                System.out.println("Game was added today; cannot remove");
                return false;
            }
            System.out.println("User does not have game; cannot remove");
            return false;
        }
        System.out.println("User does not exist; cannot remove");
        return false;
    }

    /**
     * Verifies the gift transaction
     *
     * @param gameName the game to be gifted
     * @param sender   the person  sending the gift
     * @param receiver the person receiving the gift
     * @return True iff this is a valid gift transaction
     */
    public boolean verifyGift(String gameName, String sender, String receiver) {
        if (checkUserNull()) {//check user null
            System.out.println("No user logged in; cannot gift");
            return false;
        }
        User senderUser = userList.getObj(sender);
        if (senderUser == null) {//makes sure user exists
            System.out.println("Sender does not exist; cannot gift");
            return false;
        }

        User receiverUser = userList.getObj(receiver);

        if (receiverUser == null) {//makes sure receiver exists
            System.out.println("Receiver does not exist; cannot gift");
            return false;
        }

        Game game = senderUser.getInv().getObj(gameName);

        if (game == null) {//makes sure sender has game
            System.out.println("Sender does not have game; cannot gift");
            return false;
        }

        if (receiverUser.getInv().getObj(gameName) != null) {//makes sure receiver doesn't
            // already have that game
            System.out.println("Receiver has game already; cannot gift");
            return false;
        }

        if (game.isAddedToday()) {//makes sure game was not bought or put up for sale today
            System.out.println("Game was bought or put up for sale today; cannot gift");
            return false;
        }
        if (currentUser.getType().equals(Data.ADMIN)) {
            return true;//make sure if its an admin, we let him send a gift
        }
        if (!currentUser.getName().equals(sender)) {//checks current user is the sender
            System.out.println("Sender is not the currently logged in user; cannot gift");
            return false;
        }
        return true;
    }

    /**
     * Process a gift transaction
     *
     * @param gameName the game to be gifted
     * @param sender   the person  sending the gift
     * @param receiver the person receiving the gift
     */
    public void processGift(String gameName, String sender, String receiver) {
        if (verifyGift(gameName, sender, receiver)) {//if we can gift
            User senderUser = userList.getObj(sender);//get the objects
            Game game = senderUser.getInv().getObj(gameName);
            if (!game.isSelling()) {//if the game is not selling, remove it
                data.removeGameUserLogins(sender, gameName);//update database
                senderUser.getInv().removeItem(game);//update the object
            }
            data.addGame(game, receiver, false);//add the game to the receiver's inventory
            System.out.println(sender + " successfully gifted " + receiver + " " + gameName);
        }
    }

    /**
     * Create a user
     *
     * @param name           name of user
     * @param type           his type
     * @param money          designated amount to be in his balance
     * @param games          ArrayList of his games
     * @param isNew          newly created user or a initial startup user
     * @param initialStartup indicates if this is the initial startup
     */
    public void createUser(String name, String type, Double money, ArrayList<String[]> games, boolean isNew,
                           boolean initialStartup) {
        //check if it is and admin creating users, or if this is the initial startup
        if ((checkUserNull() && initialStartup) || (!checkUserNull() && currentUser.getType().equals(Data.ADMIN))) {
            User newUser = null;
            if (Admin.validName(name)) {//if it is a valid name, create the user, depending on his type
                if (type.equals(Data.ADMIN)) {
                    newUser = new Admin(name.strip(), money);
                    ((Admin) newUser).userlist = this.userList;//give admin access to the usERList
                }
                if (type.equals(Data.BUYER)) {
                    newUser = new Buyer(name.strip(), money);
                }
                if (type.equals(Data.SELLER)) {
                    newUser = new Seller(name.strip(), money);
                }
                if (type.equals(Data.STANDARD)) {
                    newUser = new StandardUser(name.strip(), money);
                }
            }
            if (newUser != null) {//if the user is created
                if (userList.containsObj(newUser)) {//check if he exists, if he does, don't add add him to our system
                    System.out.println("User exists; cannot create");
                    return;
                }
                if (games != null) {//if he has games, add them to his inventory, with appropriate format
                    for (String[] game : games) {
                        Game g = null;
                        if (game.length == 4) {
                            g = new Game(game[0].strip(), Double.parseDouble(game[1]), game[3], newUser.getName());
                            g.setSelling(game[3].equals(Game.FS));
                            g.setDiscount(Double.parseDouble(game[2]));
                        }
                        if (game.length == 3) {
                            g = new Game(game[0].strip(), Double.parseDouble(game[1]), game[2], newUser.getName());
                            g.setSelling(false);
                        }
                        assert g != null;
                        g.setAddedToday(false);
                        newUser.getInv().addItem(g);
                    }
                }
                newUser.setDailyCredit(0);
                this.userList.addItem(newUser);
                if (isNew) {//if he is new, add him to our database
                    data.addLogin(newUser);
                }
                System.out.println("Created user: " + newUser.getName());
                return;
            }
        }
        if (checkUserNull()) {
            System.out.println("No user logged in, cannot create user");

        } else {
            System.out.println("Not an admin; cannot create user");
        }
    }

}
