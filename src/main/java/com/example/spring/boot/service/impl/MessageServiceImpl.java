package com.example.spring.boot.service.impl;

import com.example.spring.boot.domain.CaptchaMessage;
import com.example.spring.boot.domain.Message;
import com.example.spring.boot.domain.QCaptchaMessage;
import com.example.spring.boot.repository.MessageRepository;
import com.example.spring.boot.repository.MessageSpecification;
import com.example.spring.boot.service.MessageException;
import com.example.spring.boot.service.MessageService;
import com.example.spring.boot.service.SendMessageTooManyTimesException;
import com.example.spring.boot.service.SendMessageWithinIntervalException;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by pc on 2016/7/2.
 */
@Service
public class MessageServiceImpl implements MessageService {
    private int minIntervalInMinutes;

    private MessageRepository messageRepository;

    private int maxCountPerIpInInterval;

    @Override
    public void send(Message message) throws MessageException {
        if (CaptchaMessage.class.isAssignableFrom(message.getClass())) {
            sendCaptchaMessage((CaptchaMessage) message);
        }

    }


    private void sendCaptchaMessage(CaptchaMessage message) throws MessageException {
        Date fromDate = DateUtils.addMinutes(message.getCreationDate(), -minIntervalInMinutes);
        List<CaptchaMessage> messages = findCaptchaMessagesByPhoneAfter(message.getPhone(), fromDate);
        if (!messages.isEmpty()) {
            throw new SendMessageWithinIntervalException(String.format("send captcha message within %s minutes", minIntervalInMinutes));
        }

        messages = findCaptchaMessagesByIpAfter(message.getFromIp(), fromDate);
        if (messages.size() > maxCountPerIpInInterval) {
            throw new SendMessageTooManyTimesException(String.format("send captcha message too many time with the same ip %s within %s minutes", message.getFromIp(), minIntervalInMinutes));
        }
        messageRepository.save(message);
    }

    private List<CaptchaMessage> findCaptchaMessagesByIpAfter(String fromIp, Date fromDate) {
        QCaptchaMessage message = QCaptchaMessage.captchaMessage;
        Iterable<CaptchaMessage> messages = messageRepository.findAll(message.fromIp.eq(fromIp).and(message.creationDate.after(fromDate)));
        return Lists.newArrayList(messages);
    }

    private List<CaptchaMessage> findCaptchaMessagesByPhoneAfter(String phone, Date fromDate) {
        QCaptchaMessage message = QCaptchaMessage.captchaMessage;
        Iterable<CaptchaMessage> messages = messageRepository.findAll(message.phone.eq(phone).and(message.creationDate.after(fromDate)));
        return Lists.newArrayList(messages);
    }

    @Resource
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void setMinIntervalInMinutes(int minIntervalInMinutes) {
        this.minIntervalInMinutes = minIntervalInMinutes;
    }

    public void setMaxCountPerIpInInterval(int maxCountPerIpInInterval) {
        this.maxCountPerIpInInterval = maxCountPerIpInInterval;
    }
}
