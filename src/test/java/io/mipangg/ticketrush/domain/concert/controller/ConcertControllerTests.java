package io.mipangg.ticketrush.domain.concert.controller;

import static io.mipangg.ticketrush.util.TestUtil.genConcertCreateRequest;
import static io.mipangg.ticketrush.util.TestUtil.genConcertSummaryResponses;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateRequest;
import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateResponse;
import io.mipangg.ticketrush.domain.concert.dto.ConcertDetailResponse;
import io.mipangg.ticketrush.domain.concert.dto.ConcertSummaryResponse;
import io.mipangg.ticketrush.domain.concert.service.ConcertService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ConcertController.class)
class ConcertControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ConcertService concertService;

    @Test
    @DisplayName("공연 생성 성공 테스트")
    void createConcertSuccessTest() throws Exception {

        ConcertCreateRequest req = genConcertCreateRequest();

        ConcertCreateResponse resp = new ConcertCreateResponse(
                1L,
                "뮤지컬 위키드",
                LocalDateTime.of(2026, 10, 10, 19, 0),
                LocalDateTime.of(2026, 10, 10, 21, 30),
                "예술의전당 오페라극장",
                LocalDateTime.now()
        );

        when(concertService.saveConcert(req)).thenReturn(resp);

        mockMvc.perform(post("/api/concerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("뮤지컬 위키드"))
                .andExpect(jsonPath("$.location").value("예술의전당 오페라극장"))
                .andDo(print()); // 요청/응답 로그 출력

        verify(concertService).saveConcert(any(ConcertCreateRequest.class));
    }

    @Test
    @DisplayName("공연 단건 조회 성공 테스트")
    void getConcertDetailSuccessTest() throws Exception {

        Long concertId = 1L;

        ConcertDetailResponse resp = new ConcertDetailResponse(
                1L,
                "뮤지컬 위키드",
                LocalDateTime.of(2026, 10, 10, 19, 0),
                LocalDateTime.of(2026, 10, 10, 21, 30),
                "예술의전당 오페라극장",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(concertService.getConcertDetail(concertId)).thenReturn(resp);

        mockMvc.perform(get("/api/concerts/{concertId}", concertId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("뮤지컬 위키드"))
                .andExpect(jsonPath("$.startAt").value("2026-10-10T19:00:00"))
                .andExpect(jsonPath("$.endAt").value("2026-10-10T21:30:00"))
                .andExpect(jsonPath("$.location").value("예술의전당 오페라극장"))
                .andDo(print());


    }

    @Test
    @DisplayName("공연 목록 조회 성공 테스트")
    void getConcertsSuccessTest() throws Exception {

        List<ConcertSummaryResponse> resp = genConcertSummaryResponses();

        when(concertService.getConcerts()).thenReturn(resp);

        mockMvc.perform(get("/api/concerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("뮤지컬 위키드"))
                .andExpect(jsonPath("$[0].startAt").value("2026-10-10T19:00:00"))
                .andExpect(jsonPath("$[0].endAt").value("2026-10-10T21:30:00"))
                .andExpect(jsonPath("$[0].location").value("예술의전당 오페라극장"))
                .andDo(print());

        verify(concertService).getConcerts();

    }

}