package io.hhplus.tdd.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @Mock // UserPointRepository Mock 객체 생성
    private UserPointRepository userPointRepository;

    @Mock // PointHistoryRepository Mock 객체 생성
    private PointHistoryRepository pointHistoryRepository;

    @InjectMocks // PointService Mock 객체 생성 및 주입
    private PointService pointService;

    @Test
    @DisplayName("유저포인트 조회")
    void user_point_search() {
        // given
        long id = 1L;
        UserPoint userPoint = new UserPoint(1L, 1000L, 0);
        when(userPointRepository.selectById(id)).thenReturn(Optional.of(userPoint));

        // when
        UserPoint whenUserPoint = pointService.selectPoint(id);

        // then
        assertThat(whenUserPoint).isEqualTo(userPoint);
        verify(userPointRepository).selectById(id);
    }

    @Test
    @DisplayName("없는_유저_조회시_null반환")
    void user_search_returns_null() {
        // given
        long id = 1L;
        when(userPointRepository.selectById(id)).thenReturn(Optional.empty());

        // when / then
        assertThrows(CommonException.class, () -> pointService.selectPoint(id));
        verify(userPointRepository, times(1)).selectById(id);
    }

    @Test
    @DisplayName("데이터베이스 오류 발생 시 예외 발생")
    void database_error_exception() {
        // given
        long id = 1L;
        when(userPointRepository.selectById(id)).thenThrow(new RuntimeException("Database error"));

        // when & then
        assertThrows(RuntimeException.class, () -> pointService.selectPoint(id));
        verify(userPointRepository, times(1)).selectById(id);
    }

    @Test
    @DisplayName("유저의 포인트 충전 및 사용내역을 조회")
    void point_charge_and_history_search() {
        //given
        long userId = 1L;
        List<PointHistory> pointHistories = Arrays.asList(
            new PointHistory(1L, userId, 1000L, TransactionType.CHARGE, 1671234567890L),
            new PointHistory(2L, userId, -500L, TransactionType.USE, 1671234567891L)
        );

        //when
        when(pointHistoryRepository.selectHistories(userId)).thenReturn(
            Optional.of(pointHistories));
        List<PointHistory> whenPointHistories = pointService.selectPointHistory(userId);

        //then
        verify(pointHistoryRepository).selectHistories(userId);
        assertEquals(2, whenPointHistories.size());
        assertEquals(1000L, whenPointHistories.get(0).amount());
        assertEquals(TransactionType.CHARGE, whenPointHistories.get(0).type());
        assertEquals(TransactionType.USE, whenPointHistories.get(1).type());
    }

    @Test
    @DisplayName("포인트를 음수로 충전하는 경우")
    void charge_points_with_negative_throws_exception() {

        // given
        long userId = 1L;
        long chargeAmount = -1000L; // 음수 금액

        // when & then
        assertThrows(IllegalArgumentException.class,
            () -> pointService.charge(userId, chargeAmount));

    }

    @Test
    @DisplayName("포인트를 사용")
    void use_points_throws_exception() throws Exception {

        // given
        long userId = 1L;
        long initialBalance = 1000L; // 초기 잔액
        long useAmount = initialBalance + 1L; // 사용 금액 (보유금보다 크거나 같음)

        // Mock 객체 생성
        UserPointRepository userPointRepositoryMock = Mockito.mock(UserPointRepository.class);
        Mockito.when(userPointRepositoryMock.selectById(userId))
            .thenReturn(Optional.of(new UserPoint(userId, initialBalance, 0)));

        // PointService 객체 생성 (Mock 객체 사용)
        PointService pointService = new PointService(userPointRepositoryMock,
            pointHistoryRepository);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> pointService.use(userId, useAmount));
    }


}