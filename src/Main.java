import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Main m = new Main();
        user u = new user("null", "null");
        Scanner console = new Scanner(System.in);

        //Connects to the database using the path found within the mysql folder.
        //No password required in my case, only the root.
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/mydb", "root", "Trollface64!");

        //Hashtable to store usernames and passwords.
        Map<String, String> info = new HashMap<>();

        //Function that stores the username and passwords.
        m.addAccountsIntoMap(info, connection);


        m.Intro();
        int response = console.nextInt();
        if (response == 1) {
            u.createUserandAccount(console, connection, info);
            m.dbOptions(console, u, connection);
        } else if (response == 2) {
            u.AccessAccount(info, console);
            m.dbOptions(console, u, connection);
        } else if (response == 3) {
            System.out.println("Goodbye");
            System.exit(0);
        }
    }//End of main Method


    public void Intro() {
        System.out.println("Welcome to the movie database program. Here are a list of commands you can try");
        System.out.println("[1] New User? Register and create an account.\n" +
                "[2] Already registered? Login into your account.\n" +
                "[3] Exit the program.");
    }//End of Intro method


    /*
    After the user has successfully made an account or has successfully logged in then the options for the database
    are introduced.
     */
    public void dbOptionList(){
        System.out.println("\n[1] Retrieve all movies in the database.\n" +
                "[2] add a movie to the database\n" +
                "[3] Retrieve a Top 10 list.\n" +
                "[4] Add a movie into the database.\n" +
                "[5] Add a Streaming platform.\n" +
                "[6] Add a Top 10 list.\n" +
                "[7] Exit Program");
    }

    public void dbOptions(Scanner console, user u, Connection connection) throws SQLException {

        dbOptionList();
        int result = console.nextInt();

        while(result != 7) {
            if (result == 1) {
                u.retrieveMovies(connection);
                dbOptionList();
                result = console.nextInt();
            } else if (result == 2) {
                u.addMovie(connection, console);
                dbOptionList();
                result = console.nextInt();
            } else if (result == 3) {
                dbOptionList();
                result = console.nextInt();

            } else if (result == 4) {
                dbOptionList();
                result = console.nextInt();

            } else if (result == 5) {
                dbOptionList();
                result = console.nextInt();

            } else if (result == 6) {
                dbOptionList();
                result = console.nextInt();

            } else if (result == 7) {
                System.exit(0);
            }
        }
    }//End of databaseOptions


    /*
    This method adds any existing accounts found in the "accounts" table from the database into the
    Hashtable named "info."
     */
    public void addAccountsIntoMap(Map<String, String> info, Connection connection) throws SQLException {

        //Statement allows mysql code to be written within it to interact with the database
        Statement statement = connection.createStatement();

        //Allows to put all columns within the account table into sets that can be accessed individually.
        ResultSet rs1 = statement.executeQuery("SELECT * FROM account");

        while (rs1.next()) {

            //The sets defined in a ResultSet are defined by the name of the column.
            info.put(rs1.getString("username"), rs1.getString("password"));
        }
    }//end of addAccountsIntoMap
}