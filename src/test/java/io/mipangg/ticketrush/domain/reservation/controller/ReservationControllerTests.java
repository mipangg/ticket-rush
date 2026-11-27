package io.mipangg.ticketrush.domain.reservation.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mipangg.ticketrush.domain.reservation.dto.ReservationCreateRequest;
import io.mipangg.ticketrush.domain.reservation.dto.ReservationCreateResponse;
import io.mipangg.ticketrush.domain.reservation.dto.ReservationSeatSummaryResponse;
import io.mipangg.ticketrush.domain.reservation.entity.ReservationStatus;
import io.mipangg.ticketrush.domain.reservation.service.ReservationService;
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

@WebMvcTest(ReservationController.class)
class ReservationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    @DisplayName("예약 생성 성공 테스트")
    void createReservationSuccessTest() throws Exception {

        ReservationCreateRequest req = new ReservationCreateRequest(1L, 1L,List.of(101L, 102L));

        ReservationCreateResponse resp = new ReservationCreateResponse(
                1001L,
                1L,
                1L,
                ReservationStatus.PENDING,
                BigDecimal.valueOf(300000),
                LocalDateTime.of(2026, 9, 21, 16, 35),
                LocalDateTime.of(2026, 9, 21, 16, 25),
                List.of(
                        new ReservationSeatSummaryResponse(
                                101L, "A-01", "VIP", BigDecimal.valueOf(150000)
                        ),
                        new ReservationSeatSummaryResponse(
                                102L, "A-02", "VIP", BigDecimal.valueOf(150000)
                        )
                )
        );

        when(reservationService.saveReservation(req)).thenReturn(resp);

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1001L))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.totalPrice").value(300000))
                .andDo(print());

        verify(reservationService).saveReservation(req);
    }

}