package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserPointRepositoryImpl implements UserPointRepository {

    UserPointTable userPointTable = new UserPointTable();

    @Override
    public Optional<UserPoint> selectById(long id) throws CommonException {
        UserPoint userPoint = userPointTable.selectById(id);
        return Optional.ofNullable(userPoint);

    }

    @Override
    public UserPoint charge(long id, long amount) throws CommonException {
        return userPointTable.insertOrUpdate(id, amount);
    }

    @Override
    public UserPoint use(long id, long amount) throws CommonException {
        return userPointTable.insertOrUpdate(id, amount);
    }


}
