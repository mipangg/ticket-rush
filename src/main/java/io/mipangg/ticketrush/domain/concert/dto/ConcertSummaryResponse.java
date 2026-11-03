package io.mipangg.ticketrush.domain.concert.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record ConcertSummaryResponse(

        @Schema(description = "공연 ID", example = "1")
        Long id,

        @Schema(description = "공연 이름", example = "뮤지컬 위키드")
        String title,

        @Schema(description = "공연 시작 시간", example = "2026-10-10T19:00:00")
        LocalDateTime startAt,

        @Schema(description = "공연 종료 시간", example = "2026-10-10T21:30:00")
        LocalDateTime endAt,

        @Schema(description = "공연 장소", example = "예술의전당 오페라극장")
        String location
) {

}
