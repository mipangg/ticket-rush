package io.mipangg.ticketrush.domain.concert.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateRequest;
import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateResponse;
import io.mipangg.ticketrush.domain.concert.entity.Concert;
import io.mipangg.ticketrush.domain.concert.repository.ConcertRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTests {

    @InjectMocks
    private ConcertService concertService;

    @Mock
    private ConcertRepository concertRepository;

    @Test
    @DisplayName("공연을 정상적으로 등록할 수 있다")
    void saveConcertSuccessTest() {

        ConcertCreateRequest req = new ConcertCreateRequest(
                "뮤지컬 위키드",
                LocalDateTime.of(2026, 10, 10, 19, 0),
                LocalDateTime.of(2026, 10, 10, 21, 30),
                "예술의전당 오페라극장"
        );

        ConcertCreateResponse resp = concertService.saveConcert(req);

        assertThat(resp.title()).isEqualTo("뮤지컬 위키드");
        assertThat(resp.startAt()).isEqualTo(LocalDateTime.of(2026, 10, 10, 19, 0));
        assertThat(resp.endAt()).isEqualTo(LocalDateTime.of(2026, 10, 10, 21, 30));
        assertThat(resp.location()).isEqualTo("예술의전당 오페라극장");

        verify(concertRepository).save(any(Concert.class));

    }

}