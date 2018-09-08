package com.zhoukaifan.simpleblog.domain;

import java.util.Date;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA. User: ZhouKaifan Date:2018/8/28 Time:下午5:19
 */
public class BlogContent {
    private String id;
    private String title;
    private Date date;
    private String dateStr;
    private String type;
    private String group;
    private String digest;
    private String url;
    private String content;
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BlogContent that = (BlogContent) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BlogContent{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", dateStr='" + dateStr + '\'' +
                ", type='" + type + '\'' +
                ", group='" + group + '\'' +
                ", digest='" + digest + '\'' +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
