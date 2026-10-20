package io.mipangg.ticketrush.domain.concert.service;

import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateRequest;
import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateResponse;
import io.mipangg.ticketrush.domain.concert.entity.Concert;
import io.mipangg.ticketrush.domain.concert.repository.ConcertRepository;
import io.mipangg.ticketrush.global.exception.CustomLogicException;
import io.mipangg.ticketrush.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    @Transactional
    public ConcertCreateResponse saveConcert(ConcertCreateRequest req) {

        if (!req.endAt().isAfter(req.startAt())) {
            throw new CustomLogicException(
                    ErrorCode.BAD_REQUEST,
                    "공연 종료 시간은 시작 시간 이후여야 합니다."
            );
        }

        Concert concert = Concert.builder()
                .title(req.title())
                .startAt(req.startAt())
                .endAt(req.endAt())
                .location(req.location())
                .build();

        Concert savedConcert = concertRepository.save(concert);

        return new ConcertCreateResponse(
                savedConcert.getId(),
                savedConcert.getTitle(),
                savedConcert.getStartAt(),
                savedConcert.getEndAt(),
                savedConcert.getLocation(),
                savedConcert.getCreatedAt()
        );
    }
}
