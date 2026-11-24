package io.mipangg.ticketrush.domain.reservation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record ReservationCreateRequest(

        @NotNull
        @Positive
        Long concertId,

        @NotNull
        @Positive
        Long userId,

        @NotNull
        List<Long> seatIds
) {

}
