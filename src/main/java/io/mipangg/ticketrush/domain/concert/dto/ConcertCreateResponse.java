package io.mipangg.ticketrush.domain.concert.dto;

import java.time.LocalDateTime;

public record ConcertCreateResponse (
        Long id,
        String title,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String location,
        LocalDateTime createdAt
) {

}
