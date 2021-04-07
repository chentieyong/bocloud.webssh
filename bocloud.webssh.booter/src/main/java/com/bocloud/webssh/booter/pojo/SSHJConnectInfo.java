package com.bocloud.webssh.booter.pojo;//package cn.objectspace.webssh.pojo;
//
//import java.io.IOException;
//import java.io.OutputStream;
//
//import org.springframework.web.socket.WebSocketSession;
//
//import com.jcraft.jsch.Channel;
//import com.jcraft.jsch.JSch;
///**
//* @Description: ssh连接信息
//* @Author: NoCortY
//* @Date: 2020/3/8
//*/
//public class SSHJConnectInfo implements SSHConnect{
//    private WebSocketSession webSocketSession;
//    private JSch jSch;
//    private Channel channel;
//    
//    
//    public SSHJConnectInfo(WebSocketSession session) {
//    	this.jSch = new JSch();
//    	this.webSocketSession = session;
//    }
//
//    public WebSocketSession getWebSocketSession() {
//        return webSocketSession;
//    }
//
//    public void setWebSocketSession(WebSocketSession webSocketSession) {
//        this.webSocketSession = webSocketSession;
//    }
//
//    public JSch getjSch() {
//        return jSch;
//    }
//
//    public void setjSch(JSch jSch) {
//        this.jSch = jSch;
//    }
//
//    public Channel getChannel() {
//        return channel;
//    }
//
//    public void setChannel(Channel channel) {
//        this.channel = channel;
//    }
//    
//    public OutputStream getOutputStream() throws IOException {
//    	return this.channel.getOutputStream();
//    }
//}
