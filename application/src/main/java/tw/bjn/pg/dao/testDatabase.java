package tw.bjn.pg.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.sql.DataSource;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;

public class testDatabase {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Autowired
    private DataSource dataSource;

    private static Connection getConnection() throws URISyntaxException, SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        return DriverManager.getConnection(dbUrl);
    }

    @RequestMapping("/db")
    boolean insert(int uid, int price, String time) {
        try {
            Connection connection = dataSource.getConnection();
            Statement stmt = connection.createStatement();
//            stmt.execute("CREATE TABLE IF NOT EXISTS testTable");
            stmt.execute("INSERT INTO testTable(uid,price,time) VALUES ("+uid+","+price+","+time+") ");
            ResultSet rs = stmt.executeQuery("SELECT price FROM testTable");

            ArrayList<String> output = new ArrayList<String>();
            while (rs.next()) {
                output.add("Read from DB: " + rs.getInt("price"));
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
