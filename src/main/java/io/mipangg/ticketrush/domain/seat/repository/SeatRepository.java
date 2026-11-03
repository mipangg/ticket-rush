package io.mipangg.ticketrush.domain.seat.repository;

import io.mipangg.ticketrush.domain.seat.entity.Seat;
import io.mipangg.ticketrush.domain.seat.entity.SeatGrade;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    boolean existsByNumberAndSeatGrade(String number, SeatGrade seatGrade);

    @Query("select s from Seat s "
            + "join fetch s.seatGrade sg "
            + "where s.id = :id "
            + "and sg.concert.id = :concertId")
    Optional<Seat> findByIdAndConcertIdWithSeatGrade(
            @Param("id") Long id,
            @Param("concertId") Long concertId
    );

    @Query("select s from Seat s "
            + "join fetch s.seatGrade sg "
            + "where sg.concert.id = :concertId")
    List<Seat> getSeatsByConcertId(@Param("concertId") Long concertId);
}
