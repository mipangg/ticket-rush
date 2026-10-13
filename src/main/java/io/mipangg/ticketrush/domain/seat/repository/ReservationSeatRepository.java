package io.mipangg.ticketrush.domain.seat.repository;

import io.mipangg.ticketrush.domain.seat.entity.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {

}
