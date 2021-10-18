package Backend;

import java.util.Objects;

/**
 * a game class that provide all the data needed for a game
 * this class includes all actions that can be performed on game
 * by the users
 */
public class Game {
    public static final String FS = "FS", NFS = "NFS";
    private String name;
    private double price;
    private String status;
    private String owner;
    private double discountPercent;
    private boolean selling;
    private boolean addedToday;

    /**
     * A constructor for a Game that can be bought or sold on the market
     *
     * @param name   - name of the game
     * @param price  - price of the game
     * @param status - whether game is for sale or not
     * @param owner  - name of owner of the game
     */
    public Game(String name, double price, String status, String owner) {
        this.name = name;
        this.price = price;
        this.status = status;
        this.owner = owner;
    }

    /**
     * set discount rate
     * @param discountPercent - discount percentage
     */
    public void setDiscount(double discountPercent){
        this.discountPercent = discountPercent;
    }

    /**
     * returns discount percentage of the game
     * @return the percentage of the discount
     */
    public double getDiscountPercent() {
        return discountPercent;
    }

    /**
     * set a game to be either selling or not using a boolean expression
     * @param selling - boolean
     */
    public void setSelling(boolean selling){
        this.selling = selling;
    }

    /**
     * return true if a game is sellig and false otherwise
     * @return true iff a game is selling
     */
    public boolean isSelling() {
        return selling;
    }

    /**
     * return true if the game has been added today and false otherwise
     * @return true iff a game is added today
     */
    public boolean isAddedToday() {
        return addedToday;
    }

    /**
     * set a game to be added today or not
     * @param boughtToday - boolean
     */
    public void setAddedToday(boolean boughtToday) {
        this.addedToday = boughtToday;
    }

    /**
     * gets name of the game
     * @return the name of the game
     */
    public String getName() {
        return this.name;
    }

    /**
     * gets name of the User owner of the game
     * @return the name of the owner
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * gets the price of the game
     * @return the price of the game
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * gets whether the game is for sale or not
     * @return the status of the game
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * sets the name of a game
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets the price of a game
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Creates a new game and gives it to new owner
     * @param owner User who will own the game
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * checks the status of a game and switches it
     */
    public void changeStatus() {

        if (this.getStatus().equals(FS)) {
            this.status = NFS;
        } else {
            this.status = FS;
        }
    }

    /**
     * return the name of the game as a string
     * @return game name
     */
    @Override
    public String toString() {
        return this.name;
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
        Game game = (Game) o;
        return Objects.equals(name, game.name);
    }
}
