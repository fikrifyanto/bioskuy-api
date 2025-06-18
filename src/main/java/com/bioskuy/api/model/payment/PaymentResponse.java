package com.bioskuy.api.model.payment;

import com.bioskuy.api.model.booking.BookingResponse;
import com.bioskuy.api.model.midtrans.SnapTransactionResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private BookingResponse booking;

    private SnapTransactionResult payment;
}
