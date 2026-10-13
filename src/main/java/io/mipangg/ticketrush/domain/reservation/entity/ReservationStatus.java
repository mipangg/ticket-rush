package io.mipangg.ticketrush.domain.reservation.entity;

import lombok.Getter;

@Getter
public enum ReservationStatus {

    PENDING("예약 진행중"),
    CONFIRMED("예약 완료"),
    CANCELLED("예약 취소"),
    EXPIRED("예약 만료");

    private final String status;

    ReservationStatus(String status) {
        this.status = status;
    }

}
