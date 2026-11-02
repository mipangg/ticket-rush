package io.mipangg.ticketrush.util;

import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateRequest;
import io.mipangg.ticketrush.domain.concert.entity.Concert;
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
        return Concert.builder()
                .title("뮤지컬 위키드")
                .startAt(LocalDateTime.of(2026, 10, 10, 19, 0))
                .endAt(LocalDateTime.of(2026, 10, 10, 21, 30))
                .location("예술의전당 오페라극장")
                .build();
    }

    public static Seat genSeat() {
        return Seat.builder()
                .number("A-01")
                .seatGrade(genSeatGrade())
                .build();
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
