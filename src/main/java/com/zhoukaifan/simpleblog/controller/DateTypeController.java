package com.zhoukaifan.simpleblog.controller;

import com.zhoukaifan.simpleblog.service.IBlogContentService;
import com.zhoukaifan.simpleblog.vo.DateType;
import com.zhoukaifan.simpleblog.vo.Message;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhoukaifan on 17-9-4.
 */
@RestController
@RequestMapping("dateType")
public class DateTypeController {
    @Autowired
    private IBlogContentService blogContentService;

    @RequestMapping("list")
    public Message getDateTypeList(){
        Message message = new Message();
        Collection<DateType> dateTypes = blogContentService.getDateTypeList();
        message.add(dateTypes);
        return message;
    }
}
