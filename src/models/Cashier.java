package models;
public class Cashier extends User {

    public Cashier(int userId, String username, String password) {
        super(userId, username, password, "Cashier");
    }

    @Override
    public void displayMenu() {
        System.out.println("=== Cashier Menu ===");
        System.out.println("1. Search Movies & Showtimes");
        System.out.println("2. Select Seats");
        System.out.println("3. Sell Ticket");
        System.out.println("4. Process Payment");
        System.out.println("5. Generate Receipt");
        System.out.println("6. Logout");
    }

    public void searchMovies(String title) {
        System.out.println("Searching for: " + title);
    }

    public void sellTicket() {
        System.out.println("Processing ticket sale...");
    }

    public void processPayment(double amount) {
        System.out.println("Processing payment of PHP " + amount);
    }

    public void generateReceipt() {
        System.out.println("Generating receipt...");
    }
}
