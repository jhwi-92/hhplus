package io.hhplus.tdd.point;

import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PointService {

    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public PointService(UserPointRepository userPointRepository,
        PointHistoryRepository pointHistoryRepository) {
        this.userPointRepository = userPointRepository;
        this.pointHistoryRepository = pointHistoryRepository;
    }

    // userId로 포인트를 조회한다.
    public UserPoint selectPoint(long id) {
        return userPointRepository.selectById(id)
            .orElseThrow(() -> new CommonException("id를 찾을 수 없습니다. id: " + id));
    }

    // userId로 포인트 내역을 조회한다.
    public List<PointHistory> selectPointHistory(long id) {
        return pointHistoryRepository.selectHistories(id)
            .orElseThrow(() -> new CommonException("id를 찾을 수 없습니다. id: " + id));
    }

    //유저 포인트를 충전한다.
    public UserPoint charge(long id, long amount) {

        validateChargeRequest(amount);

        UserPoint userPoint = userPointRepository.selectById(id)
            .orElseThrow(() -> new CommonException("id를 찾을 수 없습니다. id: " + id));
        ;

        long updateAmount = userPoint.point() + amount;

        userPointRepository.charge(id, updateAmount);

        pointHistoryRepository.insert(id, amount, TransactionType.CHARGE, 0L);

        return new UserPoint(id, updateAmount, 0L);

    }

    //유저 포인트를 사용한다.
    public UserPoint use(long id, long amount) {

        validateChargeRequest(amount);

        UserPoint userPoint = userPointRepository.selectById(id)
            .orElseThrow(() -> new CommonException("id를 찾을 수 없습니다. id: " + id));
        ;

        long updateAmount = userPoint.point() - amount;

        validateUseRequest(updateAmount);

        userPointRepository.use(id, updateAmount);

        pointHistoryRepository.insert(id, amount, TransactionType.USE, 0L);

        return new UserPoint(id, updateAmount, 0L);
    }

    private void validateChargeRequest(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
    }

    private void validateUseRequest(long updateAmount) {
        if (updateAmount <= 0) {
            throw new IllegalArgumentException("사용 금액은 보유금보다 적어야 합니다.");
        }
    }
}
