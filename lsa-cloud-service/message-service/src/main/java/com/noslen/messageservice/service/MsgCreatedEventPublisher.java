package com.noslen.messageservice.service;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
public class MsgCreatedEventPublisher implements
        ApplicationListener<MsgCreatedEvent>,
        Consumer<FluxSink<MsgCreatedEvent>> {

    private final Executor executor;
    private final BlockingQueue<MsgCreatedEvent> queue =
            new LinkedBlockingQueue<>();

    MsgCreatedEventPublisher(Executor executor) {
        this.executor = executor;
    }


    @Override
    public void onApplicationEvent(MsgCreatedEvent event) {
        this.queue.offer(event);
    }

    @Override
    public void accept(FluxSink<MsgCreatedEvent> sink) {
        this.executor.execute(() -> {
            while (true)
                try {
                    MsgCreatedEvent event = queue.take();
                    sink.next(event);
                }
                catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }
}
