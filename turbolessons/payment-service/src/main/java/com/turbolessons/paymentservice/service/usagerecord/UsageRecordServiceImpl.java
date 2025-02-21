package com.turbolessons.paymentservice.service.usagerecord;

import com.stripe.StripeClient;
import com.stripe.model.UsageRecord;
import com.stripe.param.UsageRecordCreateParams;
import com.turbolessons.paymentservice.dto.UsageRecordDto;
import com.turbolessons.paymentservice.service.StripeClientHelper;
import reactor.core.publisher.Mono;

public class UsageRecordServiceImpl implements UsageRecordService {

    private final StripeClient stripeClient;

    private final StripeClientHelper stripeClientHelper;

    public UsageRecordServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    private UsageRecordDto mapUsageRecordToDto(UsageRecord usageRecord) {
        return new UsageRecordDto(usageRecord.getTimestamp(),
                                  usageRecord.getSubscriptionItem());
    }

    @Override
    public Mono<UsageRecordDto> createUsageRecord(UsageRecordDto usageRecordDto) {
        UsageRecordCreateParams params = UsageRecordCreateParams.builder()
                .setQuantity(1L) // Increment by 1 lesson
                .setTimestamp(usageRecordDto.timestamp()) // Use provided timestamp
                .setAction(UsageRecordCreateParams.Action.INCREMENT) // Ensure correct action
                .build();
        return stripeClientHelper.executeStripeCall(() -> stripeClient.subscriptionItems()
                        .usageRecords()
                        .create(usageRecordDto.subscriptionItem(),
                                params))
                .map(this::mapUsageRecordToDto);
    }
}
