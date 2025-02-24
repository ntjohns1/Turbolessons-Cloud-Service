package com.turbolessons.paymentservice.dto;

import java.util.Map;

public record LineItemData(String id, long amount, String description, Map<String, Long> period, String priceId, String invoiceId) {
}
