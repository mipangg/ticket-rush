package io.mipangg.ticketrush.domain.seat.repository;

import io.mipangg.ticketrush.domain.seat.entity.SeatGrade;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeatGradeRepository extends JpaRepository<SeatGrade, Long> {

    @Query("select sg from SeatGrade sg where sg.id = :id and sg.concert.id = :concertId")
    Optional<SeatGrade> findByIdAndConcertId(
            @Param("id") Long id,
            @Param("concertId") Long concertId
    );
}
