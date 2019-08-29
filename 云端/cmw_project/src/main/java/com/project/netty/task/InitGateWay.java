package com.project.netty.task;

import com.project.netty.NettyProxyServer;
import com.project.netty.ProxyServerHandler;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.group.ChannelGroup;

import java.util.Iterator;

public class InitGateWay implements Runnable {
	
	public static final Logger logger = Logger.getLogger(InitGateWay.class);

	//心跳归零的数据帧
	public static final byte[] initHeartbeat = new byte[]{0x2A, 0x0F, 02, 00, 00, 00, 00, 00, 00, 00, 00, 00, 0x13, (byte) 0x9C, 04, 01, 00, (byte) 0xB6, 0x23};

	@Override
	public void run() {
//		logger.warn("执行心跳归零任务!");
		
		ChannelGroup group = NettyProxyServer.gateWayChannels;
		Iterator<Channel> iterator= group.iterator();
		while(iterator.hasNext()) {
			final Channel channel = iterator.next();
			if(channel.isConnected()) {
			
				channel.write(ChannelBuffers.wrappedBuffer(ProxyServerHandler.getGateWayMac)).addListener(new ChannelFutureListener(){
					@Override
					public void operationComplete(ChannelFuture future)
							throws Exception {
						if(future.isSuccess()) {
//							logger.debug("心跳归零任务 - 获取网关MAC地址命令成功!");
//							logger.warn("通道id为:"+channel.getId()+" 执行心跳归零任务!");
							channel.write(ChannelBuffers.wrappedBuffer(initHeartbeat)).addListener(new ChannelFutureListener(){
								@Override
								public void operationComplete(ChannelFuture future)
										throws Exception {
									if(future.isSuccess()) {
//										logger.debug("定时任务心跳归零成功!");
									} else {
										logger.debug("心跳归零失败,等待下次调度重发!");
									}
								}
							}); 
						} else {
							logger.debug("获取网关MAC地址命令失败,等待下次调度重发!");
						}
					} 
				});
			}
		}	
	}

}
