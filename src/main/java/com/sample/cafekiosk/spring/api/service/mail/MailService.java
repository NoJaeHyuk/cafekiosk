package com.sample.cafekiosk.spring.api.service.mail;

import com.sample.cafekiosk.spring.client.mail.MailSendClient;
import com.sample.cafekiosk.spring.domain.history.MailSendHistory;
import com.sample.cafekiosk.spring.domain.history.MailSendHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {

    private final MailSendClient mailSendClient;
    private final MailSendHistoryRepository mailSendHistoryRepository;

    public boolean sendMail(String fromEmail, String toEmail, String subject, String content) {
        boolean result = mailSendClient.sendMail(fromEmail, toEmail, subject, content);
        if (result) {
            mailSendHistoryRepository.save(MailSendHistory.builder()
                    .fromEmail(fromEmail)
                    .toEmail(toEmail)
                    .subject(subject)
                    .content(content)
                    .build()
            );

            mailSendClient.a();
            mailSendClient.b();
            mailSendClient.c();

            return true;
        }

        return false;
    }
}
