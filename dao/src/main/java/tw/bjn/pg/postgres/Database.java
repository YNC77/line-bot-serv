package tw.bjn.pg.postgres;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.postgres.model.ExpenseRecord;
import tw.bjn.pg.postgres.repositories.TestTable;

import java.sql.*;
import java.util.Optional;

@Slf4j
@Component
public class Database {

    private TestTable repository;

    @Autowired
    public Database(TestTable repository) {
        this.repository = repository;
    }

    public boolean insert(String uid, int price, Timestamp time) {
        try {
            ExpenseRecord record = new ExpenseRecord();
            record.setUid(uid);
            record.setPrice(price);
            record.setTime(time);
            repository.save(record);
            return true;
        } catch (RuntimeException ex) {
            log.error("jpa error.", ex);
            return false;
        }
    }

    int queryTotalPrice(String uid) {
        Optional<ExpenseRecord> optionalRecord = repository.findById(uid);
        if (optionalRecord.isPresent())
            return optionalRecord.get().getPrice();

        log.warn("No query result for uid '{}'", uid);
        return 0;
    }
}
