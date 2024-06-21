package io.hhplus.tdd.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@DisplayName("PointController 테스트")
@WebMvcTest(PointController.class)
@ExtendWith(MockitoExtension.class)
public class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointService pointService;

    @Test
    @DisplayName("get - 포인트 조회")
    void point() throws Exception {

        //given
        long id = 1L;
        //when
        UserPoint userPoint = new UserPoint(id, 0, System.currentTimeMillis());
        when(pointService.selectPoint(id)).thenReturn(userPoint);

        //then
        mockMvc.perform(get("/point/{id}", id)).andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id)).andExpect(jsonPath("$.point").value(0L))
            .andExpect(jsonPath("$.updateMillis").value(userPoint.updateMillis()));
        verify(pointService).selectPoint(id);
    }

    @Test
    @DisplayName("get - history 조회")
    void history() throws Exception {

        //given
        long id = 1L;

        //when
        mockMvc.perform(get("/point/{id}/histories", id))

            //then
            .andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("patch - 포인트 충전")
    void charge() throws Exception {

        //given
        long id = 1L;
        long amount = 10L;

        //when
        mockMvc.perform(patch("/point/{id}/charge", id).contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(amount)))

            //then
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.point").value(amount));
    }

    @Test
    @DisplayName("patch - 포인트 사용")
    void use() throws Exception {

        //given
        long id = 1L;
        long amount = 10L;

        //when
        mockMvc.perform(patch("/point/{id}/use", id).contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(amount)))

            //then
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.point").value(amount));
    }
}
