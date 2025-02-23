package com.turbolessons.paymentservice.service.meter;

import com.stripe.model.StripeCollection;
import com.stripe.model.billing.Meter;
import com.turbolessons.paymentservice.dto.MeterDto;
import com.turbolessons.paymentservice.dto.MeterEventDto;
import reactor.core.publisher.Mono;
import com.stripe.model.billing.MeterCollection;

public interface MeterService {
//    Mono<MeterEventDto> createUsageRecord(MeterEventDto meterEventDto);
    Mono<StripeCollection<Meter>> listAllMeters();
    Mono<MeterDto> retrieveMeter(String id);
    Mono<MeterDto> createMeter(MeterDto meterDto);
    Mono<Void> updateMeter(String id, MeterDto meterDto);
    Mono<MeterDto> deactivateMeter(String id);
    Mono<MeterDto> reactivateMeter(String id);
    Mono<MeterEventDto> createMeterEvent(MeterEventDto meterEventDto);

}
