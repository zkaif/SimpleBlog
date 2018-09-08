package com.zhoukaifan.simpleblog.utils;

import com.zhoukaifan.simpleblog.domain.BlogContent;
import com.zhoukaifan.simpleblog.domain.Comment;
import com.zhoukaifan.simpleblog.vo.BlogType;
import com.zhoukaifan.simpleblog.vo.DateType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: ZhouKaifan Date:2018/8/28 Time:下午7:14
 */
public class StrogeUtils {
    public static volatile Map<String,BlogContent> fileNameMap = new HashMap<>();
    public static volatile Map<String,String> contentMap = new HashMap<>();

    public static volatile List<BlogType> blogTypes = new ArrayList<>();
    public static volatile List<DateType> dateTypes = new ArrayList<>();

    //需要落盘的数据
    public static volatile Map<String,Long> readerCountMap = new HashMap<>();
    public static volatile Map<String,List<Comment>> commentMap = new HashMap<>();
}
