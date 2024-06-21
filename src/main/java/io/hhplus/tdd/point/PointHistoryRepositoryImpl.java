package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

    PointHistoryTable pointHistoryTable = new PointHistoryTable();

    @Override
    public Optional<List<PointHistory>> selectHistories(long id) throws CommonException {
        List<PointHistory> pointHistory = pointHistoryTable.selectAllByUserId(id);

        return Optional.ofNullable(pointHistory);
    }

    @Override
    public void insert(long id, long amount, TransactionType type, long updateMillis)
        throws CommonException {
        pointHistoryTable.insert(id, amount, type, 0L);
    }
}
