package com.noslen.emailservice.dao;

import com.noslen.emailservice.dto.MailObject;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface EmailObjectRepo extends ReactiveMongoRepository<MailObject, String> {
}
