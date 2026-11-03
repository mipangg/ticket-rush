package io.mipangg.ticketrush.domain.seat.service;

import io.mipangg.ticketrush.domain.seat.dto.SeatCreateRequest;
import io.mipangg.ticketrush.domain.seat.dto.SeatCreateResponse;
import io.mipangg.ticketrush.domain.seat.dto.SeatDetailResponse;
import io.mipangg.ticketrush.domain.seat.dto.SeatSummaryResponse;
import io.mipangg.ticketrush.domain.seat.entity.Seat;
import io.mipangg.ticketrush.domain.seat.entity.SeatGrade;
import io.mipangg.ticketrush.domain.seat.repository.SeatGradeRepository;
import io.mipangg.ticketrush.domain.seat.repository.SeatRepository;
import io.mipangg.ticketrush.global.exception.CustomLogicException;
import io.mipangg.ticketrush.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final SeatGradeRepository seatGradeRepository;

    @Transactional
    public SeatCreateResponse saveSeat(Long concertId, SeatCreateRequest req) {

        SeatGrade seatGrade = getSeatGrade(req.seatGradeId(), concertId);

        Seat seat = Seat.builder()
                .number(req.number())
                .seatGrade(seatGrade)
                .build();

        if (seatRepository.existsByNumberAndSeatGrade(seat.getNumber(), seatGrade)) {
            throw new CustomLogicException(
                    ErrorCode.CONFLICT,
                    "이미 존재하는 좌석입니다."
            );
        }

        try {
            Seat savedSeat = seatRepository.save(seat);

            return new SeatCreateResponse(
                    savedSeat.getId(),
                    concertId,
                    savedSeat.getNumber(),
                    seatGrade.getGrade(),
                    savedSeat.getCreatedAt(),
                    savedSeat.getUpdatedAt()
            );
        } catch (DataIntegrityViolationException e) {
            throw new CustomLogicException(ErrorCode.DATA_INTEGRITY_VIOLATION);
        }
    }

    @Transactional(readOnly = true)
    public SeatDetailResponse getSeatDetail(Long concertId, Long seatId) {

        Seat targetSeat = seatRepository.findByIdAndConcertIdWithSeatGrade(seatId, concertId)
                .orElseThrow(() -> new CustomLogicException(
                        ErrorCode.NOT_FOUND,
                        "해당 좌석 데이터를 찾을 수 없습니다."
                ));

        //  TODO: 우선 좌석 상태는 "AVAILABLE" 고정
        return new SeatDetailResponse(
                targetSeat.getId(),
                concertId,
                targetSeat.getNumber(),
                targetSeat.getSeatGrade().getGrade(),
                targetSeat.getSeatGrade().getPrice(),
                "AVAILABLE"
        );
    }

    @Transactional(readOnly = true)
    public List<SeatSummaryResponse> getSeats(Long concertId) {

        List<SeatSummaryResponse> seatSummaryResps = new ArrayList<>();

        //  TODO: 우선 좌석 상태는 "AVAILABLE" 고정
        seatRepository.getSeatsByConcertId(concertId).forEach(seat ->
                seatSummaryResps.add(new SeatSummaryResponse(
                        seat.getId(),
                        seat.getNumber(),
                        seat.getSeatGrade().getGrade(),
                        seat.getSeatGrade().getPrice(),
                        "AVAILABLE"
                ))
        );

        return seatSummaryResps;
    }

    private SeatGrade getSeatGrade(Long seatGradeId, Long concertId) {
        // 좌석 등급 조회 시 concertId 검증도 함께
        return seatGradeRepository.findByIdAndConcertId(seatGradeId, concertId)
                .orElseThrow(() -> new CustomLogicException(
                        ErrorCode.NOT_FOUND,
                        "해당 콘서트의 좌석 등급 데이터를 찾을 수 없습니다."
                ));
    }
}
