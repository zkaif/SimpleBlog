package com.zhoukaifan.simpleblog.timejob;

import com.zhoukaifan.simpleblog.domain.BlogContent;
import com.zhoukaifan.simpleblog.utils.MailSendWorker;
import com.zhoukaifan.simpleblog.utils.Markdown;
import com.zhoukaifan.simpleblog.utils.PinyinUtil;
import com.zhoukaifan.simpleblog.utils.SshUtils;
import com.zhoukaifan.simpleblog.utils.StrogeUtils;
import com.zhoukaifan.simpleblog.vo.BlogType;
import com.zhoukaifan.simpleblog.vo.DateType;
import com.zhoukaifan.simpleblog.vo.GithubPushVo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA. User: ZhouKaifan Date:2018/8/28 Time:下午11:05
 */
@Component
public class RefreshTimejob {

    @Value("${zhoukaifan.simpleblog.gitPath}")
    private String gitPath;

    @Value("${zhoukaifan.simpleblog.blogName}")
    private String blogName;

    private String blogPath;

    @Autowired
    private SshUtils sshUtils;
    @Autowired
    private MailSendWorker mailSendWorker;

    private Map<String,BlogContent> fileNameMap;
    private Map<String,String> contentMap;

    public static final Queue<GithubPushVo> refreshQueue = new ConcurrentLinkedQueue();

    @PostConstruct
    public void refreshTimejobInit() {
        blogPath = gitPath + blogName;
        //第一次启动初始化
        synchronized (this) {
            fileNameMap = new HashMap<>();
            contentMap = new HashMap<>();
            File file = new File(blogPath);
            init(file, blogName);
            StrogeUtils.fileNameMap=fileNameMap;
            StrogeUtils.contentMap=contentMap;
            refreshDataType();
            refreshBlogType();
        }
    }

    private void init(File file, String path) {
        if (file.isFile()) {
            BlogContent blogContent = getBlogContent(path);
            if (!checkoutBlog(blogContent)){
                return;
            }
            addBlogContent(blogContent,contentMap,fileNameMap);
            return;
        }
        File[] files = file.listFiles();
        for (File file1 : files) {
            init(file1, path + "/" + file1.getName());
        }
    }

    //没天3点做一次全量
    @Scheduled(cron="0 0 3 * * ?")
    public void refresh() {
        synchronized (this) {
            pullGit();
            fileNameMap = new HashMap<>();
            contentMap = new HashMap<>();
            File file = new File(blogPath);
            init(file, blogName);
            StrogeUtils.fileNameMap=fileNameMap;
            StrogeUtils.contentMap=contentMap;
            refreshDataType();
            refreshBlogType();
        }
    }

    //每30分钟主动检查一次队列
    @Scheduled(fixedDelay = 30*60*1000)
    public void refreshAddOrDel() {
        synchronized (this) {
            GithubPushVo refreshVo = refreshQueue.poll();
            if (refreshVo == null) {
                return;
            }
            pullGit();
            Set<String> files = new HashSet<>();
            files.addAll(refreshVo.getAdded());
            if (refreshVo.getAdded() != null && !refreshVo.getAdded().isEmpty()) {
                files.addAll(refreshVo.getAdded());
            }
            if (refreshVo.getModified() != null && !refreshVo.getModified().isEmpty()) {
                files.addAll(refreshVo.getModified());
            }
            if (refreshVo.getRemoved() != null && !refreshVo.getRemoved().isEmpty()) {
                files.addAll(refreshVo.getRemoved());
            }
            updateFiles(files);
        }
    }

    private void refreshBlogType() {
        Map<String, BlogType> blogTypeMap = new HashMap<>();
        for (BlogContent blogContent : StrogeUtils.fileNameMap.values()) {
            BlogType blogType = blogTypeMap.get(blogContent.getGroup());
            if (blogType == null) {
                blogType = new BlogType();
                blogType.setName(blogContent.getGroup());
                blogType.setBlogCount(0);
                blogTypeMap.put(blogContent.getGroup(), blogType);
            }
            blogType.setBlogCount(blogType.getBlogCount() + 1);
        }
        List<BlogType> list = new ArrayList<>();
        list.addAll(blogTypeMap.values());
        StrogeUtils.blogTypes = list;
    }

    private void refreshDataType() {
        Map<String, DateType> dateTypeMap = new HashMap<>();
        for (BlogContent blogContent : StrogeUtils.fileNameMap.values()) {
            DateType dateType = dateTypeMap.get(blogContent.getDateStr());
            if (dateType == null) {
                dateType = new DateType();
                dateType.setDate(blogContent.getDateStr());
                dateType.setBlogCount(0);
                dateTypeMap.put(blogContent.getDateStr(), dateType);
            }
            dateType.setBlogCount(dateType.getBlogCount() + 1);
        }
        List<DateType> list = new ArrayList<>();
        list.addAll(dateTypeMap.values());
        StrogeUtils.dateTypes = list.stream().sorted((o1, o2) -> {return o1.getDate().compareToIgnoreCase(o2.getDate());}).collect(Collectors.toList());
    }

    private List<String> pullGit() {
        List<String> files = new ArrayList<>();
        files.add("test");
        List<String> list = sshUtils.execute("cd " + gitPath + ";git pull");
        for (String line : list) {
            if ("Already up-to-date.".equals(line.trim())){
                return new ArrayList<>();
            }
        }
        return files;
    }

    private BlogContent getBlogContent(String path) {
        BufferedReader bufferedReader = null;
        BlogContent blogContent = new BlogContent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");

        try {
            String[] paths = path.split("/");
            if (paths[paths.length - 1].indexOf(".") == 0) {
                return null;
            }
            if (paths[paths.length - 1].indexOf("=") == 0) {
                return null;
            }
            blogContent.setGroup(paths[paths.length - 2]);
            blogContent.setDateStr(paths[paths.length - 3]);
            blogContent.setType(paths[paths.length - 1].split("_")[0]);
            String id = blogContent.getGroup() + blogContent.getDateStr() + paths[paths.length - 1]
                    .replace(".", "_");
            blogContent.setId(PinyinUtil.toPinyin(id));
            //先生成ID，不然无法删除
            File file = new File(gitPath + path);
            if (file.isDirectory()) {
                return null;
            }
            bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String[] datas = null;
            do {
                String data = bufferedReader.readLine();
                if (data == null) {
                    break;
                }
                data = data.trim();
                if ("".equals(data)) {
                    continue;
                }
                datas = data.split(":");
                if (datas[0].equals("content")) {
                    while (true) {
                        String title = bufferedReader.readLine();
                        if (title != null && !"".equals(title.trim())) {
                            blogContent.setTitle(title.replace("#", "").trim());
                            break;
                        }
                    }
                    break;
                }
                if (datas.length < 2) {
                    break;
                }
                String flag = datas[0];
                String flagData = datas[1];
                switch (flag) {
                    case "date":
                        blogContent.setDate(simpleDateFormat.parse(flagData.trim()));
                        break;
                    case "digest":
                        blogContent.setDigest(flagData.trim());
                        break;
                    case "img":
                        blogContent.setImg(flagData.trim());
                        break;
                }
            } while (true);
            StringBuilder content = new StringBuilder();
            String data;
            do {
                data = bufferedReader.readLine();
                if (data == null) {
                    break;
                }
                content.append(data).append("\n");
            } while (true);
            blogContent.setContent(content.toString());
            return blogContent;
        } catch (FileNotFoundException ef) {
            return null;
        }  catch (Exception e) {
            mailSendWorker.notifyByMail("文章发布错误",blogContent.toString()+"\n"+e.getMessage());
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addBlogContent(BlogContent blogContent,Map contentMap,Map fileNameMap) {
        if (blogContent.getImg()==null||"".equals(blogContent.getImg())){
            blogContent.setImg("/image/office.jpg");
        }else {
            blogContent.setImg("https://raw.githubusercontent.com/zkaif/zkaif.github.io/master"+blogContent.getImg());
        }
        String markdown = Markdown.toHtml(blogContent.getContent());
        contentMap.put(blogContent.getId(), markdown);
        blogContent.setContent("");
        fileNameMap.put(blogContent.getId(), blogContent);
    }
    //==============增量刷新==============
    private void updateFiles(Collection<String> files){
        for (String file : files){
            if (!isBlog(file)){
                continue;
            }
            BlogContent blogContent = getBlogContent(file);
            if (blogContent==null){
                delFiles(file);
            }
            addAndUpdateFiles(blogContent);
        }
    }
    private void addAndUpdateFiles(BlogContent blogContent){
            if(!checkoutBlog(blogContent)){
                return;
            }
            if (StrogeUtils.fileNameMap.get(blogContent.getId())==null){
                getBlogType(blogContent.getId()).addCount();
                getDateType(blogContent.getId()).addCount();
            }
            addBlogContent(blogContent,StrogeUtils.contentMap,StrogeUtils.fileNameMap);
    }
    private void delFiles(String file){
            BlogContent blogContent = StrogeUtils.fileNameMap.get(getBlogIdByPath(file));
            if(blogContent==null){
                return;
            }
            StrogeUtils.fileNameMap.remove(blogContent.getId());
            StrogeUtils.contentMap.remove(blogContent.getId());
//            StrogeUtils.commentMap.remove(blogContent.getId());
            getBlogType(blogContent.getGroup()).subCount();
            getDateType(blogContent.getDateStr()).subCount();
    }
    private boolean isBlog(String path){
        if (blogName.equals(path.split("/")[0])){
            return true;
        }
        return false;
    }
    private String getBlogIdByPath(String path){
        try {
            String[] paths = path.split("/");
            if (paths[paths.length - 1].indexOf(".") == 0) {
                return null;
            }
            if (paths[paths.length - 1].indexOf("=") == 0) {
                return null;
            }
            String group = paths[paths.length - 2];
            String dateStr = paths[paths.length - 3];
            String type = paths[paths.length - 1].split("_")[0];
            String id = group + dateStr + type.replace(".", "_");
            id = PinyinUtil.toPinyin(id);
            return id;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private BlogType getBlogType(String key){
        return StrogeUtils.blogTypes.stream().filter(blogType -> blogType.getName().equals(key))
                .findFirst().get();
    }
    private DateType getDateType(String key){
        return StrogeUtils.dateTypes.stream().filter(dateType -> dateType.getDate().equals(key))
                .findFirst().get();
    }
    private boolean checkoutBlog(BlogContent blogContent){
        if (blogContent==null){
            return false;
        }
        if (blogContent.getId()==null||blogContent.getTitle()==null||blogContent.getDate()==null||
                blogContent.getDateStr()==null||blogContent.getType()==null||blogContent.getGroup()==null||
                blogContent.getDigest()==null||blogContent.getContent()==null){
            mailSendWorker.notifyByMail("文章发布失败",blogContent.toString());
            return false;
        }
        return true;
    }
}
