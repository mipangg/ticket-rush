package io.mipangg.ticketrush.domain.reservation.service;

import io.mipangg.ticketrush.domain.reservation.dto.ReservationCreateRequest;
import io.mipangg.ticketrush.domain.reservation.dto.ReservationCreateResponse;
import io.mipangg.ticketrush.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationCreateResponse saveReservation(ReservationCreateRequest req) {


        return null;
    }
}
