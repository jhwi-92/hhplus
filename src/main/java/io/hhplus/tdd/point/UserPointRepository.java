package io.hhplus.tdd.point;

import java.util.Optional;

public interface UserPointRepository {

    Optional<UserPoint> selectById(long id) throws CommonException;

    UserPoint charge(long id, long amount) throws CommonException;

    UserPoint use(long id, long amount) throws CommonException;


}
