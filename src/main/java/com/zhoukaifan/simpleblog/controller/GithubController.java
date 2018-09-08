package com.zhoukaifan.simpleblog.controller;

import com.zhoukaifan.simpleblog.timejob.RefreshTimejob;
import com.zhoukaifan.simpleblog.vo.GithubPushVo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA. User: ZhouKaifan Date:2018/9/6 Time:下午11:08
 */
@RestController
@RequestMapping("github")
public class GithubController {
    private static final Logger log = LoggerFactory.getLogger("githubcontroller");
    @Autowired
    private RefreshTimejob refreshTimejob;

    @RequestMapping("push")
    public String push(@RequestBody HashMap hashMap){
        log.info(hashMap.toString());
        log.info(hashMap.get("commits").toString());
        GithubPushVo refreshVo = new GithubPushVo();
        for (Map map : (List<Map>)hashMap.get("commits")){
            refreshVo.getAdded().addAll((Collection<? extends String>) map.get("added"));
            refreshVo.getModified().addAll((Collection<? extends String>) map.get("modified"));
            refreshVo.getRemoved().addAll((Collection<? extends String>) map.get("removed"));
        }
        log.info(refreshVo.toString());
        if (refreshVo==null){
            return "1";
        }
        RefreshTimejob.refreshQueue.offer(refreshVo);
        refreshTimejob.refreshAddOrDel();
        return "0";
    }
}
