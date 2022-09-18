package com.noslen.emailservice.dao;

import com.noslen.emailservice.dto.MailObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailObjectRepo extends JpaRepository<MailObject, Integer> {
}
