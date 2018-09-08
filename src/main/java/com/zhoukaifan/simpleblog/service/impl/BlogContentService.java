package com.zhoukaifan.simpleblog.service.impl;

import com.zhoukaifan.simpleblog.domain.BlogContent;
import com.zhoukaifan.simpleblog.domain.Comment;
import com.zhoukaifan.simpleblog.service.IBlogContentService;
import com.zhoukaifan.simpleblog.utils.MailSendWorker;
import com.zhoukaifan.simpleblog.utils.StrogeUtils;
import com.zhoukaifan.simpleblog.vo.BlogType;
import com.zhoukaifan.simpleblog.vo.DateType;
import com.zhoukaifan.simpleblog.vo.Page;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.logging.log4j.util.PropertySource.Comparator;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA. User: ZhouKaifan Date:2018/8/28 Time:下午7:30
 */
@Service
public class BlogContentService implements IBlogContentService {

    @Override
    public void addReaderCount(String id) {
        synchronized (StrogeUtils.fileNameMap.get(id)){
            Long count = StrogeUtils.readerCountMap.get(id);
            StrogeUtils.readerCountMap.put(id,count==null?1:count+1);
        }
    }

    @Override
    public long getReaderCount(String id) {
        return StrogeUtils.readerCountMap.get(id)==null?0:StrogeUtils.readerCountMap.get(id);
    }

    @Override
    public Page<BlogContent> getBlogContentList(BlogContent blogContent,
            Page<BlogContent> blogContentPage) {
        if (blogContent==null){ return blogContentPage;}
        Collection<BlogContent> list = StrogeUtils.fileNameMap.values();
        if (blogContent.getGroup()!=null){
                list = list.stream().filter(item->blogContent.getGroup().equals(item.getGroup()))
                        .collect(Collectors.toList());
        }
        if (blogContent.getType()!=null){
                list = list.stream().filter(item->blogContent.getType().equals(item.getType()))
                        .collect(Collectors.toList());
        }
        if (blogContent.getDateStr()!=null){
                list = list.stream().filter(item->blogContent.getDateStr().equals(item.getDateStr()))
                        .collect(Collectors.toList());
        }
        list = list.stream().sorted((o1,o2)->{return o1.getDate().before(o2.getDate())?1:-1;}).collect(Collectors.toList());
        blogContentPage.setTotal(list.size());
        blogContentPage.setTotalPage(list.size()%blogContentPage.getCount()>0?
                list.size()/blogContentPage.getCount()+1:list.size()/blogContentPage.getCount());

        list=list.stream().skip(blogContentPage.getCount()*(blogContentPage.getPage()-1)).
                limit(blogContentPage.getCount()).collect(Collectors.toList());

        blogContentPage.setData(new ArrayList<>(list));
        return blogContentPage;
    }

    @Override
    public Collection<BlogType> getBlogTypeList() {
        return StrogeUtils.blogTypes;
    }

    @Override
    public Collection<DateType> getDateTypeList() {
        return StrogeUtils.dateTypes;
    }

    @Override
    public BlogContent getBlogContentById(String id) {
        return StrogeUtils.fileNameMap.get(id);
    }

    @Override
    public void createComment(Comment comment) {
        if (comment==null||comment.getBlogContentId()==null){
            return;
        }
        List<Comment> list = StrogeUtils.commentMap.get(comment.getBlogContentId());
        if (list==null){
            if (StrogeUtils.fileNameMap.get(comment.getBlogContentId())==null){
                return;
            }
            synchronized (StrogeUtils.fileNameMap.get(comment.getBlogContentId())){
                if (StrogeUtils.commentMap.get(comment.getBlogContentId())==null) {
                    list = Collections.synchronizedList(new ArrayList<>());
                    StrogeUtils.commentMap.put(comment.getBlogContentId(), list);
                }
            }
        }
        comment.setId(UUID.randomUUID().toString());
        comment.setKey(UUID.randomUUID().toString());
        comment.setDate(new Date());
        list.add(comment);
        MailSendWorker.commentQueue.offer(comment);
    }

    @Override
    public List<Comment> getCommentListByBlogId(String blogId) {
        return StrogeUtils.commentMap.get(blogId)==null?new ArrayList<>():StrogeUtils.commentMap.get(blogId);
    }

    @Override
    public boolean deletesCommentById(String key,String blogId,List<String> ids) {
        if (key==null||blogId==null||ids==null){
            return false;
        }
        List<Comment> list = StrogeUtils.commentMap.get(blogId);
        if (list==null){
            return false;
        }
        for (Comment comment : list) {
            if (ids.contains(comment.getId())&&key.equals(comment.getKey())) {
                list.remove(comment);
                return true;
            }
        }
        return false;
    }
}
