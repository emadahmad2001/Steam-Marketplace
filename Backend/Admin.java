package Backend;

/**
 * An Admin class that inherits user class
 * this class provides all the controls that an admin can do to the system.
 */
public class Admin extends User {
    public Inventory<User> userlist;

    /**
     * admin constructor that inherits User class
     *
     * @param name  - name of the admin
     * @param money - amount of money
     */
    public Admin(String name, double money) {
        super(name, money);
        type = Data.ADMIN;
    }

    /**
     * update the credit of a given user
     *
     * @param amount - amount of money to added
     * @param person - user object of the user
     */
    public void updateCredit(double amount, User person) {
        person.setCredit(person.getCredit() + amount);
    }

    /**
     * Validate a name
     *
     * @param name - Name to be validated
     * @return True if valid, False otherwise
     */
    public static boolean validName(String name) {
        return name.length() <= 16;
    }

    /**
     * check whether a user exists in the user list or not
     *
     * @param name - name of the user
     * @return true if the given user exist and false otherwise
     */
    public boolean userExists(String name) {
        for (User user : userlist.getItems()) {
            if (user.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Delete a User given a name
     *
     * @param name name of wanted User
     */
    public void deleteUser(String name) {
        User person = userlist.getObj(name);
        userlist.removeItem(person);
    }

    /**
     * refund a buyer from a seller
     *
     * @param seller - seller object
     * @param buyer  - buyer object
     * @param credit - amount of money
     */
    public void refund(User seller, User buyer, Double credit) {
        seller.setCredit(seller.getCredit() - credit);
        buyer.setCredit(Math.min(buyer.getCredit() + credit, 999999.99));
    }

    /**
     * provide discounts to games that exist in user's inventory iff the auction sale is on
     *
     * @param buttonOn - boolean entry
     */
    public void setAuctionPrices(boolean buttonOn) {
        for (User user : userlist.getItems()) {
            for (Game game : user.getInv().getItems()) {
                if (game.isSelling() && game.getDiscountPercent() != 0) {
                    String price;
                    if (buttonOn) {
                        price = String.valueOf(game.getPrice() - (game.getPrice() * game.getDiscountPercent() / 100));
                    } else {
                        price = String.valueOf((game.getPrice() / (100 - game.getDiscountPercent())) * 100);
                    }
                    game.setPrice(Double.parseDouble(price));
                }
            }
        }
    }
}
