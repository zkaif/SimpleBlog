package com.zhoukaifan.simpleblog.vo;

public class BlogType {
    private static final long serialVersionUID = 2100952399625289102L;
    private String name;
    private long blogCount;

    public BlogType() {
    }

    public BlogType(String name, long blogCount) {
        this.name = name;
        this.blogCount = blogCount;
    }

    public long getBlogCount() {
        return blogCount;
    }

    public void setBlogCount(long blogCount) {
        this.blogCount = blogCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addCount(){
        blogCount++;
    }
    public void subCount(){
        blogCount--;
    }
}
