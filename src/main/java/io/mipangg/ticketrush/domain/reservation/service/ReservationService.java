package io.mipangg.ticketrush.domain.reservation.service;

import io.mipangg.ticketrush.domain.concert.entity.Concert;
import io.mipangg.ticketrush.domain.concert.repository.ConcertRepository;
import io.mipangg.ticketrush.domain.reservation.dto.ReservationCreateRequest;
import io.mipangg.ticketrush.domain.reservation.dto.ReservationCreateResponse;
import io.mipangg.ticketrush.domain.reservation.dto.ReservationSeatSummaryResponse;
import io.mipangg.ticketrush.domain.reservation.entity.Reservation;
import io.mipangg.ticketrush.domain.reservation.repository.ReservationRepository;
import io.mipangg.ticketrush.domain.seat.entity.Seat;
import io.mipangg.ticketrush.domain.seat.repository.SeatRepository;
import io.mipangg.ticketrush.domain.user.entity.User;
import io.mipangg.ticketrush.domain.user.repository.UserRepository;
import io.mipangg.ticketrush.global.exception.CustomLogicException;
import io.mipangg.ticketrush.global.exception.ErrorCode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;

    private final ReservationSeatService reservationSeatService;

    @Transactional
    public ReservationCreateResponse saveReservation(ReservationCreateRequest req) {

        // 좌석 조회
        List<Seat> seats = seatRepository.findByIdIn(req.seatIds());

        if (seats.isEmpty()) {
            throw new CustomLogicException(ErrorCode.NOT_FOUND, "해당 좌석 데이터를 찾을 수 없습니다.");
        }

        // seat.SeatGrade.concertId == req.concertId 검증
        Set<Long> concertIds = new HashSet<>();
        for (Seat seat : seats) {
            concertIds.add(seat.getSeatGrade().getConcert().getId());
        }

        if (concertIds.size() != 1 || !concertIds.contains(req.concertId())) {
            throw new CustomLogicException(
                    ErrorCode.BAD_REQUEST,
                    "콘서트 번호가 잘못된 좌석의 예약 요청입니다."
            );
        }

        // TODO: Redis 사용 좌석 상태 확인 (AVAILABLE?)
        // Seat -> ReservationSeat -> Reservation -> ReservationStatus (PENDING/CONFIRMED면 예약 불가)
        if (!reservationSeatService.isPossibleSeat(seats)) {
            throw new CustomLogicException(ErrorCode.CONFLICT, "이미 선점/예약된 좌석이 포함되어 있습니다.");
        }


        // TODO: 좌석 상태 변경 (AVAILABLE -> HELD)


        Concert concert = seats.getFirst()
                .getSeatGrade()
                .getConcert();

        User user = userRepository.findById(req.userId())
                .orElseThrow(() ->
                        new CustomLogicException(
                                ErrorCode.NOT_FOUND,
                                "해당 유저 데이터를 찾을 수 없습니다."
                        )
                );

        Reservation reservation = Reservation.builder()
                .user(user)
                .concert(concert)
                .build();


        // reservation 추가
        Reservation savedReservation = reservationRepository.save(reservation);

        // ReservationSeat 중복 검사 후 추가
        reservationSeatService.saveReservationSeats(reservation, seats);

        return new ReservationCreateResponse(
                savedReservation.getId(),
                user.getId(),
                concert.getId(),
                savedReservation.getStatus(),
                calculateTotalPrice(seats),
                savedReservation.getExpiresAt(),
                savedReservation.getCreatedAt(),
                getReservationSeatSummaries(seats)
        );
    }

    // 예약된 좌석의 총 가격 계산
    private BigDecimal calculateTotalPrice(List<Seat> seats) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Seat seat : seats) {
            totalPrice = totalPrice.add(seat.getSeatGrade().getPrice());
        }

        return totalPrice;
    }

    // 예약된 좌석 요약 정보 반환
    private List<ReservationSeatSummaryResponse> getReservationSeatSummaries(List<Seat> seats) {
        List<ReservationSeatSummaryResponse> seatSummaries = new ArrayList<>();

        for (Seat seat : seats) {
            seatSummaries.add(
                    new ReservationSeatSummaryResponse(
                            seat.getId(),
                            seat.getNumber(),
                            seat.getSeatGrade().getGrade(),
                            seat.getSeatGrade().getPrice()
                    )
            );
        }

        return seatSummaries;
    }
}
