package io.mipangg.ticketrush.domain.concert.repository;

import io.mipangg.ticketrush.domain.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

}
