package io.mipangg.ticketrush.domain.reservation.dto;

import io.mipangg.ticketrush.domain.reservation.entity.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ReservationCreateResponse(

        @Schema(description = "예약 ID", example = "1001")
        Long id,

        @Schema(description = "예약자 ID", example = "1")
        Long userId,

        @Schema(description = "공연 ID", example = "1")
        Long concertId,

        @Schema(description = "예약 상태", example = "PENDING")
        ReservationStatus status,

        @Schema(description = "예약 총 금액", example = "PENDING")
        BigDecimal totalPrice,

        @Schema(description = "예약 만료 시간", example = "2026-09-21T16:35:00")
        LocalDateTime expiresAt,

        @Schema(description = "예약 생성 시간", example = "2026-09-21T16:25:00")
        LocalDateTime createdAt,

        @Schema(description = "예약 좌석 정보 목록")
        List<ReservationSeatSummaryResponse> seats
) {

}