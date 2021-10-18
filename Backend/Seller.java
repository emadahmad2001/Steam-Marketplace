package Backend;

/**
 * a seller class that inherits the user
 * this class will provide all necessary action for a seller
 */
public class Seller extends User {

    /**
     * A constructor for the Seller user that inherits from the User class
     *
     * @param name  - name of the Buyer
     * @param money - Buyer's credit
     */
    public Seller(String name, double money) {
        super(name, money);
        this.type = Data.SELLER;
    }

    /**
     * print a message stating that a seller can't buy
     *
     * @param game - game object
     */
    @Override
    public void buy(Game game) {
        System.out.println("Seller cannot buy, contact admin");
    }
}