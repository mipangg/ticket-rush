package io.mipangg.ticketrush.domain.concert.service;

import static io.mipangg.ticketrush.util.TestUtil.genConcert;
import static io.mipangg.ticketrush.util.TestUtil.genConcertCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateRequest;
import io.mipangg.ticketrush.domain.concert.dto.ConcertCreateResponse;
import io.mipangg.ticketrush.domain.concert.dto.ConcertDetailResponse;
import io.mipangg.ticketrush.domain.concert.entity.Concert;
import io.mipangg.ticketrush.domain.concert.repository.ConcertRepository;
import io.mipangg.ticketrush.global.exception.CustomLogicException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTests {

    @InjectMocks
    private ConcertService concertService;

    @Mock
    private ConcertRepository concertRepository;

    @Test
    @DisplayName("공연을 정상적으로 등록할 수 있다")
    void saveConcertSuccessTest() {

        ConcertCreateRequest req = genConcertCreateRequest();

        Concert savedConcert = Concert.builder()
                .title(req.title())
                .startAt(req.startAt())
                .endAt(req.endAt())
                .location(req.location())
                .build();

        ReflectionTestUtils.setField(savedConcert, "id", 1L); // Mock 객체 id 설정

        when(concertRepository.save(any(Concert.class))).thenReturn(savedConcert);

        ConcertCreateResponse resp = concertService.saveConcert(req);

        assertThat(resp.title()).isEqualTo("뮤지컬 위키드");
        assertThat(resp.startAt()).isEqualTo(LocalDateTime.of(2026, 10, 10, 19, 0));
        assertThat(resp.endAt()).isEqualTo(LocalDateTime.of(2026, 10, 10, 21, 30));
        assertThat(resp.location()).isEqualTo("예술의전당 오페라극장");

        verify(concertRepository).save(any(Concert.class));

    }

    @Test
    @DisplayName("공연을 단건 조회할 수 있다")
    void getConcertDetailSuccessTest() {

        Long concertId = 1L;

        Concert concert = genConcert();

        when(concertRepository.findById(concertId)).thenReturn(Optional.of(concert));

        ConcertDetailResponse resp = concertService.getConcertDetail(concertId);

        assertThat(resp.title()).isEqualTo("뮤지컬 위키드");
        assertThat(resp.startAt()).isEqualTo(LocalDateTime.of(2026, 10, 10, 19, 0));
        assertThat(resp.endAt()).isEqualTo(LocalDateTime.of(2026, 10, 10, 21, 30));
        assertThat(resp.location()).isEqualTo("예술의전당 오페라극장");

        verify(concertRepository).findById(concertId);

    }

    @Test
    @DisplayName("공연 단건 조회 시 일치하는 id가 없으면 예외가 발생한다")
    void getConcertDetailFailTest() {

        Long concertId = 1L;

        when(concertRepository.findById(concertId)).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> {
                    concertService.getConcertDetail(concertId);
                }
        ).isInstanceOf(CustomLogicException.class)
                .hasMessage("데이터를 찾을 수 없습니다.");

    }

}