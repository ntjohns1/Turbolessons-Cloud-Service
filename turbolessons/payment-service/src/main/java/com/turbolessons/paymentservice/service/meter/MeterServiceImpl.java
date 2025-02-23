package com.turbolessons.paymentservice.service.meter;

import com.stripe.StripeClient;
import com.stripe.model.StripeCollection;
import com.stripe.model.billing.Meter;
import com.stripe.model.billing.MeterEvent;
import com.stripe.param.billing.MeterCreateParams;
import com.stripe.param.billing.MeterEventCreateParams;
import com.stripe.param.billing.MeterUpdateParams;
import com.turbolessons.paymentservice.dto.MeterDto;
import com.turbolessons.paymentservice.dto.MeterEventDto;
import com.turbolessons.paymentservice.service.StripeClientHelper;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

public class MeterServiceImpl implements MeterService {

    private final StripeClient stripeClient;

    private final StripeClientHelper stripeClientHelper;

    public MeterServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    private MeterEventDto mapMeterEventToDto(MeterEvent meterEvent) {
        return new MeterEventDto(meterEvent.getIdentifier(),
                                 meterEvent.getEventName(),
                                 meterEvent.getPayload()
                                         .get("stripe_customer_id"),
                                 meterEvent.getPayload()
                                         .get("value"));
    }

    private MeterDto mapMeterToDto(Meter meter) {
        return new MeterDto(meter.getId(),
                            meter.getDisplayName(),
                            meter.getEventName());
    }

    @Override
    public Mono<StripeCollection<Meter>> listAllMeters() {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.billing()
                .meters()
                .list());
    }

    @Override
    public Mono<MeterDto> retrieveMeter(String id) {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.billing()
                        .meters()
                        .retrieve(id))
                .map(this::mapMeterToDto);
    }

    @Override
    public Mono<MeterDto> createMeter(MeterDto meterDto) {
        MeterCreateParams params = MeterCreateParams.builder()
                .setDisplayName(meterDto.displayName())
                .setEventName(meterDto.eventName())
                .setDefaultAggregation(MeterCreateParams.DefaultAggregation.builder()
                                               .setFormula(MeterCreateParams.DefaultAggregation.Formula.COUNT)
                                               .build())
                .setValueSettings(MeterCreateParams.ValueSettings.builder()
                                          .setEventPayloadKey("value")
                                          .build())
                .setCustomerMapping(MeterCreateParams.CustomerMapping.builder()
                                            .setType(MeterCreateParams.CustomerMapping.Type.BY_ID)
                                            .setEventPayloadKey("stripe_customer_id")
                                            .build())
                .build();
        return stripeClientHelper.executeStripeCall(() -> stripeClient.billing()
                        .meters()
                        .create(params))
                .map(this::mapMeterToDto);
    }

    @Override
    public Mono<Void> updateMeter(String id, MeterDto meterDto) {
        MeterUpdateParams params = MeterUpdateParams.builder()
                .setDisplayName(meterDto.displayName())
                .build();

        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.billing()
                .meters()
                .update(id,
                        params));
    }

    @Override
    public Mono<MeterDto> deactivateMeter(String id) {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.billing()
                        .meters()
                        .reactivate(id))
                .map(this::mapMeterToDto);
    }

    @Override
    public Mono<MeterDto> reactivateMeter(String id) {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.billing()
                        .meters()
                        .reactivate(id))
                .map(this::mapMeterToDto);
    }

    //    *** Create Meter Event ***
    @Override
    public Mono<MeterEventDto> createMeterEvent(MeterEventDto meterEventDto) {
        MeterEventCreateParams params = MeterEventCreateParams.builder()
                .setEventName(meterEventDto.eventName())
                .putPayload("value",
                            meterEventDto.value())
                .putPayload("stripe_customer_id",
                            meterEventDto.stripeCustomerId())
                .setIdentifier(meterEventDto.identifier())
                .build();
        System.out.println("Meter service params:" + params);
        return stripeClientHelper.executeStripeCall(() -> stripeClient.billing()
                        .meterEvents()
                        .create(params))
                .map(this::mapMeterEventToDto);
    }
}
