package Backend;

import java.util.Objects;

/**
 * this the parent class for all user classes
 * this class contains all necessary methods that every user requires
 * it will be the parent class of all the user types and includes all
 * necessary methods
 */
public class User {
    private String name;
    public String type;
    private double credit;
    private final Inventory<Game> inventory = new Inventory<>();
    private double dailyCredit;

    /**
     * User Constructor
     *
     * @param name   - name of the user
     * @param credit - credit amount of the user
     */
    public User(String name, Double credit) {
        this.name = name;
        this.credit = credit;
    }

    /**
     * a buy method that will buy the specified game
     *
     * @param game - Game object
     */
    public void buy(Game game) {
        this.credit -= game.getPrice();
        Game gameBought = new Game(game.getName(), game.getPrice(), Game.NFS, this.name);
        gameBought.setSelling(false);
        gameBought.setAddedToday(true);
        this.inventory.addItem(gameBought);
    }

    /**
     * a sell method that will sell the specified game
     * @param game - game name
     * @param price - game price
     * @param discountPercent - discount percentage on the game
     */
    public void sell(String game, Double price, double discountPercent) {
        Game newGame = new Game(game, price, Game.NFS, this.name);
        newGame.setSelling(true);
        newGame.setDiscount(discountPercent);
        newGame.setAddedToday(true);
        this.inventory.addItem(newGame);
    }

    /**
     * get the name of the user
     * @return the name of the user
     */
    public String getName() {
        return this.name;
    }

    /**
     * set the name of the user
     * @param name - new name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * set the credit for the user
     * @param credit - new credit of the user
     */
    public void setCredit(Double credit) {
        this.credit = credit;
    }

    /**
     * get the type of the user
     * @return the type of the user
     */
    public String getType() {
        return this.type;
    }

    /**
     * get the credit of the user
     * @return the amount of credit for the user
     */
    public double getCredit() {
        return credit;
    }

    /**
     * get the items inside the user's inventory
     * @return the arraylist of games inside inventory
     */
    public Inventory<Game> getInv() {
        return inventory;
    }

    /**
     * get the daily credit
     * @return daily credit
     */
    public double getDailyCredit() {
        return dailyCredit;
    }

    /**
     * set new daily credit
     * @param dailyCredit - amount of daily credit
     */
    public void setDailyCredit(double dailyCredit) {
        this.dailyCredit = dailyCredit;
    }

    /**
     * check whether two objects are equal or not
     * @param o - object
     * @return true iff the object is equal to the other object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name);
    }

    /**
     * string manipulation of the name of the user
     * @return the user's name as a string
     */
    @Override
    public String toString() {
        return this.name;
    }
}
