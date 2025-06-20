package com.bioskuy.api.service;

import com.bioskuy.api.model.midtrans.SnapTransactionResult;
import com.midtrans.httpclient.error.MidtransError;

public interface MidtransServiceInterface {
    SnapTransactionResult createSnapTransaction(
            String orderId,
            double grossAmount,
            String firstName,
            String email
    ) throws MidtransError;
}
