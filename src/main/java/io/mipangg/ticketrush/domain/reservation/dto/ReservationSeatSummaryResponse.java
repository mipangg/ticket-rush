package io.mipangg.ticketrush.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record ReservationSeatSummaryResponse(
        @Schema(description = "좌석 ID", example = "101")
        Long seatId,

        @Schema(description = "좌석 번호", example = "A-01")
        String number,

        @Schema(description = "좌석 등급", example = "VIP")
        String seatGrade,

        @Schema(description = "좌석 가격", example = "150000")
        BigDecimal price
) {

}