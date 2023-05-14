package com.noslen.messageservice.service;

import com.noslen.messageservice.model.Msg;
import com.noslen.messageservice.repository.MsgRepo;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
@Service
public class MsgService {

    private final ApplicationEventPublisher publisher;
    private final MsgRepo msgRepository;

    MsgService(ApplicationEventPublisher publisher, MsgRepo msgRepository) {
        this.publisher = publisher;
        this.msgRepository = msgRepository;
    }

    public Flux<Msg> all() {
        return this.msgRepository.findAll();
    }

    public Publisher<Msg> get(String id) {
        return this.msgRepository.findById(id);
    }

    public Flux<Msg> getBySender(String sender) {
        return this.msgRepository.findBySender(sender);
    }

    public Flux<Msg> getByRecipient(String recipient) {
        return this.msgRepository.findByRecipient(recipient);
    }

    public Flux<Msg> getBySenderAndRecipient(String senderId, String recipientId) {
        return this.msgRepository.findBySenderAndRecipient(senderId, recipientId);
    }
    public Mono<Msg> delete(String id) {
        return this.msgRepository
                .findById(id)
                .flatMap(p -> this.msgRepository.deleteById(p.getId()).thenReturn(p));
    }

    public Mono<Msg> create(String senderId,String recipientId, String msg) {
        final String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        return this.msgRepository
                .save(new Msg(null, senderId,recipientId,msg,time))
                .doOnSuccess(message -> this.publisher.publishEvent(new MsgCreatedEvent(message)));
    }
}