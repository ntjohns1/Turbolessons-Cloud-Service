package com.turbolessons.paymentservice.service.meter;

import com.turbolessons.paymentservice.dto.MeterEventDto;
import reactor.core.publisher.Mono;

public interface MeterService {
    Mono<MeterEventDto> createUsageRecord(MeterEventDto meterEventDto);
}
