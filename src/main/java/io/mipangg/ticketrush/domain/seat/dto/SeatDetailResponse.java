package io.mipangg.ticketrush.domain.seat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record SeatDetailResponse(

        @Schema(description = "좌석 ID", example = "101")
        Long id,

        @Schema(description = "공연 ID", example = "1")
        Long concertId,

        @Schema(description = "좌석 번호", example = "A-01")
        String number,

        @Schema(description = "좌석 등급", example = "VIP")
        String seatGrade,

        @Schema(description = "좌석 가격", example = "150000")
        BigDecimal price,

        @Schema(description = "좌석 상태", example = "AVAILABLE")
        String status
) {

}
