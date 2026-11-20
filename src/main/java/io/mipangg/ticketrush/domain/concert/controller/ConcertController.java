package io.mipangg.ticketrush.domain.concert.controller;

import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateRequest;
import io.mipangg.ticketrush.domain.concert.dto.ConcertDetailResponse;
import io.mipangg.ticketrush.domain.concert.dto.ConcertSummaryResponse;
import io.mipangg.ticketrush.domain.concert.service.ConcertService;
import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateResponse;
import io.mipangg.ticketrush.global.common.CursorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Concert", description = "공연 관련 API")
@RestController
@RequestMapping("/api/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;

    @Operation(
            summary = "공연 생성",
            description = "정보를 입력하여 공연을 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "공연 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 정보를 포함한 공연")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConcertCreateResponse createConcert(@Valid @RequestBody ConcertCreateRequest req) {
        return concertService.saveConcert(req);
    }

    @Operation(
            summary = "공연 단건 조회",
            description = "공연 ID를 이용하여 공연 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공연 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 공연")
    })
    @GetMapping("/{concertId}")
    @ResponseStatus(HttpStatus.OK)
    public ConcertDetailResponse getConcertDetail(@PathVariable Long concertId) {
        return concertService.getConcertDetail(concertId);
    }

    @Operation(
            summary = "공연 목록 조회",
            description = "저장된 모든 공연 목록 데이터를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공연 목록 조회 성공"),
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CursorResponse<ConcertSummaryResponse> getConcerts(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") @Min(1) @Max(30) int size
    ) {
        return concertService.getConcerts(cursor, size);
    }

}

