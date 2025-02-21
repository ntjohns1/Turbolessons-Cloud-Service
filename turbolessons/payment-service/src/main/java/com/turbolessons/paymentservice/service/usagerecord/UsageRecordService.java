package com.turbolessons.paymentservice.service.usagerecord;

import com.turbolessons.paymentservice.dto.UsageRecordDto;
import reactor.core.publisher.Mono;

public interface UsageRecordService {
    Mono<UsageRecordDto> createUsageRecord(UsageRecordDto usageRecordDto);
}
