package com.zhoukaifan.simpleblog.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: ZhouKaifan Date:2018/9/7 Time:下午1:47
 */
public class GithubPushVo {
    List<String> added = new ArrayList<>();
    List<String> modified = new ArrayList<>();
    List<String> removed = new ArrayList<>();

    public List<String> getAdded() {
        return added;
    }

    public void setAdded(List<String> added) {
        this.added = added;
    }

    public List<String> getModified() {
        return modified;
    }

    public void setModified(List<String> modified) {
        this.modified = modified;
    }

    public List<String> getRemoved() {
        return removed;
    }

    public void setRemoved(List<String> removed) {
        this.removed = removed;
    }

    @Override
    public String toString() {
        return "GithubPushVo{" +
                "added=" + added +
                ", modified=" + modified +
                ", removed=" + removed +
                '}';
    }
}
