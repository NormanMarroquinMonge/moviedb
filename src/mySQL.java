import java.sql.*;
public class mySQL {

    private Connection connnection = DriverManager.getConnection("jdbc:mysql://localhost:3307/mydb", "root", "");;

    public mySQL() throws SQLException {
    }

}
