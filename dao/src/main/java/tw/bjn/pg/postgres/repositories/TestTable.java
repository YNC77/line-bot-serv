package tw.bjn.pg.postgres.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.bjn.pg.postgres.model.ExpenseRecord;

@Repository
public interface TestTable extends JpaRepository<ExpenseRecord, String> {
}
