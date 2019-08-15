package tw.bjn.pg.postgres.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "testtable")
public class ExpenseRecord {

    private Integer id;
    private String uid;
    private Integer price;
    private Timestamp time;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    @Column(nullable = false)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Column(nullable = false)
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Basic
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return String.format("%s - $%d", time.toString(), price);
    }
}
