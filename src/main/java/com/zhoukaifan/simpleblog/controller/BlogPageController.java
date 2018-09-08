package com.zhoukaifan.simpleblog.controller;

import com.zhoukaifan.simpleblog.domain.BlogContent;
import com.zhoukaifan.simpleblog.service.IBlogContentService;
import com.zhoukaifan.simpleblog.utils.StrogeUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by dim on 17-10-15.
 */
@Controller
public class BlogPageController {
    @Autowired
    private IBlogContentService blogContentService;

    @RequestMapping("blog/{id}.html")
    public ModelAndView blogPage(HttpServletResponse httpServletResponse,HttpServletRequest httpServletRequest,@PathVariable("id") String id) {
        BlogContent blogContent = StrogeUtils.fileNameMap.get(id);
        String content = StrogeUtils.contentMap.get(id);
        ModelAndView modelAndView= new ModelAndView();
        if (blogContent==null||content==null){
            httpServletResponse.setStatus(404);
            modelAndView.addObject("status",404);
            modelAndView.addObject("error","Not Found");
            modelAndView.addObject("path",httpServletRequest.getServletPath());
            modelAndView.setViewName("error");
            return modelAndView;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        modelAndView.addObject("id", id);
        modelAndView.addObject("title", blogContent.getTitle());
        modelAndView.addObject("digest", blogContent.getDigest());
        modelAndView.addObject("content", content);
        modelAndView.addObject("type", blogContent.getType());
        modelAndView.addObject("group", blogContent.getGroup());
        modelAndView.addObject("dateStr", blogContent.getDateStr());
        modelAndView.addObject("date", simpleDateFormat.format(blogContent.getDate()));
        modelAndView.addObject("readCount", blogContentService.getReaderCount(id));
        modelAndView.setViewName("blogSingle");
        return modelAndView;
    }
}
