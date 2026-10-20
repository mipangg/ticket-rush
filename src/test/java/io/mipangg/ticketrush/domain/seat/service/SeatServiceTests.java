package io.mipangg.ticketrush.domain.seat.service;

import static io.mipangg.ticketrush.util.TestUtil.genSeatGrade;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.mipangg.ticketrush.domain.seat.dto.SeatCreateRequest;
import io.mipangg.ticketrush.domain.seat.dto.SeatCreateResponse;
import io.mipangg.ticketrush.domain.seat.entity.Seat;
import io.mipangg.ticketrush.domain.seat.entity.SeatGrade;
import io.mipangg.ticketrush.domain.seat.repository.SeatGradeRepository;
import io.mipangg.ticketrush.domain.seat.repository.SeatRepository;
import io.mipangg.ticketrush.global.exception.CustomLogicException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SeatServiceTests {

    @InjectMocks
    private SeatService seatService;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private SeatGradeRepository seatGradeRepository;

    @Test
    @DisplayName("좌석을 등록할 수 있다")
    void saveSeatSuccessTest() {

        Long concertId = 1L;

        SeatCreateRequest req = new SeatCreateRequest("A-01", 1L);
        SeatGrade seatGrade = genSeatGrade();
        Seat savedSeat = Seat.builder()
                .number("A-01")
                .seatGrade(seatGrade)
                .build();

        when(seatGradeRepository.findByIdAndConcertId(req.seatGradeId(), concertId))
                .thenReturn(Optional.of(seatGrade));
        when(seatRepository.existsByNumberAndSeatGrade(savedSeat.getNumber(), seatGrade))
                .thenReturn(false);
        when(seatRepository.save(any(Seat.class))).thenReturn(savedSeat);

        SeatCreateResponse resp = seatService.saveSeat(concertId, req);

        assertThat(resp.concertId()).isEqualTo(concertId);
        assertThat(resp.seatGradeId()).isEqualTo(seatGrade.getId());
        assertThat(resp.number()).isEqualTo("A-01");
        verify(seatGradeRepository).findByIdAndConcertId(req.seatGradeId(), concertId);
        verify(seatRepository).save(any(Seat.class));

    }

    @Test
    @DisplayName("요청받은 콘서트 좌석등급이 존재하지 않으면 예외가 발생한다")
    void saveSeatSeatGradeNotFoundFailTest() {

        Long concertId = 1L;
        SeatCreateRequest req = new SeatCreateRequest("A-01", 1L);

        when(seatGradeRepository.findByIdAndConcertId(concertId, req.seatGradeId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> {
                    seatService.saveSeat(concertId, req);
                }
        ).isInstanceOf(CustomLogicException.class)
                .hasMessage("데이터를 찾을 수 없습니다.");

    }

    @Test
    @DisplayName("이미 존재하는 좌석을 생성하려 시도하면 예외가 발생한다")
    void saveSeatConflictFailTest() {

        Long concertId = 1L;
        SeatCreateRequest req = new SeatCreateRequest("A-01", 1L);
        SeatGrade seatGrade = genSeatGrade();

        when(seatGradeRepository.findByIdAndConcertId(req.seatGradeId(), concertId))
                .thenReturn(Optional.of(seatGrade));
        when(seatRepository.existsByNumberAndSeatGrade(anyString(), any(SeatGrade.class)))
                .thenReturn(true);

        assertThatThrownBy(
                () -> {
                    seatService.saveSeat(concertId, req);
                }
        ).isInstanceOf(CustomLogicException.class)
                .hasMessage("이미 존재하는 데이터입니다.");

    }

}