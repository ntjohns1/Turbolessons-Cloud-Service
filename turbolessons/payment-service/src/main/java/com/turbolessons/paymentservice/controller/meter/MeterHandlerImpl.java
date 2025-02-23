package com.turbolessons.paymentservice.controller.meter;

import com.turbolessons.paymentservice.controller.BaseHandler;
import com.turbolessons.paymentservice.dto.MeterDto;
import com.turbolessons.paymentservice.dto.MeterEventDto;
import com.turbolessons.paymentservice.service.meter.MeterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class MeterHandlerImpl extends BaseHandler implements MeterHandler {

    private final MeterService meterService;

    public MeterHandlerImpl(MeterService meterService) {
        this.meterService = meterService;
    }

    @Override
    public Mono<ServerResponse> listAll(ServerRequest r) {
        return handleList(r,
                          request -> meterService.listAllMeters(),
                          new ParameterizedTypeReference<>() {
                          });
    }

    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {
        return handleRetrieve(r,
                              request -> meterService.retrieveMeter(id(request)),
                              MeterDto.class);
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        return handleCreate(r,
                            requestBody -> requestBody.flatMap(this.meterService::createMeter),
                            MeterDto.class,
                            MeterDto.class);
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest r) {
        String id = id(r);
        return handleUpdate(r,
                            (idParam, requestBody) -> requestBody.flatMap(dto -> meterService.updateMeter(idParam,
                                                                                                          dto)),
                            id,
                            MeterDto.class);
    }

    @Override
    public Mono<ServerResponse> deactivate(ServerRequest r) {
        return meterService.deactivateMeter(id(r))
                .then(ServerResponse.ok()
                              .build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

    @Override
    public Mono<ServerResponse> reactivate(ServerRequest r) {
        return meterService.reactivateMeter(id(r))
                .then(ServerResponse.ok()
                              .build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

    @Override
    public Mono<ServerResponse> createEvent(ServerRequest r) {
        return handleCreate(r,
                            requestBody -> requestBody.flatMap(this.meterService::createMeterEvent),
                            MeterEventDto.class,
                            MeterEventDto.class);
    }
}
