package com.zhoukaifan.simpleblog.controller;

import com.zhoukaifan.simpleblog.domain.Comment;
import com.zhoukaifan.simpleblog.service.IBlogContentService;
import com.zhoukaifan.simpleblog.vo.Message;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dim on 17-9-15.
 */
@RestController
@RequestMapping("comment")
public class CommentController {
    @Autowired
    private IBlogContentService blogContentService;

    @RequestMapping("create")
    public Message create(@Validated Comment comment, BindingResult bindingResult){
        Message message = new Message();
        if (bindingResult.hasErrors()) {
            message.setCode(Message.Code.ERROR.ordinal());
            message.setDes(bindingResult.getFieldError().getDefaultMessage());
            return message;
        }
        if(comment == null){
            message.setCode(Message.Code.ERROR.ordinal());
            message.setDes("contactMessage is null");
            return message;
        }
        blogContentService.createComment(comment);
        return message;
    }

    @RequestMapping("listByBlogId/{blogId}")
    public Message listByBlogId(@PathVariable("blogId") String blogId){
        Message message = new Message();
        List<Comment> comments = blogContentService.getCommentListByBlogId(blogId);
        message.add(comments);
        return message;
    }

    @RequestMapping("deletesById/{id}")
    public Message deleteById(@RequestParam String key,@PathVariable("id") String id,@RequestParam List<String> ids){
        Message message = new Message();
        boolean b = blogContentService.deletesCommentById(key,id,ids);
        if (!b) {
            message.setCode(Message.Code.ERROR.ordinal());
        }
        return message;
    }
}
