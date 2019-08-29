package com.project.netty;

import com.project.netty.task.CheckGateWayConnect;
import com.project.netty.task.CheckGateWayMac;
import com.project.netty.util.ConfigUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class NettyProxyServer {

	private static final Logger logger = Logger.getLogger(NettyProxyServer.class);
	
	public static final ChannelGroup allChannels = new DefaultChannelGroup("NettyProxyServer");
	public static final ChannelGroup mobileChannels = new DefaultChannelGroup("mobileChannels");
	public static final ChannelGroup gateWayChannels = new DefaultChannelGroup("gateWayChannels");
	
	private int mobilePort;
	private int gateWayPort;
	private int threadNum;
	private long delay;
	private long period;
	private long initHart;
	
	private ServerBootstrap serverBootstrap;
	
	public NettyProxyServer() { 
		mobilePort = Integer.parseInt(ConfigUtils.getProperty("mobilePort").trim());
		gateWayPort = Integer.parseInt(ConfigUtils.getProperty("gateWayPort").trim());
		threadNum = Integer.parseInt(ConfigUtils.getProperty("threadNum").trim());
		delay = Long.parseLong(ConfigUtils.getProperty("delay").trim());
		period = Long.parseLong(ConfigUtils.getProperty("period").trim());
		initHart = Long.parseLong(ConfigUtils.getProperty("hartPeriod").trim());
		
		serverBootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
	}
	
	public boolean startup() throws Exception{
		ChannelPipeline pipeline = serverBootstrap.getPipeline();
		pipeline.addLast("handler", new ProxyServerHandler());
		
		serverBootstrap.setOption("tcpNoDelay", true);

		serverBootstrap.setOption("receiveBufferSize",65536);
		serverBootstrap.setOption("sendBufferSize",65536);

		serverBootstrap.setOption("child.tcpNoDelay", true);
		serverBootstrap.setOption("child.keepAlive", true);
		serverBootstrap.setOption("child.receiveBufferSize",65536);
		serverBootstrap.setOption("child.sendBufferSize",65536);
		
		Channel mobileChannel = serverBootstrap.bind(new InetSocketAddress(mobilePort));
		allChannels.add(mobileChannel);
		logger.warn("server is started on port " + mobilePort + ",mobilePort listening!");
		Channel gateWayChannel = serverBootstrap.bind(new InetSocketAddress(gateWayPort));
		allChannels.add(gateWayChannel);
		logger.warn("server is started on port " + gateWayPort + ",gateWayPort listening!");

		return false;
	}
	
	private void startTask() {
		ScheduledExecutorService scheduler =  Executors.newScheduledThreadPool(threadNum);
		// 获取mac地址
		scheduler.scheduleWithFixedDelay(new CheckGateWayMac(), delay, period, TimeUnit.SECONDS);
		// 定时心跳连接
		scheduler.scheduleWithFixedDelay(new CheckGateWayConnect(), delay, period, TimeUnit.SECONDS);
		// 心跳归零
		// scheduler.scheduleWithFixedDelay(new InitGateWay(), delay, initHart, TimeUnit.SECONDS);
	}

	public static void nettyStart() {
		try {
			NettyProxyServer proxyServer = new NettyProxyServer();
			proxyServer.startup();
			proxyServer.startTask();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		nettyStart();
	}

}
