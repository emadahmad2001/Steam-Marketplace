package Backend;

/**
 * a buyer class that inherits the user class
 * this class will provide all necessary actions for a buyer
 */
public class Buyer extends User {
    /**
     * A constructor for the Buyer user that inherits from the User class
     *
     * @param name  - name of the Buyer
     * @param money - Buyer's credit
     */
    public Buyer(String name, double money) {
        super(name, money);
        this.type = Data.BUYER;
    }

    /**
     * print an appropriate message stating that a buyer can't sell
     *
     * @param game            - game name
     * @param price           - price of the game
     * @param discountPercent - discount percentage
     */
    @Override
    public void sell(String game, Double price, double discountPercent) {
        System.out.println("Buyer cannot sell, contact admin");
    }
}