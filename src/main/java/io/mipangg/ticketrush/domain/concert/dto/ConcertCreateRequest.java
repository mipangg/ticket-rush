package io.mipangg.ticketrush.domain.concert.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ConcertCreateRequest(

        @NotBlank
        String title,

        @NotNull
        LocalDateTime startAt,

        @NotNull
        LocalDateTime endAt,

        @NotBlank
        String location
) {

}
