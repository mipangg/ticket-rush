package io.mipangg.ticketrush.util;

import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateRequest;
import io.mipangg.ticketrush.domain.concert.dto.ConcertSummaryResponse;
import io.mipangg.ticketrush.domain.concert.entity.Concert;
import io.mipangg.ticketrush.domain.seat.dto.SeatSummaryResponse;
import io.mipangg.ticketrush.domain.seat.entity.Seat;
import io.mipangg.ticketrush.domain.seat.entity.SeatGrade;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TestUtil {

    public static ConcertCreateRequest genConcertCreateRequest() {
        return new ConcertCreateRequest(
                "뮤지컬 위키드",
                LocalDateTime.of(2026, 10, 10, 19, 0),
                LocalDateTime.of(2026, 10, 10, 21, 30),
                "예술의전당 오페라극장"
        );
    }

    public static Concert genConcert() {
        return genConcerts().getFirst();
    }

    public static List<Concert> genConcerts() {
        return List.of(
                Concert.builder()
                        .title("뮤지컬 위키드")
                        .startAt(LocalDateTime.of(2026, 10, 10, 19, 0))
                        .endAt(LocalDateTime.of(2026, 10, 10, 21, 30))
                        .location("예술의전당 오페라극장")
                        .build(),
                Concert.builder()
                        .title("뮤지컬 라이온킹")
                        .startAt(LocalDateTime.of(2026, 11, 1, 19, 0))
                        .endAt(LocalDateTime.of(2026, 11, 1, 21, 30))
                        .location("샤롯데씨어터")
                        .build()
        );
    }

    public static List<ConcertSummaryResponse> genConcertSummaryResponses() {
        return List.of(
                new ConcertSummaryResponse(
                        1L,
                        "뮤지컬 위키드",
                        LocalDateTime.of(2026, 10, 10, 19, 0),
                        LocalDateTime.of(2026, 10, 10, 21, 30),
                        "예술의전당 오페라극장"
                ),
                new ConcertSummaryResponse(
                        2L,
                        "뮤지컬 라이온킹",
                        LocalDateTime.of(2026, 11, 1, 19, 0),
                        LocalDateTime.of(2026, 11, 1, 21, 30),
                        "샤롯데씨어터"
                )
        );
    }

    public static Seat genSeat() {
        return genSeats().getFirst();
    }

    public static List<Seat> genSeats() {
        return List.of(
                Seat.builder()
                        .number("A-01")
                        .seatGrade(genSeatGrades().getFirst())
                        .build(),
                Seat.builder()
                        .number("A-02")
                        .seatGrade(genSeatGrades().getFirst())
                        .build(),
                Seat.builder()
                        .number("A-03")
                        .seatGrade(genSeatGrades().get(1))
                        .build()
        );
    }

    public static List<SeatSummaryResponse> genSeatSummaryResponses() {
        return List.of(
                new SeatSummaryResponse(
                        101L,
                        "A-01",
                        "VIP",
                        BigDecimal.valueOf(150000),
                        "AVAILABLE"
                ),
                new SeatSummaryResponse(
                        102L,
                        "A-02",
                        "VIP",
                        BigDecimal.valueOf(150000),
                        "HELD"
                ),
                new SeatSummaryResponse(
                        103L,
                        "A-03",
                        "R",
                        BigDecimal.valueOf(100000),
                        "SOLD"
                )
        );
    }

    public static List<SeatGrade> genSeatGrades() {
        return List.of(
                SeatGrade.builder()
                        .concert(genConcert())
                        .grade("VIP")
                        .price(BigDecimal.valueOf(180000))
                        .build(),
                SeatGrade.builder()
                        .concert(genConcert())
                        .grade("R")
                        .price(BigDecimal.valueOf(150000))
                        .build(),
                SeatGrade.builder()
                        .concert(genConcert())
                        .grade("S")
                        .price(BigDecimal.valueOf(120000))
                        .build(),
                SeatGrade.builder()
                        .concert(genConcert())
                        .grade("A")
                        .price(BigDecimal.valueOf(90000))
                        .build()
        );
    }

    // VIP
    public static SeatGrade genSeatGrade() {
        return genSeatGrades().getFirst();
    }

}
