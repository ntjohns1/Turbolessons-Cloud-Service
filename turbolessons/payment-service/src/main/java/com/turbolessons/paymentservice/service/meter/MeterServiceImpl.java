package com.turbolessons.paymentservice.service.meter;

import com.stripe.StripeClient;
import com.stripe.model.UsageRecord;
import com.stripe.param.UsageRecordCreateParams;
import com.turbolessons.paymentservice.dto.MeterEventDto;
import com.turbolessons.paymentservice.service.StripeClientHelper;
import reactor.core.publisher.Mono;

public class MeterServiceImpl implements MeterService {

    private final StripeClient stripeClient;

    private final StripeClientHelper stripeClientHelper;

    public MeterServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    private MeterEventDto mapMeterEventToDto(UsageRecord usageRecord) {
        return new MeterEventDto(usageRecord.getTimestamp(),
                                 usageRecord.getSubscriptionItem());
    }

    @Override
    public Mono<MeterEventDto> createUsageRecord(MeterEventDto meterEventDto) {
        UsageRecordCreateParams params = UsageRecordCreateParams.builder()
                .setQuantity(1L) // Increment by 1 lesson
                .setTimestamp(meterEventDto.timestamp()) // Use provided timestamp
                .setAction(UsageRecordCreateParams.Action.INCREMENT) // Ensure correct action
                .build();
        return stripeClientHelper.executeStripeCall(() -> stripeClient.subscriptionItems()
                        .usageRecords()
                        .create(meterEventDto.subscriptionItem(),
                                params))
                .map(this::mapMeterEventToDto);
    }
}
