package com.sample.cafekiosk.spring.api.service.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.sample.cafekiosk.spring.client.mail.MailSendClient;
import com.sample.cafekiosk.spring.domain.history.MailSendHistory;
import com.sample.cafekiosk.spring.domain.history.MailSendHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    //@Spy
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService;

    @DisplayName("메일 전송 테스트를 진행한다.")
    @Test
    void sendMail() {
        // given
        // Mock 문법
        when(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(true);

        // Spy 문법
        /*doReturn(true)
            .when(mailSendClient)
            .sendMail(anyString(), anyString(), anyString(), anyString());*/

        // when
        boolean result = mailService.sendMail("", "", "", "");
        Mockito.verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));

        // then
        assertThat(result).isTrue();
    }

}