
import java.util.Scanner;

public class App {
    public static void mainMenu(){
        Scanner inp = new Scanner(System.in);
        int ch;
        System.out.printf("""
                TICKET BOOKING SYSTEM

                1. Login
                2. SignUp
                99. Exit

                Enter ur choice: 
                """);
        ch = inp.nextInt();
        switch(ch){
            case 1:
                System.out.println("Login\n");
                System.out.print("Enter your email: ");
                String email = inp.nextLine();
                System.out.print("Enter your password: ");
                String passwd = inp.nextLine();
                User u = User.login(email, passwd);
                u.DashBoard();
                break;
            case 2:
                System.out.print("Enter your name: ");
                String name = inp.nextLine();
                System.out.print("Enter your email: ");
                email = inp.nextLine();
                System.out.print("Enter your password: ");
                passwd = inp.nextLine();
                User nu = User.signup(name, email, passwd);
                nu.DashBoard();
                break;
            case 99:
                System.out.println("Logging Out");
                mainMenu();
                ch = inp.nextInt();
                break;
        }
    }
    public static void main(String[] args) throws Exception {
        mainMenu();
    }
}