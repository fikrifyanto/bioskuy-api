package com.bioskuy.api.controller;

import com.bioskuy.api.model.payment.PaymentRequest;
import com.bioskuy.api.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/notification")
    public ResponseEntity<String> handleNotification(@RequestBody PaymentRequest request) {
        this.paymentService.verifyPayment(request);

        return ResponseEntity.ok("Notification processed");
    }
}
