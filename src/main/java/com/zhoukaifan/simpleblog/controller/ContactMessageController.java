package com.zhoukaifan.simpleblog.controller;

import com.zhoukaifan.simpleblog.domain.ContactMessage;
import com.zhoukaifan.simpleblog.service.IContactMessageService;
import com.zhoukaifan.simpleblog.vo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhoukaifan on 17-9-2.
 */
@RestController
@RequestMapping("contactMessage")
public class ContactMessageController {
    @Autowired
    IContactMessageService contactMessageService;

    @RequestMapping("create")
    public Message create(@Validated ContactMessage contactMessage, BindingResult bindingResult){
        Message message = new Message();
        if (bindingResult.hasErrors()) {
            message.setCode(Message.Code.ERROR.ordinal());
            message.setDes(bindingResult.getFieldError().getDefaultMessage());
            return message;
        }
        if(contactMessage == null){
            message.setCode(Message.Code.ERROR.ordinal());
            message.setDes("contactMessage is null");
            return message;
        }
        contactMessageService.sendContactMessage(contactMessage);
        return message;
    }
}
