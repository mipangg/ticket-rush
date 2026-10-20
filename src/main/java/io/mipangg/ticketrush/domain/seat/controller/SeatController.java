package io.mipangg.ticketrush.domain.seat.controller;

import io.mipangg.ticketrush.domain.seat.dto.SeatCreateRequest;
import io.mipangg.ticketrush.domain.seat.dto.SeatCreateResponse;
import io.mipangg.ticketrush.domain.seat.service.SeatService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/concerts/{concertId}/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeatCreateResponse createSeat(
            @Positive @PathVariable Long concertId,
            @Valid @RequestBody SeatCreateRequest req
) {
        return seatService.saveSeat(concertId, req);
    }

}
