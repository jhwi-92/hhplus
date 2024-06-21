package io.hhplus.tdd.point;

import java.util.List;
import java.util.Optional;

public interface PointHistoryRepository {

    Optional<List<PointHistory>> selectHistories(long id) throws CommonException;

    void insert(long id, long amount, TransactionType type, long updateMillis)
        throws CommonException;

}
