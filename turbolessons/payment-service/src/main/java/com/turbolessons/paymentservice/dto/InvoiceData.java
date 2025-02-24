package com.turbolessons.paymentservice.dto;

public record InvoiceData(String id, String account_name, String customer, int amount_due, int amount_paid,
                          int amount_remaining, int attempt_count, boolean attempted, long created, long due_date,
                          long effective_at, long ending_balance, long next_payment_attempt, boolean paid) {
}
