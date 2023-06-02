package com.noslen.emailservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailObject implements Serializable {
    @Id
    private String id;
    @Email
    @NotNull
    @Size(min = 1, message = "Please, set an email address to send the message to it")
    private String email;
    private String recipientName;
    private String subject;
    private String text;
    private String senderName;
}
