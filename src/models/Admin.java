package models;
public class Admin extends Cashier {

    public Admin(int userId, String username, String password) {
        super(userId, username, password);
        setRole("Admin");
    }

    @Override
    public void displayMenu() {
        System.out.println("=== Admin Menu ===");
        System.out.println("1. Manage Movies");
        System.out.println("2. Manage Showtimes");
        System.out.println("3. Manage Theater Rooms");
        System.out.println("4. Manage Users");
        System.out.println("5. View Sales Reports");
        System.out.println("6. Sell Ticket (Cashier Mode)");
        System.out.println("7. Logout");
    }

    public void manageMovies() {
        System.out.println("Managing movies...");
    }

    public void manageShowtimes() {
        System.out.println("Managing showtimes...");
    }

    public void manageUsers() {
        System.out.println("Managing users...");
    }

    public void viewSalesReport() {
        System.out.println("Viewing sales report...");
    }
}