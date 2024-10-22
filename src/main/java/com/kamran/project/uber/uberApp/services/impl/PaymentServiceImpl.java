package com.kamran.project.uber.uberApp.services.impl;

import com.kamran.project.uber.uberApp.entities.Payment;
import com.kamran.project.uber.uberApp.entities.Ride;
import com.kamran.project.uber.uberApp.entities.enums.PaymentStatus;
import com.kamran.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.kamran.project.uber.uberApp.repositories.PaymentRepository;
import com.kamran.project.uber.uberApp.services.PaymentService;
import com.kamran.project.uber.uberApp.strategies.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStrategyManager paymentStrategyManager;

    @Override
    public void processPayment(Ride ride) {
        Payment payment = paymentRepository.findByRide(ride)
                        .orElseThrow( () -> new ResourceNotFoundException("Payment not found for ride with id: " + ride.getId()));
        paymentStrategyManager.paymentStrategy(payment.getPaymentMethod()).processPayment(payment);
    }

    @Override
    public Payment createNewPayment(Ride ride) {
        Payment payment = Payment.builder()
                .ride(ride)
                .paymentMethod(ride.getPaymentMethod())
                .amount(ride.getFare())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        return paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus status) {
        payment.setPaymentStatus(status);
        paymentRepository.save(payment);
    }
}