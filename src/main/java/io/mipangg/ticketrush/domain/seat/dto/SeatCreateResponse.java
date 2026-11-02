package io.mipangg.ticketrush.domain.seat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record SeatCreateResponse(

        @Schema(description = "좌석 ID", example = "101")
        Long id,

        @Schema(description = "공연 ID", example = "1")
        Long concertId,

        @Schema(description = "좌석 번호", example = "A-01")
        String number,

        @Schema(description = "좌석 등급", example = "VIP")
        String seatGrade,

        @Schema(description = "좌석 생성 시간", example = "2026-09-21T16:00:00")
        LocalDateTime createdAt,

        @Schema(description = "좌석 갱신 시간", example = "2026-09-21T16:00:00")
        LocalDateTime updatedAt
) {

}
