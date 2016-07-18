package com.example.spring.boot;

import com.example.spring.boot.domain.Message;
import com.example.spring.boot.domain.QCaptchaMessage;
import com.example.spring.boot.repository.MessageRepository;
import com.example.spring.boot.repository.MessageSpecification;
import com.example.spring.boot.domain.CaptchaMessage;
import com.example.spring.boot.service.impl.MessageServiceImpl;
import com.example.spring.boot.service.SendMessageTooManyTimesException;
import com.example.spring.boot.service.SendMessageWithinIntervalException;
import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by pc on 2016/7/2.
 */
public class MessageServiceTest {

    MessageServiceImpl messageService;
    CaptchaMessage message;
    MessageRepository messageRepository;
    String mobile;
    String withinIntervalMobile;
    String ip = "10.1.2.33";
    String invalidIp = "10.1.2.32";
    private Date creationDate;
    private int sendMessageIntervalInMinutes = 1;

    @Before
    public void setUp() throws Exception {
        mobile = "15800826960";
        withinIntervalMobile = "15921508206";
        messageService = new MessageServiceImpl();
        messageService.setMinIntervalInMinutes(1);
        messageService.setMaxCountPerIpInInterval(2);
        message = new CaptchaMessage();
        message.setContent("your register captcha is 123456.");
        message.setFromIp(ip);
        message.setSignature("microsoft");
        creationDate = new Date();
        message.setCreationDate(creationDate);
        messageRepository = createMessageRepository();
        messageService.setMessageRepository(messageRepository);
        messageService.setMinIntervalInMinutes(sendMessageIntervalInMinutes);
    }

    private MessageRepository createMessageRepository() {
        MessageRepository messageRepository = mock(MessageRepository.class);
        QCaptchaMessage message = QCaptchaMessage.captchaMessage;
        Date fromDate = DateUtils.addMinutes(creationDate, -sendMessageIntervalInMinutes);

        BooleanExpression phoneAndFromDate = message.phone.eq(mobile).and(message.creationDate.after(fromDate));
        when(messageRepository.findAll(phoneAndFromDate)).thenReturn(Lists.newArrayList());

        BooleanExpression fromIpAndDateExpression = message.fromIp.eq(ip).and(message.creationDate.after(fromDate));
        when(messageRepository.findAll(fromIpAndDateExpression)).thenReturn(Lists.newArrayList());

        BooleanExpression phoneAndFromDateWithinInterval = message.phone.eq(withinIntervalMobile).and(message.creationDate.after(fromDate));
        when(messageRepository.findAll(phoneAndFromDateWithinInterval)).thenReturn(Lists.newArrayList(new CaptchaMessage()));

        BooleanExpression fromIpAndDateInInterval = message.fromIp.eq(invalidIp).and(message.creationDate.after(fromDate));
        when(messageRepository.findAll(fromIpAndDateInInterval)).thenReturn(Lists.newArrayList(new CaptchaMessage(), new CaptchaMessage(), new CaptchaMessage()));

        return messageRepository;
    }


    @Test
    public void sendCaptchaMessageSuccess() throws Exception {
        message.setPhone(mobile);
        message.setFromIp(ip);
        messageService.send(message);
    }

    @Test(expected = SendMessageWithinIntervalException.class)
    public void sendCaptchaMessageWithinMinIntervalException() throws Exception {
        message.setPhone(withinIntervalMobile);
        message.setFromIp(ip);
        messageService.send(message);
    }

    @Test(expected = SendMessageTooManyTimesException.class)
    public void sendCaptchaMessageTooTooManyTimesWithTheSameIp() throws Exception {
        message.setPhone(mobile);
        message.setFromIp(invalidIp);
        messageService.send(message);

    }

}
