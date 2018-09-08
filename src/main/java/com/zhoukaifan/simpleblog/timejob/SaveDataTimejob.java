package com.zhoukaifan.simpleblog.timejob;

import com.zhoukaifan.simpleblog.domain.Comment;
import com.zhoukaifan.simpleblog.utils.StrogeUtils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA. User: ZhouKaifan Date:2018/8/28 Time:下午11:05
 */
@Component
public class SaveDataTimejob {
    @Value("${zhoukaifan.simpleblog.savePath}")
    private String savePath;

    @PostConstruct
    public void saveDataTimejobInit() {
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(
                    new FileInputStream(savePath+"readCount.txt"));
            StrogeUtils.readerCountMap.putAll((Map<String, Long>) objectInputStream.readObject());
        } catch (Exception e) {
            if (objectInputStream!=null){
                try {
                    objectInputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }finally {
            if (objectInputStream!=null){
                try {
                    objectInputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        try {
            objectInputStream = new ObjectInputStream(
                    new FileInputStream(savePath+"comment.txt"));
            StrogeUtils.commentMap.putAll((Map<String, List<Comment>>) objectInputStream.readObject());
        } catch (Exception e) {
            if (objectInputStream!=null){
                try {
                    objectInputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }finally {
            if (objectInputStream!=null){
                try {
                    objectInputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Scheduled(cron="* 0/2 *  * * ? ")
    public void saveData() {
        synchronized (this) {
            ObjectOutputStream objectOutputStream = null;
            try {
                objectOutputStream = new ObjectOutputStream(
                        new FileOutputStream(savePath+"readCount.txt"));
                objectOutputStream.writeObject(StrogeUtils.readerCountMap);
            } catch (Exception e) {
                if (objectOutputStream!=null){
                    try {
                        objectOutputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }finally {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                objectOutputStream = new ObjectOutputStream(
                        new FileOutputStream(savePath+"comment.txt"));
                objectOutputStream.writeObject(StrogeUtils.commentMap);
            } catch (Exception e) {
                if (objectOutputStream!=null){
                    try {
                        objectOutputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }finally {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        saveData();
        super.finalize();
    }
}
