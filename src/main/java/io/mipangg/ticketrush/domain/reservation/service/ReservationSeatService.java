package io.mipangg.ticketrush.domain.reservation.service;

import io.mipangg.ticketrush.domain.reservation.dto.ReservationSeatSummaryResponse;
import io.mipangg.ticketrush.domain.reservation.entity.Reservation;
import io.mipangg.ticketrush.domain.reservation.repository.ReservationSeatRepository;
import io.mipangg.ticketrush.domain.seat.entity.Seat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationSeatService {

    private final ReservationSeatRepository reservationSeatRepository;

    public List<ReservationSeatSummaryResponse> saveReservationSeats(
            Reservation reservation,
            List<Seat> seats
    ) {
        // TODO: ReservationSeat 중복 검사 후 추가
    }
}
