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


        m.intro();
        m.introOptions(console, u, connection, info, m);


    }//End of main Method


    public void intro() {
        System.out.println("Welcome to the movie database program. Here are a list of commands you can try");
        System.out.println("[1] New User? Register and create an account.\n" +
                "[2] Already registered? Login into your account.\n" +
                "[3] Exit the program.");
    }//End of Intro method

    public void introOptions(Scanner console, user u, Connection connection, Map<String, String> info, Main m) throws SQLException {
        int response = console.nextInt();

        if (response == 1) {
            int account_ID = u.createUserandAccount(console, connection, info);
            m.dbOptions(console, u, connection, account_ID, info, m);
        } else if (response == 2) {
            int account_ID = u.AccessAccount(connection, info, console);
            m.dbOptions(console, u, connection, account_ID, info, m);
        } else if (response == 3) {
            System.out.println("Goodbye");
            System.exit(0);
        }
    }

    public void dbIntro() {
        System.out.println("\n[1] Retrieve all movies in the database.\n" +
                "[2] Add a movie into the database.\n" +
                "[3] Add a platform.\n" +
                "[4] Retrieve all movies in a Top 10 list.\n" +
                "[5] View your movie ratings.\n" +
                "[6] Rate a movie.\n" +
                "[7] Main Menu");
    }

    /*
    After the user has successfully made an account or has successfully logged in then the options for the database
    are introduced.
     */
    public void dbOptions(Scanner console, user u, Connection connection, int account_ID, Map<String, String> info, Main m) throws SQLException {

        dbIntro();
        int result = console.nextInt();

        while (!(result  >= 7)) {
            switch (result) {
                case 1 -> u.retrieveMovies(connection);
                case 2 -> u.addMovie(connection);
                case 3 -> u.addPlatform(connection, console);
                case 4 -> u.retrieveMoviesFromList(connection, console);
                case 5 -> u.checkPersonalRatings(connection, account_ID);
                case 6 -> u.createRating(connection, console, account_ID);
            }
            dbIntro();
            result = console.nextInt();
        }

        System.out.println("[1] New User? Register and create an account.\n" +
                "[2] Already registered? Login into your account.\n" +
                "[3] Exit the program.");
        introOptions(console, u, connection, info, m);

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