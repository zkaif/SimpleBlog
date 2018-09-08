package com.zhoukaifan.simpleblog.utils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA. User: ZhouKaifan Date:2018/8/29 Time:上午10:55
 */
@Component
public class SshUtils {
    @Value("${zhoukaifan.simpleblog.ssh.username}")
    private String userName;
    @Value("${zhoukaifan.simpleblog.ssh.host}")
    private String host;
    @Value("${zhoukaifan.simpleblog.ssh.password}")
    private String password;
    @Value("${zhoukaifan.simpleblog.ssh.port}")
    private Integer port;
    public List<String> execute(final String command) {
        List<String> relust = new ArrayList<>();
        JSch jsch = new JSch();
        try {
            //创建session并且打开连接，因为创建session之后要主动打开连接
            Session session = jsch.getSession(userName, host, port);
            session.setPassword(password);
            session.setUserInfo(new MyUserInfo());
            session.connect();
            Channel channel = session.openChannel("exec");
            ChannelExec channelExec = (ChannelExec)channel;
            channelExec.setCommand(command);
            channelExec.setInputStream(null);
            BufferedReader input = new BufferedReader(new InputStreamReader
                    (channelExec.getInputStream()));
            channelExec.connect();
            //接收远程服务器执行命令的结果
            String line;
            while ((line = input.readLine()) != null) {
                relust.add(line);
            }
            input.close();
//            if (channelExec.isClosed()) {
//                relust = new ArrayList<>();
//            }
            channelExec.disconnect();
            session.disconnect();

        } catch (JSchException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return relust;
    }
}
