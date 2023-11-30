import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import java.sql.*;
import java.util.Scanner;
import java.util.Set;
public class user {
    SecureRandom r = new SecureRandom();
    private String fName;
    private String lName;

    private int accountID;
    private int userID = r.nextInt(999-800+1) + 800;



    public user(String fName, String lName) {
        fName = this.fName;
        lName = this.lName;
    }


    /*
    Just as the name of the method reads, it allows a user to register into the database and allows a user to
    create an account. The persons name and account username along with passwords are saved into the database. It also
    stores the account information into the hashtable "info"
     */
    public Integer createUserandAccount(Scanner console, Connection connection, Map<String,String> info) throws SQLException {

        //Our empty statement is created
        Statement statement = connection.createStatement();
        System.out.println("Please enter your first name");
        String fName = console.next();
        System.out.println("Please enter your last name");
        String lName = console.next();
        System.out.println("Great! you have been registered into the database \n");

        //Creates a new user object which replaces the user object created in the main method. This one is stored into the db.
        user u = new user(fName, lName);

        //Uses the statement created earlier to register the user into the database
        statement.executeUpdate("INSERT INTO user(user_id, first_name, last_name) VALUES ('" + u.getUserID() + "','" + fName + "','" + lName + "')");


        account a = createAccountandStore(console, connection,u.getUserID(), info);

        //Simple greeting once a user and account is registered.
        System.out.println("Welcome " + fName + "! Here are your options for the database...");

        return a.getAccountID();

    }//End of createUserandAccount


    /*
       This method will be used within the createUserandAccount method. Creates a new account object that is also stored
       within the database.
     */
    public account createAccountandStore(Scanner console, Connection connection, int userID, Map<String,String> info) throws SQLException {

        Statement statement = connection.createStatement();

        System.out.println("Lets create your account now");
        System.out.println("Please create a username");
        String username = console.next().toLowerCase();
        System.out.println("Please create a password");
        String password = console.next();

        //Hash the password before account insertion
        try {
            password = hash(password);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        //Creates a new account with the given username and password.
        account a = new account(password, username, userID);

        //Stores the account information within the database.
        statement.executeUpdate("INSERT INTO account(account_id, password, username, user_id) VALUES ('" + a.getAccountID() + "','" + password + "','" + username + "','" + userID+ "')");

        //Turns the username to lowercase. Only the password is case-sensitive.
        info.put(username.toLowerCase(), password);

        return a;

    }//End of createAccount and Store


    /*
    If a user already has an account then this method is called in order for the user to login into the database.
    In this program only the password is case-sensitive. The characters within the username are all turned to lowercase
    to match the username stored in the hashtable.
     */
    public Integer AccessAccount(Connection connection, Map<String,String> info, Scanner console) throws SQLException {

        Statement statement = connection.createStatement();

        System.out.println("Please type in your username");
        String username = console.next().toLowerCase();
        System.out.println("Please enter your password");
        String password = console.next();

        try {
            password = hash(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //Creates a set from the values (username) of the hashtable "info".
        Set<String> usernames = info.keySet();

        //Tells user to re-enter credentials if the username provided is not found within the set.
        while (!usernames.contains(username)) {
            System.out.println("Incorrect username or password. Please try again.");
            System.out.println("Please type in your username");
            username = console.next();
            System.out.println("Please enter your password");
            password = console.next();

            try {
                password = hash(password);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        //Once username is successfully found, the password provided is compared to the password associated with the username.
        //If the password doesn't match then credentials must be entered all over again.
        while (!password.equals(info.get(username))) {
            System.out.println("Incorrect username or password. Please try again.");
            System.out.println("Please type in your username");
            username = console.next();
            System.out.println("Please enter your password");
            password = console.next();

            try {
                password = hash(password);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        //Once everything is successful a greeting is given
        System.out.println("Welcome " + username + "! Here are your options for the database...");
        ResultSet rs = statement.executeQuery("SELECT a.account_id FROM account a WHERE a.username = '" + username + "';");

        int account_ID = 0;
        while (rs.next()) {
            account_ID = (int) rs.getObject("a.account_id");
        }

        return account_ID;
    }//End of accessAccount


    /*
    Retrieves all the movies found within the database using the Statement and ResultSet method provided by the jdbs
    package.
     */
    public void retrieveMovies(Connection connection) throws SQLException {

        //Blank statement is created
        Statement statement = connection.createStatement();

        //Statement retrieves all information from movie tables and divides each column into sets.
        ResultSet rs = statement.executeQuery("SELECT * FROM movie");
        while(rs.next()){

            //Each column is retrieved and printed out into the console.
            System.out.println(rs.getObject("movie_id") + " "
                    + rs.getObject("movie_name") + " "
                    + rs.getObject("genre") + " "
                    + rs.getObject("production_date") + " "
                    + rs.getObject("imdb") + " "
                    + rs.getObject("age_rating") + " "
                    + rs.getObject("Trailer"));
        }
    }

    /*
    Allows a user to insert a new movie entry into the database.
     */
    public void addMovie(Connection connection) throws SQLException {
        Scanner console = new Scanner(System.in);

        //Blank Statement created.
        Statement statement = connection.createStatement();

        //The movie_id is from the database is randomly generated.
        int movieID = r.nextInt(200 - 100 + 1) + 100;
        System.out.println("Enter the movies name:");
        String movieName = console.nextLine();
        System.out.println("Enter the movies genre:");
        String genre = console.nextLine();
        System.out.println("Enter the movies date in the format YYYY-MM-DD:");
        String movieDate = console.nextLine();
        System.out.println("Enter the movies imdb rating:");
        String movieIMDB = console.nextLine();
        System.out.println("Enter the movies age rating:");
        String ageRating = console.nextLine();
        System.out.println("Enter the movies trailer URL: ");
        String trailer = console.nextLine();

        //Statement stores all the attributes provided by the user into the database.
        statement.executeUpdate("INSERT INTO movie(movie_id, movie_name, genre, production_date, imdb, age_rating, trailer) " +
                "VALUES('" + movieID + "','" + movieName + "','" + genre + "','"+ movieDate + "','" + movieIMDB + "','" + ageRating + "','" + trailer + "')");
    }//End of addMovie

    public void retrieveLists(Connection connection) throws SQLException{

        //Blank statement is created
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT p.platform_name, t.list_id, list_date FROM streaming_platform p JOIN top_10_list t ON p.platform_id = t.platform_id;");

        while(rs.next()){
            System.out.println(rs.getObject("p.platform_name") + " "
                    + rs.getObject("t.list_id"));
        }
    }//End of retrieveLists

    public void retrieveMoviesFromList(Connection connection, Scanner console) throws SQLException {
        //Blank statement is created
        Statement statement = connection.createStatement();

        System.out.println("What Streaming platform would you like to see? Type the ID number of the list. \n");
        retrieveLists(connection);
        String list_id = console.next();
        ResultSet rs = statement.executeQuery("SELECT m.movie_name FROM movie m JOIN appears a ON m.movie_id = a.movie_id WHERE a.list_id = " + list_id + ";");
        while(rs.next()){
            System.out.println(rs.getObject("m.movie_name"));
        }
    }//End of retrieveMovieFromList

    //Retrieves the randomly generated user_ID so that it can be stored into the database.
    public int getUserID(){
        return userID;
    }

    public String hash(String password) throws NoSuchAlgorithmException {

        MessageDigest encoder = MessageDigest.getInstance("SHA-256");//gets the digest from security class
        byte[] encodedhash = encoder.digest(password.getBytes(StandardCharsets.UTF_8));//Digests the bytes and puts them in array

        String hashPass = new String(encodedhash, StandardCharsets.UTF_8);//creates a new string by condensing array of bytes

        return hashPass;
    }//End of hash

    public void createRating(Connection connection, Scanner console, int account_ID) throws SQLException {

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM movie m");

        while(rs.next()){
            System.out.println(rs.getObject("m.movie_id") + " " + rs.getObject("m.movie_name"));
        }

            System.out.println("\nEnter the movie ID for the movie you want to rate: ");
            int movie_id = console.nextInt();
            System.out.println("Rate from 0 to 5.0");
            double account_rating = console.nextDouble();
            int rating_id = r.nextInt(400-300+1) + 300;

            statement.executeUpdate("INSERT INTO rating(rating_id, account_rating, account_id, movie_id) " +
                    "VALUES(" + rating_id + "," + account_rating + "," + account_ID + "," + movie_id + ")");

    }//End of createRating

    public void checkPersonalRatings(Connection connection, int account_ID) throws SQLException{

        Statement statement = connection.createStatement();

        System.out.println("Here are you ratings from your account: \n");

        int nameColumnWidth = 20;

        ResultSet rs1 = statement.executeQuery("SELECT m.movie_name, r.account_rating FROM rating r JOIN movie m WHERE r.account_id = '" + account_ID + "' AND m.movie_ID = r.movie_id;");
        while(rs1.next()){
            System.out.printf("%-" + nameColumnWidth + "s%.1f%n", rs1.getObject("m.movie_name"), rs1.getObject("r.account_rating"));
        }

    }//End of checkPersonalRatings

}
