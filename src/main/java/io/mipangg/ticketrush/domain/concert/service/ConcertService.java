package io.mipangg.ticketrush.domain.concert.service;

import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateRequest;
import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateResponse;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConcertService {

    @Transactional
    public ConcertCreateResponse saveConcert(ConcertCreateRequest req) {
        return new ConcertCreateResponse(
                1L,
                "뮤지컬 위키드",
                LocalDateTime.of(2026, 10, 10, 19, 0),
                LocalDateTime.of(2026, 10, 10, 21, 30),
                "예술의전당 오페라극장",
                LocalDateTime.now()
        );
    }
}
