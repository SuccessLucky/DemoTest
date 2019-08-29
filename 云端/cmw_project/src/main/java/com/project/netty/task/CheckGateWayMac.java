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

public class CheckGateWayMac implements Runnable {

	public static final Logger logger = Logger.getLogger(CheckGateWayMac.class);

	@Override
	public void run() {

		ChannelGroup group = NettyProxyServer.gateWayChannels;
		Iterator<Channel> iterator= group.iterator();
		while(iterator.hasNext()) {
			Channel channel = iterator.next();
			if(channel.isConnected() && channel.getAttachment() == null) {
//				logger.warn("通道id为:"+channel.getId()+" 的网关没有返回MAC,重发获取MAC的的命令!");
				channel.write(ChannelBuffers.wrappedBuffer(ProxyServerHandler.getGateWayMac)).addListener(new ChannelFutureListener(){
					@Override
					public void operationComplete(ChannelFuture future)
							throws Exception {
						if(future.isSuccess()) {
//							logger.debug("定时任务重发获取网关MAC地址命令成功!");
						} else {
//							logger.debug("定时任务重发获取网关MAC地址命令失败,等待下次调度重发!");
						}
					}

				});
			}
		}

	}

}
