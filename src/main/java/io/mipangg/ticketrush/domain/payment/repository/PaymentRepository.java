package io.mipangg.ticketrush.domain.payment.repository;

import io.mipangg.ticketrush.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
