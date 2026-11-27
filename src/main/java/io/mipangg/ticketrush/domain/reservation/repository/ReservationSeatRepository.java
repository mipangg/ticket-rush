package io.mipangg.ticketrush.domain.reservation.repository;

import io.mipangg.ticketrush.domain.reservation.entity.Reservation;
import io.mipangg.ticketrush.domain.reservation.entity.ReservationSeat;
import io.mipangg.ticketrush.domain.reservation.entity.ReservationStatus;
import io.mipangg.ticketrush.domain.seat.entity.Seat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {

    boolean existsByReservationAndSeatIn(Reservation reservation, List<Seat> seats);

    @Query("select case when count(rs) > 0 then true else false end "
            + "from ReservationSeat rs "
            + "join rs.reservation r "
            + "where rs.seat in :seats "
            + "and r.status in :statuses")
    boolean existsReservedSeat(
            @Param("seats") List<Seat> seats,
            @Param("statuses") List<ReservationStatus> statuses
    );
}
