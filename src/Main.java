import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=====================================");
            System.out.println("        CAB BOOKING SYSTEM");
            System.out.println("=====================================");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter name: ");
                String name = sc.nextLine();
                System.out.print("Enter email: ");
                String email = sc.nextLine();
                System.out.print("Enter password: ");
                String password = sc.nextLine();
                System.out.print("Enter phone: ");
                String phone = sc.nextLine();
                UserService.register(name, email, password, phone);

            } else if (choice == 2) {
                System.out.print("Enter email: ");
                String email = sc.nextLine();
                System.out.print("Enter password: ");
                String password = sc.nextLine();
                int userId = UserService.login(email, password);

                if (userId == -1) {
                    System.out.println("Invalid email or password!");
                } else {
                    System.out.println("Login successful! Welcome, " + UserService.getName(userId));
                    userMenu(userId, sc);
                }

            } else if (choice == 3) {
                System.out.println("Thank you for using Cab Booking System!");
                break;
            } else {
                System.out.println("Invalid choice! Try again.");
            }
        }
    }

    // Menu shown after login (we will add more options in the next steps)
    static void userMenu(int userId, Scanner sc) {
        while (true) {
            System.out.println("\n------ USER MENU ------");
            System.out.println("1. Logout");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.println("Logged out successfully.");
                break;
            }
        }
    }
}
