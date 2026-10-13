package io.mipangg.ticketrush.domain.payment.entity;

import lombok.Getter;

@Getter
public enum PaymentStatus {

    PENDING("결제 진행중"),
    SUCCESS("결제 성공"),
    FAILED("결제 실패"),
    CANCELLED("결제 취소");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }

}
