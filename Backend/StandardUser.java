package Backend;

/**
 * a standard seller class that inherits the user class
 * this class will provide all necessary actions for a standard seller
 */
public class StandardUser extends User {

    /**
     * A constructor for the Standard user that inherits from the User class
     *
     * @param name  the name of the Seller
     * @param money the amount of money the Seller has
     */
    public StandardUser(String name, double money) {
        super(name, money);
        this.type = Data.STANDARD;
    }
}
