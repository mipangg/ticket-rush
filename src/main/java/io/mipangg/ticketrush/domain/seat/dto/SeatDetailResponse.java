package io.mipangg.ticketrush.domain.seat.dto;

import java.math.BigDecimal;

public record SeatDetailResponse(
        Long id,
        Long concertId,
        String number,
        String seatGrade,
        BigDecimal price,
        String status
) {

}
