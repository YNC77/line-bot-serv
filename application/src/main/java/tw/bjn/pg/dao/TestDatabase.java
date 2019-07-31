package tw.bjn.pg.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.sql.DataSource;
import java.math.BigInteger;
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

    public boolean insert(String uid, int price, long time) {
        try {
            Connection connection = dataSource.getConnection();
//            Statement stmt = getConnection().createStatement();
//            Statement stmt = connection.createStatement();
//            stmt.execute("CREATE TABLE IF NOT EXISTS testTable");
//            stmt.execute("INSERT INTO testtable(uid,price,time) VALUES (?,?,?) ");

            String myStatement = "INSERT INTO testtable(uid,price,time) VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(myStatement);
            statement.setString(1, uid);
            statement.setInt(2, price);
            statement.setLong(3, time);
            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    int queryTotalPrice(int uid) {
        try {
            Connection connection = dataSource.getConnection();
            Statement stmt = connection.createStatement();
//            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT price FROM testtable WHERE uid = "+uid);

            int totalPrice = 0;
            while (rs.next()) {
                totalPrice += rs.getInt("price");
            }

            return totalPrice;
        } catch (SQLException e) {
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
