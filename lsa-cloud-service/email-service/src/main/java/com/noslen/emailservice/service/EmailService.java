package com.noslen.emailservice.service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

public interface EmailService {
    void sendHTMLMessage(String to,
                                           String subject,
                                           Map<String, Object> templateModel) 
            throws IOException, MessagingException;

}
