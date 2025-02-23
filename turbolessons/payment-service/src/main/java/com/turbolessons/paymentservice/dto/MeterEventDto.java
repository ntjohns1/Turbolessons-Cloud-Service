package com.turbolessons.paymentservice.dto;

public record MeterEventDto(String identifier, String eventName, String stripeCustomerId, String value) {
}
