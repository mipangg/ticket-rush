package io.mipangg.ticketrush.domain.controller;

import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateRequest;
import io.mipangg.ticketrush.domain.concert.service.ConcertService;
import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConcertCreateResponse createConcert(@Valid @RequestBody ConcertCreateRequest req) {
        return concertService.saveConcert(req);
    }

}

