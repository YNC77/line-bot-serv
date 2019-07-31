package tw.bjn.pg.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.sql.DataSource;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;

@Component
public class TestDatabase {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Autowired
    private DataSource dataSource;

    private static Connection getConnection() throws URISyntaxException, SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        return DriverManager.getConnection(dbUrl);
    }

    @RequestMapping("/db")
    public boolean insert(int uid, int price, long time) {
        try {
//            Connection connection = dataSource.getConnection();
            Statement stmt = getConnection().createStatement();
//            Statement stmt = connection.createStatement();
//            stmt.execute("CREATE TABLE IF NOT EXISTS testTable");
            stmt.execute("INSERT INTO testTable(uid,price,time) VALUES ("+uid+","+price+","+time+") ");

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

    int queryTotalPrice(int uid) {
        try {
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT price FROM testTable WHERE uid = "+uid);

            int totalPrice = 0;
            while (rs.next()) {
                totalPrice += rs.getInt("price");
            }

            return totalPrice;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return 0;
        }
    }

//    @Bean
//    public DataSource dataSource() throws SQLException {
//        if (dbUrl == null || dbUrl.isEmpty()) {
//            return new HikariDataSource();
//        } else {
//            HikariConfig config = new HikariConfig();
//            config.setJdbcUrl(dbUrl);
//            return new HikariDataSource(config);
//        }
}
