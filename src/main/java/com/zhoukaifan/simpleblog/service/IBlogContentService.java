package com.zhoukaifan.simpleblog.service;

import com.zhoukaifan.simpleblog.domain.BlogContent;
import com.zhoukaifan.simpleblog.domain.Comment;
import com.zhoukaifan.simpleblog.vo.BlogType;
import com.zhoukaifan.simpleblog.vo.DateType;
import com.zhoukaifan.simpleblog.vo.Page;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: ZhouKaifan Date:2018/8/28 Time:下午7:30
 */
public interface IBlogContentService {

    void addReaderCount(String id);

    long getReaderCount(String id);

    Page<BlogContent> getBlogContentList(BlogContent blogContent, Page<BlogContent> blogContentPage);

    Collection<BlogType> getBlogTypeList();

    Collection<DateType> getDateTypeList();

    BlogContent getBlogContentById(String id);

    void createComment(Comment comment);

    List<Comment> getCommentListByBlogId(String blogId);

    boolean deletesCommentById(String key,String blogId,List<String> ids);
}
