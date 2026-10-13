package io.mipangg.ticketrush.domain.reservation.repository;

import io.mipangg.ticketrush.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

}
