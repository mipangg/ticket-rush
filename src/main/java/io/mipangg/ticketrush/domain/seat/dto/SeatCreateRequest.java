package io.mipangg.ticketrush.domain.seat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SeatCreateRequest(

        @NotBlank
        String number,

        @NotNull
        @Positive
        Long seatGradeId
) {

}
