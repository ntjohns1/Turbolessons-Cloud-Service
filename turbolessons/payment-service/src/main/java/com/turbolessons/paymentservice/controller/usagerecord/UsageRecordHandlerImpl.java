package com.turbolessons.paymentservice.controller.usagerecord;

import com.turbolessons.paymentservice.controller.BaseHandler;
import com.turbolessons.paymentservice.dto.UsageRecordDto;
import com.turbolessons.paymentservice.service.usagerecord.UsageRecordService;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class UsageRecordHandlerImpl extends BaseHandler implements UsageRecordHandler {

    private final UsageRecordService usageRecordService;

    public UsageRecordHandlerImpl(UsageRecordService usageRecordService) {
        this.usageRecordService = usageRecordService;
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        return handleCreate(r,
                            requestBody -> requestBody.flatMap(this.usageRecordService::createUsageRecord),
                            UsageRecordDto.class,
                            UsageRecordDto.class);
    }
}
