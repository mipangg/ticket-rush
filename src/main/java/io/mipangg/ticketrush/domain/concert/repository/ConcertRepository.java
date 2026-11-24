package io.mipangg.ticketrush.domain.concert.repository;

import io.mipangg.ticketrush.domain.concert.entity.Concert;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

    @Query("select c from Concert c "
            + "where :cursor is null or c.id > :cursor "
            + "order by c.id asc")
    List<Concert> findAllAfterCursor(@Param("cursor") Long cursor, Pageable pageable);

}
