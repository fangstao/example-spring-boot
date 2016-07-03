package com.example.spring.boot;

import com.example.spring.boot.repository.MessageRepository;
import com.example.spring.boot.repository.MessageSpecification;
import com.example.spring.boot.domain.CaptchaMessage;
import com.example.spring.boot.service.MessageServiceImpl;
import com.example.spring.boot.service.SendMessageTooManyTimesException;
import com.example.spring.boot.service.SendMessageWithinIntervalException;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.util.Date;

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
        message.setCreationDate(new Date());
        messageRepository = createMessageRepository();
        messageService.setMessageRepository(messageRepository);
    }

    private MessageRepository createMessageRepository() {
        MessageRepository messageRepository = mock(MessageRepository.class);
        MessageSpecification specification1 =
                MessageSpecification.create()
                        .phoneEq(mobile)
                        .creationDateAfter(new Date());
        when(messageRepository.findBySpecifiation(argThat(new SpecificationMobileEqual(specification1)))).thenReturn(Lists.newArrayList());

        MessageSpecification specification2 =
                MessageSpecification.create()
                        .phoneEq(withinIntervalMobile)
                        .creationDateAfter(new Date());
        when(messageRepository.findBySpecifiation(argThat(new SpecificationMobileEqual(specification2)))).thenReturn(Lists.newArrayList(new CaptchaMessage()));

        MessageSpecification specification3 =
                MessageSpecification.create()
                        .fromIpEq(invalidIp)
                        .creationDateAfter(new Date());
        when(messageRepository.findBySpecifiation(argThat(new SpecificationFromIpEqual(specification3)))).thenReturn(Lists.newArrayList(new CaptchaMessage(), new CaptchaMessage(), new CaptchaMessage()));
        return messageRepository;
    }


    @Test
    public void sendCaptchaMessageSuccess() throws Exception {
        message.setPhone(mobile);
        messageService.send(message);
    }

    @Test(expected = SendMessageWithinIntervalException.class)
    public void sendCaptchaMessageWithinMinIntervalException() throws Exception {
        message.setPhone(withinIntervalMobile);
        messageService.send(message);
    }

    @Test(expected = SendMessageTooManyTimesException.class)
    public void sendCaptchaMessageTooTooManyTimesWithTheSameIp() throws Exception {
        message.setFromIp(invalidIp);
        messageService.send(message);

    }

    class SpecificationFromIpEqual extends ArgumentMatcher<MessageSpecification> {
        private MessageSpecification stub;

        @Override
        public boolean matches(Object argument) {
            MessageSpecification arg = (MessageSpecification) argument;
            return argument != null && stub.getFromIp().equals(arg.getFromIp());
        }

        public SpecificationFromIpEqual(MessageSpecification stub) {
            this.stub = stub;
        }
    }

    class SpecificationMobileEqual extends ArgumentMatcher<MessageSpecification> {
        MessageSpecification spec;

        @Override
        public boolean matches(Object argument) {
            MessageSpecification arg = (MessageSpecification) argument;
            return arg != null && spec.getPhoneEq().equals(arg.getPhoneEq());
        }

        public SpecificationMobileEqual(MessageSpecification spec) {
            this.spec = spec;
        }
    }
}
