package com.zhoukaifan.simpleblog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA. User: ZhouKaifan Date:2018/8/28 Time:下午7:19
 */
public class Comment implements Serializable{
    private String id;
    @NotNull
    private String name;
    private Date date;
    private String email;
    @NotNull
    private String content;
    @NotNull
    private String blogContentId;
    private String pId;
    @JsonIgnore
    private String key;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBlogContentId() {
        return blogContentId;
    }

    public void setBlogContentId(String blogContentId) {
        this.blogContentId = blogContentId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
