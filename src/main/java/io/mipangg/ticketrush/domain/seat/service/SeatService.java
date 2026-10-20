package io.mipangg.ticketrush.domain.seat.service;

import io.mipangg.ticketrush.domain.seat.dto.SeatCreateRequest;
import io.mipangg.ticketrush.domain.seat.dto.SeatCreateResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeatService {

    @Transactional
    public SeatCreateResponse saveSeat(SeatCreateRequest req) {
        return null;
    }
}
