package io.mipangg.ticketrush.domain.seat.dto;

import java.time.LocalDateTime;

public record SeatCreateResponse(
        Long id,
        Long concertId,
        String number,
        Long seatGradeId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
