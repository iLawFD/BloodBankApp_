package com.bloodbankapp.bloodbankapp.database;

import models.SendEnhancedRequestBody;
import models.SendEnhancedResponseBody;
import models.SendRequestMessage;
import services.Courier;
import services.SendService;

import java.io.IOException;
import java.util.HashMap;

public class EmailSender {
    private final static EmailSender emailSender = new EmailSender();
    private EmailSender() {
        Courier.init("dk_prod_F0EWAR5M1AMJC8Q73KRCFNFRJBNZ");
    }

    public void SendMessage(String email, String title, String messageContent ) throws IOException {
        SendEnhancedRequestBody request = new SendEnhancedRequestBody();
        SendRequestMessage message = new SendRequestMessage();
        HashMap<String, String> to = new HashMap<String, String>();
        to.put("email", email);
        message.setTo(to);

        HashMap<String, Object> content = new HashMap<String, Object>();
        content.put("title", title);
        content.put("body", messageContent);
        message.setContent(content);

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("name", "BloodBankSystem,");
        message.setData(data);

        request.setMessage(message);
        SendEnhancedResponseBody response = new SendService().sendEnhancedMessage(request);
        System.out.println(response);

    }

    public static EmailSender getEmailSender() {
        return emailSender;
    }

    public static void main(String[] args) {
        try {
            EmailSender.getEmailSender().SendMessage(
                    "ayednfqht@gmail.com",
                    "demo",
                    "hello aha md one person wants to donation you"
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
