package com.bioskuy.api.model.midtrans;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class SnapTransactionResult {
    private final String token;
    private final String redirectUrl;
}
