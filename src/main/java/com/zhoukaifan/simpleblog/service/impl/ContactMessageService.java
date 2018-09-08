package com.zhoukaifan.simpleblog.service.impl;

import com.zhoukaifan.simpleblog.domain.ContactMessage;
import com.zhoukaifan.simpleblog.service.IContactMessageService;
import com.zhoukaifan.simpleblog.utils.MailSendWorker;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA. User: ZhouKaifan Date:2018/8/28 Time:下午10:27
 */
@Service
public class ContactMessageService implements IContactMessageService{

    @Override
    public void sendContactMessage(ContactMessage contactMessage) {
        if (contactMessage==null){
            return;
        }
        MailSendWorker.contactMessageQueue.offer(contactMessage);
    }
}
