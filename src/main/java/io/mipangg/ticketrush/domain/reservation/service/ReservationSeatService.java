package io.mipangg.ticketrush.domain.reservation.service;

import io.mipangg.ticketrush.domain.reservation.entity.Reservation;
import io.mipangg.ticketrush.domain.reservation.entity.ReservationSeat;
import io.mipangg.ticketrush.domain.reservation.entity.ReservationStatus;
import io.mipangg.ticketrush.domain.reservation.repository.ReservationSeatRepository;
import io.mipangg.ticketrush.domain.seat.entity.Seat;
import io.mipangg.ticketrush.global.exception.CustomLogicException;
import io.mipangg.ticketrush.global.exception.ErrorCode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationSeatService {

    private final ReservationSeatRepository reservationSeatRepository;

    @Transactional
    public void saveReservationSeats(Reservation reservation, List<Seat> seats) {

        if (reservationSeatRepository.existsByReservationAndSeatIn(reservation, seats)) {
            throw new CustomLogicException(ErrorCode.CONFLICT, "이미 존재하는 예약 좌석입니다.");
        }

        try {
            for (Seat seat : seats) {
                reservationSeatRepository.save(new ReservationSeat(reservation, seat));
            }
        } catch (DataIntegrityViolationException e) {
            throw new CustomLogicException(ErrorCode.DATA_INTEGRITY_VIOLATION);
        }
    }

    @Transactional(readOnly = true)
    public boolean isPossibleSeat(List<Seat> seats) {

        return !reservationSeatRepository.existsReservedSeat(
                seats,
                List.of(
                        ReservationStatus.PENDING,
                        ReservationStatus.CONFIRMED
                )
        );

    }
}
