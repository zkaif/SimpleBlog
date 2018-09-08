package com.zhoukaifan.simpleblog.service;

import com.zhoukaifan.simpleblog.domain.ContactMessage; /**
 * Created with IntelliJ IDEA. User: ZhouKaifan Date:2018/8/28 Time:下午10:27
 */
public interface IContactMessageService {

    void sendContactMessage(ContactMessage contactMessage);
}
