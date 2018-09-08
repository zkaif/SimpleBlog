package com.zhoukaifan.simpleblog.controller;

import com.zhoukaifan.simpleblog.domain.BlogContent;
import com.zhoukaifan.simpleblog.service.IBlogContentService;
import com.zhoukaifan.simpleblog.utils.StrogeUtils;
import com.zhoukaifan.simpleblog.vo.Message;
import com.zhoukaifan.simpleblog.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhoukaifan on 17-9-2.
 */
@RestController
@RequestMapping("blogContent")
public class BlogContentController {
    @Autowired
    private IBlogContentService blogContentService;

    @RequestMapping("list")
    public Message list(BlogContent blogContent,int pageSize,int page) {
        Message message = new Message();
        Page<BlogContent> blogContentPage = blogContentService.getBlogContentList(
                blogContent,new Page<BlogContent>(pageSize,page));
        message.add(blogContentPage);
        message.add(StrogeUtils.readerCountMap);
        return message;
    }

    @RequestMapping("getById/{id}")
    public Message getById(String id) {
        Message message = new Message();
        BlogContent blogContent = blogContentService.getBlogContentById(id);
        message.add(blogContent);
        return message;
    }

    @RequestMapping("addReaderCount/{id}")
    public Message addReaderCount(@PathVariable("id") String id) {
        Message message = new Message();
        blogContentService.addReaderCount(id);
        return message;
    }

    @RequestMapping("getReaderCount/{id}")
    public Message getReaderCount(@PathVariable("id") String id) {
        Message message = new Message();
        long readerCount = blogContentService.getReaderCount(id);
        message.add(readerCount);
        return message;
    }

}