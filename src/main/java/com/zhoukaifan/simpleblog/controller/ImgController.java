package com.zhoukaifan.simpleblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA. User: ZhouKaifan Date:2018/9/7 Time:下午4:15
 */
@Controller
@RequestMapping("img")
public class ImgController {
    @RequestMapping("{file}")
    public String img(@PathVariable String file){
        return "redirect:https://raw.githubusercontent.com/zkaif/zkaif.github.io/master/img/"+file;
    }
}
