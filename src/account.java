import java.security.SecureRandom;
import java.util.*;

public class account {
    SecureRandom r = new SecureRandom();

    private int accountID = r.nextInt(700-500+1) + 500;
    private int userID;
    private String username;
    private String password;

    public account(String username, String password, int userID){
        username = this.username;
        password = this.password;
        userID = this.userID;
    }


    public int getAccountID(){

        return accountID;
    }
}
