package com.zhoukaifan.simpleblog.utils;

import com.zhoukaifan.simpleblog.domain.Comment;
import com.zhoukaifan.simpleblog.domain.ContactMessage;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailSendWorker {
    @Value("${zhoukaifan.simpleblog.email.username}")
    private static final String EMAIL_USER = "blog@zhoukaifan.com";
    @Value("${zhoukaifan.simpleblog.email.password}")
    private static final String EMAIL_PASSWORD = "Dim@10339100";
    @Value("${zhoukaifan.simpleblog.email.addresse}")
    private static final String MAIL_ADDRESSE = "z@zhoukaifan.com";

    private static Logger logger = LoggerFactory.getLogger(MailSendWorker.class);
    public static final Queue<ContactMessage> contactMessageQueue = new ConcurrentLinkedQueue();
    public static final Queue<Comment> commentQueue = new ConcurrentLinkedQueue();

    private Runnable contactMessageRunnable = new Runnable() {
        @Override
        public void run() {
            logger.warn("Start MailSendWorker...");
            while (true) {
                if (contactMessageQueue.isEmpty()) {
                    try {
                        Thread.sleep(10000L);
                    } catch (Exception e) {
                        logger.error("[MAIL_SENDER]", e);
                    }
                    continue;
                }
                try {
                    ContactMessage item = contactMessageQueue.poll();
                    notifyByMail("博客留言：" + item.getTitle(),
                            "> 留言主题："+item.getTitle()+"\n> 留言者姓名："+item.getName()
                                    +"\n> 留言者邮箱："+item.getEmail()+"\n\n" +item.getContent());
                    logger.warn("[MAIL_NOTICED]" + item);
                } catch (Exception e) {
                    logger.error("MAIL_NOTICE_ERROR]", e);
                }
            }
        }
    };
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    private Runnable commentRunnable = new Runnable() {
        @Override
        public void run() {
            logger.warn("Start MailSendWorker...");
            while (true) {
                if (commentQueue.isEmpty()) {
                    try {
                        Thread.sleep(10000L);
                    } catch (Exception e) {
                        logger.error("[MAIL_SENDER]", e);
                    }
                    continue;
                }
                try {
                    Comment item = commentQueue.poll();
                    notifyByMail("博客评论：" + item.getName(),
                            "> 评论者姓名："+item.getName()+"\n> 评论关联："+item.getpId()
                                    +"\n> 留言者邮箱："+item.getEmail()+"\n> 评论时间："+simpleDateFormat.format(item.getDate())
                                    +"\n> 删除评论：\n> > https://www.zhoukaifan.com/comment/deletesById/"+item.getBlogContentId()+"?key="+item.getKey()+"&ids="+item.getId()
                                    +"\n> 查看评论：\n> > https://www.zhoukaifan.com/blog/"+item.getBlogContentId()+".html\n\n" +item.getContent());
                    logger.warn("[MAIL_NOTICED]" + item);
                } catch (Exception e) {
                    logger.error("MAIL_NOTICE_ERROR]", e);
                }
            }
        }
    };

    public void notifyByMail(String mailTitle, String mailContent) {
        notifyByMail(mailTitle, mailContent, MAIL_ADDRESSE);
    }
    private void notifyByMail(String mailTitle, String mailContent, String mailAddresse) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.ym.163.com");
        props.put("mail.smtp.auth", "true");
        Session sendMailSession = Session.getInstance(props,
                new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL_USER, EMAIL_PASSWORD);
                    }
                }
        );
        try {
            Message newMessage = new MimeMessage(sendMailSession);
            newMessage.setFrom(new InternetAddress(EMAIL_USER)); // 发件人
            newMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(mailAddresse));
            newMessage.setSubject(mailTitle);
            newMessage.setText(mailContent);
            Transport.send(newMessage);
        } catch (Exception e) {
        }
    }
    public MailSendWorker() {
        new Thread(contactMessageRunnable).start();
        new Thread(commentRunnable).start();
    }
}
