package com.bocloud.webssh.booter.pojo;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Shell;
import org.springframework.web.socket.WebSocketSession;

/**
 * @Description: ssh连接信息
 * @Author: NoCortY
 * @Date: 2020/3/8
 */
public class SSHConnectInfo {
    private WebSocketSession webSocketSession;
    private SSHClient sshClient;
    private Session session;
    private Shell shell;

    public Shell getShell() {
        return shell;
    }

    public void setShell(Shell shell) {
        this.shell = shell;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public SSHClient getSshClient() {
        return sshClient;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setSshClient(SSHClient sshClient) {
        this.sshClient = sshClient;
    }
}
