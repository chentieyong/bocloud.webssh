package com.bocloud.webssh.booter.service.impl;

import com.bocloud.webssh.booter.constant.ConstantPool;
import com.bocloud.webssh.booter.pojo.SSHConnectInfo;
import com.bocloud.webssh.booter.pojo.WebSSHData;
import com.bocloud.webssh.booter.service.WebSSHService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.schmizz.keepalive.KeepAliveProvider;
import net.schmizz.sshj.DefaultConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Shell;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: WebSSH业务逻辑实现
 * @Author: NoCortY
 * @Date: 2020/3/8
 */
@Service
public class WebSSHServiceImpl implements WebSSHService {
    private Logger logger = LoggerFactory.getLogger(WebSSHServiceImpl.class);
    //存放ssh连接信息的map
    public static Map<String, Object> sshMap = new ConcurrentHashMap<>();
    //存放command命令的map
    private static Map<String, Object> commandMap = new ConcurrentHashMap<>();
    //tab键的map
    private static Map<String, Object> tabMap = new ConcurrentHashMap<>();
    //上下键的map
    private static Map<String, Object> upOrDownMap = new ConcurrentHashMap<>();
    //左右键的map
    private static Map<String, Object> leftOrRightMap = new ConcurrentHashMap<>();
    //线程池
    private ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * @Description: 初始化连接
     * @Param: [session]
     * @return: void
     * @Author: NoCortY
     * @Date: 2020/3/7
     */
    @Override
    public void initConnection(WebSocketSession session) {
        DefaultConfig defaultConfig = new DefaultConfig();
        defaultConfig.setKeepAliveProvider(KeepAliveProvider.KEEP_ALIVE);
        SSHClient sshClient = new SSHClient(defaultConfig);
        sshClient.addHostKeyVerifier(new PromiscuousVerifier());
        SSHConnectInfo sshConnectInfo = new SSHConnectInfo();
        sshConnectInfo.setSshClient(sshClient);
        sshConnectInfo.setWebSocketSession(session);
        String uuid = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
        // 将这个ssh连接信息放入map中
        sshMap.put(uuid, sshConnectInfo);
    }

    /**
     * @Description: 处理客户端发送的数据
     * @Param: [buffer, session]
     * @return: void
     * @Author: NoCortY
     * @Date: 2020/3/7
     */
    @Override
    public void recvHandle(String buffer, WebSocketSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        WebSSHData webSSHData = null;
        try {
            webSSHData = objectMapper.readValue(buffer, WebSSHData.class);
        } catch (IOException e) {
            logger.error("Json转换异常");
            logger.error("异常信息:{}", e.getMessage());
            return;
        }
        String userId = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
        if (ConstantPool.WEBSSH_OPERATE_CONNECT.equals(webSSHData.getOperate())) {
            // 找到刚才存储的ssh连接对象
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
            // 启动线程异步处理
            WebSSHData finalWebSSHData = webSSHData;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        connectToSSH(sshConnectInfo, finalWebSSHData, session);
                    } catch (IOException e) {
                        logger.error("webssh连接异常");
                        logger.error("异常信息:{}", e.getMessage());
                        close(session);
                    }
                }
            });
        } else if (ConstantPool.WEBSSH_OPERATE_COMMAND.equals(webSSHData.getOperate())) {
            String command = webSSHData.getCommand();
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
            if (sshConnectInfo != null) {
                try {
                    String commandStr = (String) commandMap.get(userId);
                    if (StringUtils.isEmpty(commandStr)) {
                        commandStr = "";
                    }
                    //回车&tab补全&全部删除&单个删除&换行&左移&右移
                    if (!command.equals("\r")
                            && !command.equals("\t")
                            && !command.equals("\u0015")
                            && !command.equals("\u007F")
                            && !command.equals("\u0003")
                            && !command.equals("\u001B[D")
                            && !command.equals("\u001B[C")) {
                        commandStr = commandStr + command;
                    }
                    commandMap.put(userId, commandStr);
                    if (command.equals("\r")) {
                        //回车校验命令
                        if (commandStr.contains("rm")
                                || commandStr.contains("reboot")) {
                            sendMessage(session, "\n\r无权限执行该命令".getBytes(StandardCharsets.UTF_8));
                            transToSSH(sshConnectInfo, "\u0003");
                            commandMap.remove(userId);
                            return;
                        }
                        commandMap.remove(userId);
                    } else if (command.equals("\u0003")) {
                        //ctrl+c
                        commandMap.remove(userId);
                    } else if (command.equals("\u007F")
                            && commandStr.length() != 0) {
                        //单个删除命令-del
                        commandStr = commandStr.substring(0, commandStr.length() - 1);
                        commandMap.put(userId, commandStr);
                    } else if (command.equals("\u0015")) {
                        //全部删除-ctrl+u
                        commandMap.remove(userId);
                    } else if (command.equals("\t")) {
                        //tab
                        tabMap.put(userId, "\t");
                    } else if (command.equals("\u001B[A") || command.equals("\u001B[B")) {
                        //上翻下翻
                        upOrDownMap.put(userId, "upOrDown");
                    } else if (command.equals("\u001B[D") || command.equals("\u001B[C")) {
                        //左右移动
                        leftOrRightMap.put(userId, "leftOrRight");
                    }
                    transToSSH(sshConnectInfo, command);
                } catch (IOException e) {
                    logger.error("webssh连接异常");
                    logger.error("异常信息:{}", e.getMessage());
                    close(session);
                }
            }
        } else {
            logger.error("不支持的操作");
            close(session);
        }
    }

    @Override
    public void sendMessage(WebSocketSession session, String line) throws IOException {
        System.out.println(line);
        session.sendMessage(new TextMessage(line));
    }

    @Override
    public void sendMessage(WebSocketSession session, byte[] buffer) throws IOException {
        String userId = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
        String commandStr = (String) commandMap.get(userId);
        String tab = (String) tabMap.get(userId);
        String upOrDown = (String) upOrDownMap.get(userId);
        String leftOrRight = (String) leftOrRightMap.get(userId);
        //tab不全commandMap中的内容
        if (tab != null
                && tab.equals("\t")) {
            if (!StringUtils.isEmpty(commandStr)) {
                String cmd = new String(buffer);
                //空格和换行
                if (!cmd.equals("\u0007") && !cmd.contains("\r")) {
                    commandStr = commandStr + cmd;
                    commandMap.put(userId, commandStr);
                }
                tabMap.remove(userId);
            }
        } else if (upOrDown != null && upOrDown.equals("upOrDown")) {
            String cmd = new String(buffer);
            if (StringUtils.hasText(cmd) && !cmd.equals("\u0007")) {
                commandMap.put(userId, cmd);
                upOrDownMap.remove(userId);
            }
        } else if (leftOrRight != null && leftOrRight.equals("leftOrRight")) {
            String cmd = new String(buffer);
            //空格&右移&左移
            if (StringUtils.hasText(cmd)
                    && !cmd.equals("\u0007")
                    && !cmd.equals("\u001B[C")
                    && !cmd.equals("\u001B[D")
                    && !cmd.equals("\b")) {
                if (StringUtils.isEmpty(commandStr)) {
                    commandMap.put(userId, cmd);
                } else {
                    commandStr = commandStr.substring(0, commandStr.length() - 1);
                    int firstIndex = cmd.indexOf("\b");
                    String tmp = cmd.substring(1, firstIndex);
                    commandStr = commandStr.replace(tmp, cmd);
                    commandMap.put(userId, commandStr);
                }
                leftOrRightMap.remove(userId);
            }
        }
        logger.info("commandMap>>>>>>" + commandStr);
        session.sendMessage(new TextMessage(buffer));
    }

    @Override
    public void close(WebSocketSession session) {
        String userId = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
        SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
        if (sshConnectInfo != null) {
            // 断开连接
            if (sshConnectInfo.getSshClient() != null) {
                try {
                    if (sshConnectInfo.getShell() != null) {
                        sshConnectInfo.getShell().close();
                    }
                    if (sshConnectInfo.getSession() != null) {
                        sshConnectInfo.getSession().close();
                    }
                    sshConnectInfo.getSshClient().disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // map中移除
            sshMap.remove(userId);
        }
    }

    /**
     * @param sshConnectInfo
     * @param webSSHData
     * @param webSocketSession
     * @throws IOException
     */
    private void connectToSSH(SSHConnectInfo sshConnectInfo, WebSSHData webSSHData, WebSocketSession webSocketSession)
            throws IOException {
        SSHClient sshClient = sshConnectInfo.getSshClient();
        sshClient.connect(webSSHData.getHost());

        Session session = null;
        try {
            sshClient.authPassword(webSSHData.getUsername(), webSSHData.getPassword());
            session = sshClient.startSession();
            session.allocateDefaultPTY();
            Shell shell = session.startShell();
            sshConnectInfo.setSession(session);
            sshConnectInfo.setShell(shell);

            // 转发消息
            transToSSH(sshConnectInfo, "\r");
            try {
                // 循环读取
                byte[] buffer = new byte[1024];
                int i = 0;
                // 如果没有数据来，线程会一直阻塞在这个地方等待数据。
                while ((i = shell.getInputStream().read(buffer)) != -1) {
                    sendMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            if (sshConnectInfo.getShell() != null) {
                sshConnectInfo.getShell().close();
            }
            if (sshConnectInfo.getSession() != null) {
                sshConnectInfo.getSession().close();
            }
            sshClient.disconnect();
        }
    }

    /**
     * @Description: 将消息转发到终端
     * @Param: [channel, data]
     * @return: void
     * @Author: NoCortY
     * @Date: 2020/3/7
     */
    private void transToSSH(SSHConnectInfo sshConnectInfo, String command) throws IOException {
        if (sshConnectInfo != null) {
            OutputStream outputStream = sshConnectInfo.getShell().getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();
        }
    }
}
