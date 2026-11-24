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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final ConcertRepository concertRepository;
    private final UserRepository userRepository;

    private final ReservationSeatService reservationSeatService;

    @Transactional
    public ReservationCreateResponse saveReservation(ReservationCreateRequest req) {

        // 좌석 조회
        List<Seat> seats = seatRepository.findByIdIn(req.seatIds());

        if (seats.isEmpty()) {
            throw new CustomLogicException(ErrorCode.NOT_FOUND, "해당 좌석 데이터를 찾을 수 없습니다.");
        }

        // TODO: 좌석 상태 확인 (AVAILABLE?)
        // TODO: 좌석 상태 변경 (AVAILABLE -> HELD)

        Concert concert = concertRepository.findById(req.concertId())
                .orElseThrow(() ->
                        new CustomLogicException(
                                ErrorCode.NOT_FOUND,
                                "해당 공연 데이터를 찾을 수 없습니다."
                        )
                );

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


        // ReservationSeat 중복 검사 후 추가
        List<ReservationSeatSummaryResponse> reservationSeats =
                reservationSeatService.saveReservationSeats(reservation, seats);

        // reservation 추가
        Reservation savedReservation = reservationRepository.save(reservation);

        return new ReservationCreateResponse(
                savedReservation.getId(),
                user.getId(),
                concert.getId(),
                savedReservation.getStatus(),
                calculateTotalPrice(reservationSeats),
                savedReservation.getExpiresAt(),
                savedReservation.getCreatedAt(),
                reservationSeats
        );
    }

    // 예약된 좌석의 총 가격 계산
    private BigDecimal calculateTotalPrice(List<ReservationSeatSummaryResponse> reservationSeats) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (ReservationSeatSummaryResponse reservationSeat : reservationSeats) {
            totalPrice = totalPrice.add(reservationSeat.price());
        }

        return totalPrice;
    }
}
