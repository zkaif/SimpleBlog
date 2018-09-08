package com.zhoukaifan.simpleblog.controller;

import com.zhoukaifan.simpleblog.domain.BlogContent;
import com.zhoukaifan.simpleblog.utils.StrogeUtils;
import com.zhoukaifan.simpleblog.vo.SitemapsXml;
import com.zhoukaifan.simpleblog.vo.SitemapsXml.SitemapsUrl;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA. User: ZhouKaifan Date:2018/9/6 Time:下午6:49
 */
@RestController
public class RobotsController {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @RequestMapping(value = "sitemaps.xml",produces = {"application/xml"})
    public SitemapsXml sitemaps(HttpServletResponse response){
        SitemapsXml sitemaps = new SitemapsXml();
        for (BlogContent blogContent: StrogeUtils.fileNameMap.values()) {
            sitemaps.getUrl().add(new SitemapsUrl("https://www.zhoukaifan.com/blog/"+blogContent.getId()+".html",
                    "pc,mobile",simpleDateFormat.format(blogContent.getDate()),"weekly","0.8"));
        }

        response.setHeader("Accept-Ranges","bytes");
        response.setHeader("Cache-Control","private");

        return sitemaps;
    }
}
