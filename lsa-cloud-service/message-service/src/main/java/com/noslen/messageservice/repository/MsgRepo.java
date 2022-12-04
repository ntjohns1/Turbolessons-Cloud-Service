package com.noslen.messageservice.repository;

import com.noslen.messageservice.model.Msg;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MsgRepo extends ReactiveMongoRepository<Msg, String> {
}
