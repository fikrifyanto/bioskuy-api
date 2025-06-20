package com.bioskuy.api.controller;

import com.bioskuy.api.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/notification")
    public ResponseEntity<String> handleNotification(HttpServletRequest request) throws IOException {
        this.paymentService.verifyPayment(request);

        return ResponseEntity.ok("Notification processed");
    }
}
