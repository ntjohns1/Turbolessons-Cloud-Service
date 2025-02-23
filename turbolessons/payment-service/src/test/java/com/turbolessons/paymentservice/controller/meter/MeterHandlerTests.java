package com.turbolessons.paymentservice.controller.meter;

import com.stripe.model.StripeCollection;
import com.stripe.model.billing.Meter;
import com.stripe.model.billing.MeterEvent;
import com.turbolessons.paymentservice.service.meter.MeterService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class MeterHandlerTests {

    @MockBean
    private MeterService meterService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MeterHandlerImpl meterHandler;

    @BeforeEach
    public void setUp() {
        RouterFunction<ServerResponse> routerFunction = new MeterEndpointConfig(meterHandler).meterRoutes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    private StripeCollection<Meter> createMockStripeCollection() {
        StripeCollection<Meter> meters = new StripeCollection<>();

        Meter mockMeter1 = createMockMeter("mtr_123",
                                           "30 Minute Lesson",
                                           "lesson_30");

        Meter mockMeter2 = createMockMeter("mtr_234",
                                           "60 Minute Lesson",
                                           "lesson_60");

        List<Meter> meterList = Arrays.asList(mockMeter1,
                                              mockMeter2);
        meters.setData(meterList);
        return meters;
    }

    private Meter createMockMeter(String id, String displayName, String eventName) {
        Meter meter = Mockito.mock(Meter.class);
        when(meter.getId()).thenReturn(id);
        when(meter.getDisplayName()).thenReturn(displayName);
        when(meter.getEventName()).thenReturn(eventName);
        return meter;
    }

    private MeterEvent createMockMeterEvent(String identifier, String eventName, String stripeCustomerId, String value) {
        MeterEvent meterEvent = Mockito.mock(MeterEvent.class);
        when(meterEvent.getIdentifier()).thenReturn(identifier);
        when(meterEvent.getEventName()).thenReturn(eventName);
        when(meterEvent.getPayload()
                     .get("stripe_customer_id")).thenReturn(stripeCustomerId);
        when(meterEvent.getPayload()
                     .get("value")).thenReturn(value);
        return meterEvent;
    }
}
