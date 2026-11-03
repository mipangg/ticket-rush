package io.mipangg.ticketrush.domain.seat.controller;

import io.mipangg.ticketrush.domain.seat.dto.SeatCreateRequest;
import io.mipangg.ticketrush.domain.seat.dto.SeatCreateResponse;
import io.mipangg.ticketrush.domain.seat.dto.SeatDetailResponse;
import io.mipangg.ticketrush.domain.seat.dto.SeatSummaryResponse;
import io.mipangg.ticketrush.domain.seat.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Seat", description = "좌석 관련 API")
@RestController
@RequestMapping("/api/concerts/{concertId}/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @Operation(
            summary = "좌석 생성",
            description = "정보를 입력하여 좌석을 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "좌석 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 정보를 포함한 좌석"),
            @ApiResponse(responseCode = "404", description = "공연 정보를 찾을 수 없는 좌석"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 좌석"),
            @ApiResponse(responseCode = "409", description = "데이터 무결성 제약조건 위반")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeatCreateResponse createSeat(
            @Positive @PathVariable Long concertId,
            @Valid @RequestBody SeatCreateRequest req
) {
        return seatService.saveSeat(concertId, req);
    }

    @Operation(
            summary = "좌석 단건 조회",
            description = "공연 ID와 좌석 ID를 이용하여 특정 공연의 개별 좌석 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좌석 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 좌석")
    })
    @GetMapping("/{seatId}")
    @ResponseStatus(HttpStatus.OK)
    public SeatDetailResponse getSeatDetail(
            @Positive @PathVariable Long concertId,
            @Positive @PathVariable Long seatId
    ) {
        return seatService.getSeatDetail(concertId, seatId);
    }

    @Operation(
            summary = "좌석 목록 조회",
            description = "공연 ID를 이용하여 특정 공연의 전체 좌석 목록 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좌석 목록 조회 성공"),
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SeatSummaryResponse> getSeats(@Positive @PathVariable Long concertId) {
        return seatService.getSeats(concertId);
    }

}
