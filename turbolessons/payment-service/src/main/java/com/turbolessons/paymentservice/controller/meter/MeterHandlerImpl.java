package com.turbolessons.paymentservice.controller.meter;

import com.turbolessons.paymentservice.controller.BaseHandler;
import com.turbolessons.paymentservice.dto.MeterEventDto;
import com.turbolessons.paymentservice.service.meter.MeterService;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class MeterHandlerImpl extends BaseHandler implements MeterHandler {

    private final MeterService meterService;

    public MeterHandlerImpl(MeterService meterService) {
        this.meterService = meterService;
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        return handleCreate(r,
                            requestBody -> requestBody.flatMap(this.meterService::createUsageRecord),
                            MeterEventDto.class,
                            MeterEventDto.class);
    }
}
