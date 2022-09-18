package com.noslen.emailservice.controller;

import com.noslen.emailservice.dto.MailObject;
import com.noslen.emailservice.mail.EmailService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MailController {

    @Autowired
    public EmailService emailService;

    @RequestMapping(method = RequestMethod.GET)
    public String showEmailsPage() {
        return "emails";
    }

    @RequestMapping(value = {"/sendHtml"}, method = RequestMethod.GET)
    public String getHtmlMailView(Model model,
                                  ServerHttpRequest request) {

        Map<String, String> templateEngines = new HashMap<>();
        templateEngines.put("Thymeleaf", "Thymeleaf");
        templateEngines.put("Freemarker", "Freemarker");
        model.addAttribute("mailObject", new MailObject());
        model.addAttribute("templateEngines", templateEngines);
        return "mail/sendHtml";
    }

    @RequestMapping(value = "/sendHtml", method = RequestMethod.POST)
    public String createHtmlMail(Model model,
                                 @ModelAttribute("mailObject") @Valid MailObject mailObject,
                                 Errors errors) throws IOException, MessagingException, TemplateException {
        if (errors.hasErrors()) {
            return "mail/send";
        }

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", mailObject.getRecipientName());
        templateModel.put("text", mailObject.getText());
        templateModel.put("senderName", mailObject.getSenderName());
        emailService.sendMessageUsingThymeleafTemplate(
                mailObject.getTo(),
                mailObject.getSubject(),
                templateModel);


        return "redirect:/mail";
    }
}
