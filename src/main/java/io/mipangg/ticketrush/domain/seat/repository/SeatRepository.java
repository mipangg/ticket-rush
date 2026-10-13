package io.mipangg.ticketrush.domain.seat.repository;

import io.mipangg.ticketrush.domain.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {

}
