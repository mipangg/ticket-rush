package io.mipangg.ticketrush.domain.seat.controller;

import static io.mipangg.ticketrush.util.TestUtil.genSeatSummaryResponses;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mipangg.ticketrush.domain.seat.dto.SeatCreateRequest;
import io.mipangg.ticketrush.domain.seat.dto.SeatCreateResponse;
import io.mipangg.ticketrush.domain.seat.dto.SeatDetailResponse;
import io.mipangg.ticketrush.domain.seat.dto.SeatSummaryResponse;
import io.mipangg.ticketrush.domain.seat.service.SeatService;
import io.mipangg.ticketrush.global.common.CursorResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SeatController.class)
class SeatControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SeatService seatService;

    @Test
    @DisplayName("좌석 생성 성공 테스트")
    void createSeatSuccessTest() throws Exception {

        Long concertId = 1L;
        SeatCreateRequest req = new SeatCreateRequest("A-01", 1L);

        SeatCreateResponse resp = new SeatCreateResponse(
                101L,
                1L,
                "A-01",
                "VIP",
                LocalDateTime.of(2026, 9, 21, 16, 0),
                LocalDateTime.of(2026, 9, 21, 16, 0)
        );

        when(seatService.saveSeat(anyLong(), any(SeatCreateRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/concerts/{concertId}/seats", concertId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.concertId").value(1))
                .andExpect(jsonPath("$.number").value("A-01"))
                .andExpect(jsonPath("$.seatGrade").value("VIP"))
                .andDo(print());

        verify(seatService).saveSeat(anyLong(), any(SeatCreateRequest.class));

    }


    @Test
    @DisplayName("좌석 개별 조회 성공 테스트")
    void getSeatDetailSuccessTest() throws Exception {

        Long concertId = 1L;
        Long seatId = 1L;

        SeatDetailResponse resp = new SeatDetailResponse(
                101L,
                1L,
                "A-01",
                "VIP",
                BigDecimal.valueOf(150000),
                "AVAILABLE"
        );

        when(seatService.getSeatDetail(concertId, seatId)).thenReturn(resp);

        mockMvc.perform(get("/api/concerts/{concertId}/seats/{seatId}", concertId, seatId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.concertId").value(1))
                .andExpect(jsonPath("$.number").value("A-01"))
                .andExpect(jsonPath("$.seatGrade").value("VIP"))
                .andExpect(jsonPath("$.price").value(150000))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andDo(print());

        verify(seatService).getSeatDetail(concertId, seatId);

    }

    @Test
    @DisplayName("좌석 목록 조회 성공 테스트")
    void getSeatsSuccessTest() throws Exception {

        Long concertId = 1L;

        List<SeatSummaryResponse> content = genSeatSummaryResponses();
        CursorResponse<SeatSummaryResponse> resp = new CursorResponse<>(content, null, false);

        // 기본값: cursor 없음, size = 10
        when(seatService.getSeats(concertId, null, 10)).thenReturn(resp);

        mockMvc.perform(get("/api/concerts/{concertId}/seats", concertId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].number").value("A-01"))
                .andExpect(jsonPath("$.content[1].number").value("A-02"))
                .andExpect(jsonPath("$.content[2].number").value("A-03"))
                .andExpect(jsonPath("$.content[0].seatGrade").value("VIP"))
                .andExpect(jsonPath("$.content[1].seatGrade").value("VIP"))
                .andExpect(jsonPath("$.content[2].seatGrade").value("R"))
                .andExpect(jsonPath("$.content[0].price").value(150000))
                .andExpect(jsonPath("$.content[1].price").value(150000))
                .andExpect(jsonPath("$.content[2].price").value(100000))
                .andExpect(jsonPath("$.content[0].status").value("AVAILABLE"))
                .andExpect(jsonPath("$.content[1].status").value("HELD"))
                .andExpect(jsonPath("$.content[2].status").value("SOLD"))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.nextCursor").doesNotExist())
                .andDo(print());

        verify(seatService).getSeats(concertId, null, 10);

    }

}