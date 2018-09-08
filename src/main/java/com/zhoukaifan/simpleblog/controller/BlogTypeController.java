package com.zhoukaifan.simpleblog.controller;

import com.zhoukaifan.simpleblog.service.IBlogContentService;
import com.zhoukaifan.simpleblog.vo.BlogType;
import com.zhoukaifan.simpleblog.vo.Message;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Created by zhoukaifan on 17-9-4.
 */
@RestController
@RequestMapping("blogType")
public class BlogTypeController {
    @Autowired
    private IBlogContentService blogContentService;

    @RequestMapping("list")
    public Message getBlogTypeList(){
        Message message = new Message();
        Collection<BlogType> blogTypes = blogContentService.getBlogTypeList();
        message.add(blogTypes);
        return message;
    }
}
