package tw.bjn.pg.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.*;

@Component
public class Database {

    @Autowired
    private DataSource dataSource;

    public boolean insert(String uid, int price, Timestamp time) {
        try {
            Connection connection = dataSource.getConnection();

            String myStatement = "INSERT INTO testtable(uid,price,time) VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(myStatement);
            statement.setString(1, uid);
            statement.setInt(2, price);
            statement.setTimestamp(3, time);
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
}
