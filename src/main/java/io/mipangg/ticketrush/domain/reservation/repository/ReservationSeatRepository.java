package io.mipangg.ticketrush.domain.reservation.repository;

import io.mipangg.ticketrush.domain.reservation.entity.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {

}
