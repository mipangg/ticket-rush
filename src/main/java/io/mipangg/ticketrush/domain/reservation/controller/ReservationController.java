package io.mipangg.ticketrush.domain.reservation.controller;

import io.mipangg.ticketrush.domain.reservation.dto.ReservationCreateRequest;
import io.mipangg.ticketrush.domain.reservation.dto.ReservationCreateResponse;
import io.mipangg.ticketrush.domain.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Reservation", description = "예약 관련 API")
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(
            summary = "예약 생성",
            description = "정보를 입력하여 예약을 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "예약 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 정보를 포함한 예약")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationCreateResponse createReservation(
            @Valid @RequestBody ReservationCreateRequest req
    ) {
        return reservationService.saveReservation(req);
    }

}
