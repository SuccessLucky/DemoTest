package com.project.netty.task;

import com.project.netty.NettyProxyServer;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.group.ChannelGroup;

import java.util.Iterator;

public class CheckGateWayConnect implements Runnable {
	
	public static final Logger logger = Logger.getLogger(CheckGateWayConnect.class);
	
	public static final byte[] heart = new byte[]{0x00};
	
	@Override
	public void run() {
		
		ChannelGroup group = NettyProxyServer.gateWayChannels;
		Iterator<Channel> iterator= group.iterator();
		while(iterator.hasNext()) {
			Channel channel = iterator.next();
			if(channel.isConnected()) {
				// logger.warn("通道id为:"+channel.getId()+" 执行定时心跳任务!");
				channel.write(ChannelBuffers.wrappedBuffer(heart)).addListener(new ChannelFutureListener(){
					@Override
					public void operationComplete(ChannelFuture future)
							throws Exception {
						if(!future.isSuccess()) {
							future.getChannel().close();
							logger.warn("执行定时心跳任务 - 关闭当前通道!");
						}
					}
					
				}); 
			} else {
//				logger.warn("通道未连接,关闭当前通道 : " + channel.getId());
				channel.close();
			}
		}
		
		
	}

}
