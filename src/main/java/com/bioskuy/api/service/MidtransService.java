package com.bioskuy.api.service;

import com.bioskuy.api.model.midtrans.SnapTransactionResult;
import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransSnapApi;
import com.midtrans.httpclient.error.MidtransError;
import org.springframework.beans.factory.annotation.Value;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MidtransService implements MidtransServiceInterface {

    private final MidtransSnapApi snapApi;

    public MidtransService(
            @Value("${midtrans.server-key}") String serverKey,
            @Value("${midtrans.client-key}") String clientKey,
            @Value("${midtrans.is-production}") boolean isProduction
    ) {
        Config config = Config.builder()
                .setServerKey(serverKey)
                .setClientKey(clientKey)
                .setIsProduction(isProduction)
                .build();

        this.snapApi = new ConfigFactory(config).getSnapApi();
    }

    public SnapTransactionResult createSnapTransaction(
            String orderId,
            double grossAmount,
            String firstName,
            String email
    ) throws MidtransError {

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", orderId);
        transactionDetails.put("gross_amount", grossAmount);
        params.put("transaction_details", transactionDetails);

        Map<String, String> customerDetails = new HashMap<>();
        customerDetails.put("first_name", firstName);
        customerDetails.put("email", email);
        params.put("customer_details", customerDetails);

        JSONObject response = snapApi.createTransaction(params);

        String token = response.getString("token");
        String redirectUrl = response.getString("redirect_url");

        return new SnapTransactionResult(token, redirectUrl);
    }
}



