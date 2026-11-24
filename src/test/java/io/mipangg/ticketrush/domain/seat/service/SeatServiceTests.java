package io.mipangg.ticketrush.domain.seat.service;

import static io.mipangg.ticketrush.util.TestUtil.genSeat;
import static io.mipangg.ticketrush.util.TestUtil.genSeatGrade;
import static io.mipangg.ticketrush.util.TestUtil.genSeats;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.mipangg.ticketrush.domain.seat.dto.SeatCreateRequest;
import io.mipangg.ticketrush.domain.seat.dto.SeatCreateResponse;
import io.mipangg.ticketrush.domain.seat.dto.SeatDetailResponse;
import io.mipangg.ticketrush.domain.seat.dto.SeatSummaryResponse;
import io.mipangg.ticketrush.domain.seat.entity.Seat;
import io.mipangg.ticketrush.domain.seat.entity.SeatGrade;
import io.mipangg.ticketrush.domain.seat.repository.SeatGradeRepository;
import io.mipangg.ticketrush.domain.seat.repository.SeatRepository;
import io.mipangg.ticketrush.global.common.CursorResponse;
import io.mipangg.ticketrush.global.exception.CustomLogicException;
import java.awt.Cursor;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

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
        assertThat(resp.seatGrade()).isEqualTo(seatGrade.getGrade());
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

    @Test
    @DisplayName("좌석 id로 좌석을 단건 조회할 수 있다")
    void getSeatDetailSuccessTest() {

        Long concertId = 1L;
        Long seatId = 1L;

        Seat seat = genSeat();

        when(seatRepository.findByIdAndConcertId(seatId, concertId))
                .thenReturn(Optional.of(seat));

        SeatDetailResponse resp = seatService.getSeatDetail(concertId, seatId);

        assertThat(resp.seatGrade()).isEqualTo("VIP");
        assertThat(resp.number()).isEqualTo("A-01");

        verify(seatRepository).findByIdAndConcertId(seatId, concertId);

    }

    @Test
    @DisplayName("좌석 단건 조회 시 일치하는 id가 없으면 예외가 발생한다")
    void getSeatDetailFailTest() {

        Long concertId = 1L;
        Long seatId = 1L;

        when(seatRepository.findByIdAndConcertId(seatId, concertId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> {
                    seatService.getSeatDetail(concertId, seatId);
                }
        ).isInstanceOf(CustomLogicException.class)
                .hasMessage("데이터를 찾을 수 없습니다.");

    }

    @Test
    @DisplayName("공연 id로 좌석 목록 조회를 할 수 있다")
    void getSeatsSuccessTest() {

        Long concertId = 1L;
        int size = 10;
        List<Seat> seats = genSeats();

        when(seatRepository.findByConcertIdAfterCursor(concertId, null, PageRequest.of(0, size + 1)))
                .thenReturn(seats);

        CursorResponse<SeatSummaryResponse> resp = seatService.getSeats(concertId, null, size);

        assertThat(resp.content()).hasSize(3);
        assertThat(resp.content().get(0).number()).isEqualTo("A-01");
        assertThat(resp.content().get(0).seatGrade()).isEqualTo("VIP");
        assertThat(resp.content().get(1).number()).isEqualTo("A-02");
        assertThat(resp.content().get(1).seatGrade()).isEqualTo("VIP");
        assertThat(resp.content().get(2).number()).isEqualTo("A-03");
        assertThat(resp.content().get(2).seatGrade()).isEqualTo("R");
        assertThat(resp.hasNext()).isFalse();
        assertThat(resp.nextCursor()).isNull();

    }

    @Test
    @DisplayName("공연 id로 조회되는 좌석 목록이 없다면 빈 리스트를 반환한다")
    void getSeatsSuccessTestWithEmptyList() {

        Long concertId = 1L;
        int size = 10;

        when(seatRepository.findByConcertIdAfterCursor(concertId, null, PageRequest.of(0, size + 1)))
                .thenReturn(Collections.emptyList());

        CursorResponse<SeatSummaryResponse> resp = seatService.getSeats(concertId, null, size);
        assertThat(resp.content()).isEmpty();
        assertThat(resp.hasNext()).isFalse();
        assertThat(resp.nextCursor()).isNull();

    }

}