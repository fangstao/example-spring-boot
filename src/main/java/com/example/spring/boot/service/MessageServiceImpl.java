package com.example.spring.boot.service;

import com.example.spring.boot.domain.CaptchaMessage;
import com.example.spring.boot.domain.Message;
import com.example.spring.boot.repository.MessageRepository;
import org.apache.commons.lang3.time.DateUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by pc on 2016/7/2.
 */
public class MessageServiceImpl implements MessageService {
    private int minIntervalInMinutes;

    private MessageRepository messageRepository;

    @Override
    public void send(Message message) throws MessageException {
        if (CaptchaMessage.class.isAssignableFrom(message.getClass())) {
            sendCaptchaMessage((CaptchaMessage) message);
        }

    }


    private void sendCaptchaMessage(CaptchaMessage message) {
        Date fromDate = DateUtils.addMinutes(message.getCreationDate(), -minIntervalInMinutes);
        MessageSepcification spec =
                MessageSpecification.create()
                        .phoneEq(message.getPhone())
                        .creationDateAfter(fromDate);
        List<CaptchaMessage> messages = messageRepository.findBySpecifiation(spec);
        messageRepository.save(message);
    }

    @Resource
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
}
