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
        System.out.println("2. Manage Users");
        System.out.println("3. View Sales Reports");
        System.out.println("4. Logout");
    }

    //Bug checkpoint: Admin-specific methods
    public void manageMovies() {
        System.out.println("Managing movies...");
    }

    public void manageUsers() {
        System.out.println("Managing users...");
    }

    public void viewSalesReport() {
        System.out.println("Viewing sales report...");
    }
}