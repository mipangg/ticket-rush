package io.mipangg.ticketrush.domain.concert.service;

import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateRequest;
import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateResponse;
import io.mipangg.ticketrush.domain.concert.dto.ConcertDetailResponse;
import io.mipangg.ticketrush.domain.concert.dto.ConcertSummaryResponse;
import io.mipangg.ticketrush.domain.concert.entity.Concert;
import io.mipangg.ticketrush.domain.concert.repository.ConcertRepository;
import io.mipangg.ticketrush.global.common.CursorResponse;
import io.mipangg.ticketrush.global.exception.CustomLogicException;
import io.mipangg.ticketrush.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

    @Transactional(readOnly = true)
    public ConcertDetailResponse getConcertDetail(Long concertId) {

        Concert targetConcert = concertRepository.findById(concertId)
                .orElseThrow(() -> new CustomLogicException(
                        ErrorCode.NOT_FOUND,
                        "해당 공연 데이터를 찾을 수 없습니다."
                ));

        return new ConcertDetailResponse(
                targetConcert.getId(),
                targetConcert.getTitle(),
                targetConcert.getStartAt(),
                targetConcert.getEndAt(),
                targetConcert.getLocation(),
                targetConcert.getCreatedAt(),
                targetConcert.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public CursorResponse<ConcertSummaryResponse> getConcerts(Long cursor, int size) {

        List<Concert> concerts = concertRepository.findConcerts(
                cursor,
                PageRequest.of(0, size + 1)
        );

        boolean hasNext = concerts.size() > size;
        if (hasNext) {
            concerts = concerts.subList(0, size); // 초과분 제거
        }

        List<ConcertSummaryResponse> content = new ArrayList<>();

        concerts.forEach(concert -> content.add(
                new ConcertSummaryResponse(
                        concert.getId(),
                        concert.getTitle(),
                        concert.getStartAt(),
                        concert.getEndAt(),
                        concert.getLocation()
                )
        ));

        Long nextCursor = hasNext ? content.getLast().id() : null;

        return new CursorResponse<>(content, nextCursor, hasNext);
    }
}
