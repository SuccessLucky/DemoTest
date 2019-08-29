package com.project.netty;

import com.google.common.base.Strings;
import com.project.netty.exception.AppException;
import com.project.netty.task.InitGateWay;
import com.project.netty.util.ConfigUtils;
import com.project.netty.util.StringUtils;
import com.project.service.DeviceEntityService;
import com.project.utils.BeanUtils;
import com.project.utils.smarttools.BusinessTool;
import com.project.utils.smarttools.DeviceState;
import com.project.utils.smarttools.Tools;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ProxyServerHandler extends SimpleChannelHandler implements
        ChannelHandler {

    private static final Logger log = Logger.getLogger(ProxyServerHandler.class);
    private static final ConcurrentHashMap<Integer, String> mobileChannelIdMac = new ConcurrentHashMap<Integer, String>();
    private static final ConcurrentHashMap<String, Set<Channel>> mobileMacChannel = new ConcurrentHashMap<String, Set<Channel>>();

    private static final ConcurrentHashMap<Integer, String> gateWayChannelIdMac = new ConcurrentHashMap<Integer, String>();
    private static final ConcurrentHashMap<String, Channel> gateWayMacChannel = new ConcurrentHashMap<String, Channel>();

    // 获取网关MAC地址指令
    public static final byte[] getGateWayMac = new byte[]{0x2A, 0x0D, 01, 00, 00, 00, 00, 00, 00, 00, 00, 00, 03, Integer.valueOf("EB", 16).byteValue(), 00, Integer.valueOf("EF", 16).byteValue(), 0x23};
    // 读取网关登陆帧
    public static final byte[] getGateWayLoginInfo = new byte[]{0x2A, 0x0D, 01, 00, 0x5A, (byte) 0x8F, 0x38, 04, 00, 0x6F, 0x0D, 00, 0x13, (byte) 0x92, 00, 0x47, 0x23};
    // 激活网关心跳
    public static final byte[] invokeHeartbeat = new byte[]{0x2A, 0x0D, 01, 00, 00, 00, 00, 00, 00, 00, 00, 00, 0x13, (byte) 0x92, 00, (byte) 0xA6, 0x23};
    // 心跳时间修改的
    public static final byte[] initHeartbeat = new byte[]{0x2A, 0x17, 02, 01, 34, 0x7A, (byte) 0xE8, 0x0D, 00, 0x6F, 0x0D, 00, 00, (byte) 0x76, (byte) 0xE7, 0x0C, 00, 0x6F, 0x0D, 00, 13, (byte) 0x9C, 05, 01, 01, (byte) 0xBD};

    public static Lock connectionlock = new ReentrantLock();

    @Override
    public void channelBound(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        log.warn("channelBound");
        super.channelBound(ctx, e);
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        Channel channel = e.getChannel();

        int channelId = channel.getId();
        if (isMobileChannel(channel)) { //移除手机关联通道hash映射
            removeMobileChannel(channel);
            log.warn("手机通道关闭!   --通道id:" + channel.getId());
        } else {
            //移除网关通道hash映射
            NettyProxyServer.gateWayChannels.remove(channel);
            String mac = gateWayChannelIdMac.get(channelId);
            if (mac != null) {
                gateWayChannelIdMac.remove(channelId);
                gateWayMacChannel.remove(mac);

                //通知app，网关通道已经关闭
                Set<Channel> channelSet = mobileMacChannel.get(mac);
                if (channelSet != null) {
                    for (Channel appChannel : channelSet) {
                        System.out.println("关闭网关对应的手机通道：" + appChannel.getAttachment());
                        sendMobileError(appChannel, (byte) 3);
                    }
                }
            }
            System.out.println("网关通道关闭!   --通道id : " + channel.getId() + " 网关Mac地址 : " + mac);
        }

        NettyProxyServer.allChannels.remove(channel);

        log.warn("在线手机数:" + NettyProxyServer.mobileChannels.size());
        log.warn("在线网关数:" + NettyProxyServer.gateWayChannels.size());
        log.warn("----所有在线网关mac:" + jionGateWayMac(NettyProxyServer.gateWayChannels));

    }

    public void removeMobileChannel(Channel channel) {
        int channelId = channel.getId();
        NettyProxyServer.mobileChannels.remove(channel);
        String mac = mobileChannelIdMac.get(channelId);
        if (mac != null) {
            mobileChannelIdMac.remove(channelId);
            Set<Channel> channelSet = mobileMacChannel.get(mac);
            if (channelSet != null) {
                channelSet.remove(channel);
            }
        }
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {

        Channel channel = ctx.getChannel();
        connectionlock.lock();
        try {
            if (channel.isOpen()
                    && channel.isBound()
                    && channel.isConnected()) {

                log.warn("创建一个连接 id:" + e.getChannel().getId() + " " + e.getChannel().getRemoteAddress() + ",localAddress=" + e.getChannel().getLocalAddress());
                if (!isMobileChannel(channel)) {
                    //如果是网关通道,登记网关信息
                    channel.write(ChannelBuffers.wrappedBuffer(getGateWayLoginInfo)).addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future)
                                throws Exception {
                            if (future.isSuccess()) {
                                log.warn("下发读取网关登入帧命令成功!");
                            } else {
                                log.warn("下发读取网关登入帧命令失败,等待网关MAC检测定时任务自动重新下发命令!");
                            }
                        }

                    });
                    log.warn("网关通道建立!   --通道id:" + channel.getId());
                } else {
                    log.warn("手机通道建立!   --通道id:" + channel.getId());
                }
            }
        } finally {
            connectionlock.unlock();
        }

    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx,
                                    ChannelStateEvent e) throws Exception {
        log.warn("channelDisconnected");
        super.channelDisconnected(ctx, e);
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        log.warn("channelOpen");
        Channel channel = e.getChannel();
        NettyProxyServer.allChannels.add(channel);
        if (isMobileChannel(channel)) {
            NettyProxyServer.mobileChannels.add(channel);
        } else {
            NettyProxyServer.gateWayChannels.add(channel);
        }

        log.warn("在线手机数:" + NettyProxyServer.mobileChannels.size());
        log.warn("在线网关数:" + NettyProxyServer.gateWayChannels.size());

    }

    private String jionGateWayMac(ChannelGroup gatewaychannels) {
        Iterator<Channel> iterator = gatewaychannels.iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            Channel channel = iterator.next();
            Object obj = channel.getAttachment();
            if (obj != null) {
                sb.append(obj.toString()).append(":").append(channel.isConnected()).append("|");
            }
        }
        String result = "";
        if (sb.length() > 0) {
            result = sb.subSequence(0, sb.length() - 1).toString();
        }
        return result;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        log.error(e.getCause().getMessage(), e.getCause());
        e.getCause().printStackTrace();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        Channel channel = ctx.getChannel();
        final ChannelBuffer channelBuffer = (ChannelBuffer) e.getMessage();
//		String message = StringUtils.bytesToHexStringJoinDelimiter(channelBuffer.array(), " ") + "   --通道id:"+channel.getId();
        String message = Tools.byte2HexStr(channelBuffer.array()) + "   --通道id:" + channel.getId();

        if (isMobileChannel(channel)) { //手机通道
            byte flag = channelBuffer.readByte();
            if (flag == 0x55) {
                log.warn("收到手机端发送的登记请求:" + message);
                ChannelBuffer macChannelBuffer = channelBuffer.copy(3, 8);
                String mac = Tools.byte2HexStr(macChannelBuffer.array());
                channel.setAttachment(mac);
                log.warn("收到手机端登记的网关Mac地址:" + mac);

                // 检查网关通道是否存在
//                Channel ch = gateWayMacChannel.get(mac);
                Channel ch = gateWayMacChannel(mac);
                if (ch != null && ch.isConnected()) {
                    mobileChannelIdMac.put(channel.getId(), mac);
                    if (mobileMacChannel.get(mac) != null) {
                        mobileMacChannel.get(mac).add(channel);
                    } else {
                        Set<Channel> channelSet = new HashSet<Channel>();
                        channelSet.add(channel);
                        mobileMacChannel.put(mac, channelSet);
                    }

                    sendMobileSuc(channel);
                } else {
                    sendMobileError(channel, (byte) 2);
                }

            } else if (isHeartbeat(channelBuffer.array())) {
                // 手机端心跳指令
                // 直接返回指令
                channel.write(ChannelBuffers.wrappedBuffer(channelBuffer.array()));
            } else {
                String mac = mobileChannelIdMac.get(channel.getId());
                log.warn("收到来自手机端的命令:" + message);
                if (mac == null) {
                    sendMobileError(channel, (byte) 1);
                    log.error("命令转发失败,该手机没有发送登记信息MAC");
                } else {
//                    Channel ch = gateWayMacChannel.get(mac);
                    Channel ch = gateWayMacChannel(mac);
                    channelBuffer.readerIndex(0);
                    if (ch != null && ch.isConnected()) {

                        ArrayList<byte[]> channelBufferArr = new ArrayList<>();
                        // 判断是否需要拆包
                        if (Tools.isNeedSpilt(channelBuffer.array())) {
                            channelBufferArr = Tools.splitData(channelBuffer.array());
                        } else {
                            channelBufferArr.add(channelBuffer.array());
                        }

                        for (byte[] channelArray : channelBufferArr) {

                            ch.write(ChannelBuffers.wrappedBuffer(channelArray)).addListener(new ChannelFutureListener() {
                                @Override
                                public void operationComplete(ChannelFuture future)
                                        throws Exception {
                                    if (future.isSuccess()) {
                                        log.warn("命令发送到网关成功!网关MAC:" + future.getChannel().getAttachment() + "  channelId:" + future.getChannel().getId());
                                    } else {
                                        log.error("命令发送到网关失败!网关MAC:" + future.getChannel().getAttachment() + "  channelId:" + future.getChannel().getId(), future.getCause());
                                    }
                                }
                            });
                        }

                    } else if (ch != null && !ch.isConnected()) {
                        sendMobileError(channel, (byte) 2);
                        log.error("网关通道没连接,关闭该网关,等待网关自动重连!");
                        ch.close();
                    } else {
                        sendMobileError(channel, (byte) 2);
                        log.error("不存在对应的网关通道,关闭当前手机通道,重新登记!");
                        removeMobileChannel(channel);
                    }
                }

            }

        } else {//网关通道
            if (channel.getAttachment() == null &&
                    gateWayChannelIdMac.get(channel.getId()) == null &&
                    isGateWayLoginInfoReturn(channelBuffer)) {
                log.warn("收到网关返回的MAC信息:" + message);
                ChannelBuffer macChannelBuffer = channelBuffer.copy(3, 8);
                final String mac = Tools.byte2HexStr(macChannelBuffer.array());
                log.warn("收到网关返回的Mac地址:" + mac);
                channel.setAttachment(mac);
                gateWayChannelIdMac.put(channel.getId(), mac);
                gateWayMacChannel.put(mac, channel);

                channel.write(ChannelBuffers.wrappedBuffer(invokeHeartbeat)).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future)
                            throws Exception {
                        if (future.isSuccess()) {
                            log.warn("激活心跳成功! : " + mac);
                        } else {
                            log.warn("激活心跳失败! : " + mac);
                        }
                    }
                });

                channel.write(ChannelBuffers.wrappedBuffer(initHeartbeat)).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future)
                            throws Exception {
                        if (future.isSuccess()) {
                            log.warn("心跳时间修改成功! : " + mac);
                        } else {
                            log.warn("心跳时间修改成功! : " + mac);
                        }
                    }
                });

            } else if (isGateWayHeartbeatReturn(channelBuffer)) {
                ChannelBuffer macChannelBuffer = channelBuffer.copy(3, 8);
                String mac = Tools.byte2HexStr(macChannelBuffer.array());

                log.warn("接收到网关 " + mac + " 的心跳信息: " + message);
                log.warn("通道id为:" + channel.getId() + " 执行心跳归零任务!");
                channel.write(ChannelBuffers.wrappedBuffer(InitGateWay.initHeartbeat)).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future)
                            throws Exception {
                        if (future.isSuccess()) {
                            log.warn("心跳归零成功!");
                        } else {
                            log.warn("心跳归零失败,等待下次调度重发!");
                        }
                    }
                });
            } else {

                final String mac = gateWayMacByChannel(channel);
                if (mac != null) {
                    Set<Channel> mobileChannels = mobileMacChannel.get(mac);
                    if (mobileChannels != null && mobileChannels.size() > 0) {
                        for (Channel ch : mobileChannels) {
                            if (ch != null && ch.isConnected()) {

                                ArrayList<byte[]> channelBufferArr = new ArrayList<>();
                                // 判断是否需要拆包
                                if (Tools.isNeedSpilt(channelBuffer.array())) {
                                    channelBufferArr = Tools.splitData(channelBuffer.array());
                                } else {
                                    channelBufferArr.add(channelBuffer.array());
                                }

                                for (byte[] channelArray : channelBufferArr) {
                                    if (trashData(channelArray)) continue;

                                    ch.write(ChannelBuffers.wrappedBuffer(channelArray)).addListener(new ChannelFutureListener() {

                                        @Override
                                        public void operationComplete(
                                                ChannelFuture future)
                                                throws Exception {
                                            if (future.isSuccess()) {
                                                log.warn("网关返回的数据发送到手机成功!手机网关MAC:" + future.getChannel().getAttachment());
                                            } else {
                                                log.error("网关返回的数据发送到手机失败!手机网关MAC:" + future.getChannel().getAttachment(), future.getCause());
                                            }
                                        }

                                    });
                                }

                            } else {
                                log.warn("手机通道为null或者没有连接!");
                            }
                        }
                    } else {
                        log.warn("没有对应的手机通道,不能下发!");
                    }
                    reportDeviceInfo(channelBuffer, mac);
                } else {
                    log.warn("不能获取网关通道对应的Mac!");
                }
            }
        }

    }

    public boolean trashData(byte[] channelArray) {
        boolean result = false;

        try {
            if (channelArray.length >= 23) {
                DeviceState deviceState = BusinessTool.getInstance().resolveData(channelArray);
                if (deviceState.deviceType != 0x02) {
                    byte[] od = deviceState.deviceOD;
                    byte check = channelArray[22];
                    if (od[0] == 0x0f && od[1] == (byte) 0xbe && check != 00) {// 多用途休眠设备控制器4030
                        result = true;
                    }
                }
            }

        } catch (Exception e) {
            log.error("判断垃圾请求数据失败", e);
        }

        return result;
    }

    public Channel gateWayMacChannel(String mac) {
        if (Strings.isNullOrEmpty(mac)) {
            return null;
        }

        // 检查网关通道是否存在
        Channel ch = gateWayMacChannel.get(mac);
        if (ch == null) {
            ChannelGroup gatewaychannels = NettyProxyServer.gateWayChannels;
            Iterator<Channel> iterator = gatewaychannels.iterator();
            while (iterator.hasNext()) {
                Channel channel = iterator.next();
                Object obj = channel.getAttachment();
                if (obj != null && mac.equals(obj.toString()) && channel.isConnected()) {
                    log.warn("检测gateWayMacChannel中不存在网关mac : " + mac + ",重新赋值");
                    // 重新添加网关mac地址
                    gateWayChannelIdMac.put(channel.getId(), mac);
                    gateWayMacChannel.put(mac, channel);

                    ch = channel;
                }
            }
        }

        return ch;
    }

    public String gateWayMacByChannel(Channel gatewayChannel) {
        if (gatewayChannel == null) {
            return null;
        }

        String mac = gateWayChannelIdMac.get(gatewayChannel.getId());
        if (Strings.isNullOrEmpty(mac)) {

            ChannelGroup gatewaychannels = NettyProxyServer.gateWayChannels;
            Iterator<Channel> iterator = gatewaychannels.iterator();
            while (iterator.hasNext()) {
                Channel channel = iterator.next();
                Object obj = channel.getAttachment();
                if (obj != null && channel.isConnected() && channel.getId().equals(gatewayChannel.getId())) {
                    log.warn("获取网关地址失败,检测gateWayChannelIdMac中不存在网关channel : " + gatewayChannel.getId() + ",重新赋值");
                    mac = obj.toString();

                    // 重新添加网关mac地址
                    gateWayChannelIdMac.put(channel.getId(), mac);
                    gateWayMacChannel.put(mac, channel);
                }
            }
        }

        return mac;
    }

    public void reportDeviceInfo(final ChannelBuffer channelBuffer, String gatewayMac) {

        try {
            // 记录设备状态

            ArrayList<byte[]> channelBufferArr = new ArrayList<>();
            // 判断是否需要拆包
            if (Tools.isNeedSpilt(channelBuffer.array())) {
                channelBufferArr = Tools.splitData(channelBuffer.array());
            } else {
                channelBufferArr.add(channelBuffer.array());
            }
            for (byte[] channelArray : channelBufferArr) {
                if (trashData(channelArray)) continue;

                DeviceState deviceState = BusinessTool.getInstance().resolveData(channelArray);
                DeviceEntityService deviceEntityService = new BeanUtils().getBean("deviceEntityService");
                deviceEntityService.reportDeviceInfo(deviceState, gatewayMac);
            }
        } catch (Exception e) {
            log.error("设备状态上报失败", e);
        }
    }

    private boolean isGateWayHeartbeatReturn(ChannelBuffer channelBuffer) {

        int length = channelBuffer.readableBytes();
        log.warn("收到网关返回包长度为:" + length);

        if (length == 53) {

            byte head = channelBuffer.getByte(0);
            byte dataLength = channelBuffer.getByte(1);
            byte od1 = channelBuffer.getByte(11);
            byte od2 = channelBuffer.getByte(12);
            byte over = channelBuffer.getByte(52);

            StringBuffer sb = new StringBuffer();
            sb.append("head:").append(StringUtils.byteToHexStr(head)).append(" | ");
            sb.append("dataLength:").append(StringUtils.byteToHexStr(dataLength)).append(" | ");
            sb.append("OD:").append(StringUtils.byteToHexStr(od1)).append(StringUtils.byteToHexStr(od2)).append(" | ");
            sb.append("over:").append(StringUtils.byteToHexStr(over));

            log.warn("isGateWayHeartbeatReturn : 网关返回相关数据信息 : " + sb.toString());

            if (head == 0x2A && od1 == 0x13 && od2 == ((byte) 0x9C) && over == 0x23) {
                log.warn("收到网关返回包为网关心跳返回包!");
                return true;
            }

        }

        return false;
    }

    private boolean isGateWayLoginInfoReturn(ChannelBuffer channelBuffer) {

        int length = channelBuffer.readableBytes();

        if (length == 71) {

            byte head = channelBuffer.getByte(0);
            byte dataLength = channelBuffer.getByte(1);
            byte od1 = channelBuffer.getByte(11);
            byte od2 = channelBuffer.getByte(12);
            byte over = channelBuffer.getByte(70);

            StringBuffer sb = new StringBuffer();
            sb.append("head:").append(StringUtils.byteToHexStr(head)).append(" | ");
            sb.append("dataLength:").append(StringUtils.byteToHexStr(dataLength)).append(" | ");
            sb.append("OD:").append(StringUtils.byteToHexStr(od1)).append(StringUtils.byteToHexStr(od2)).append(" | ");
            sb.append("over:").append(StringUtils.byteToHexStr(over));


            if (head == 0x2A && od1 == 0x13 && od2 == ((byte) 0x92) && over == 0x23) {
                log.warn("收到网关返回包为网关登录信息返回包!");
                return true;
            }

        }

        return false;
    }

    private boolean isGateWayMacReturn(ChannelBuffer channelBuffer) {
        int length = channelBuffer.readableBytes();
        if (length == 65) {
            byte head = channelBuffer.getByte(0);
            byte dataLength = channelBuffer.getByte(1);
            byte od1 = channelBuffer.getByte(11);
            byte od2 = channelBuffer.getByte(12);
            byte over = channelBuffer.getByte(64);

            StringBuffer sb = new StringBuffer();
            sb.append("head:").append(StringUtils.byteToHexStr(head)).append(" | ");
            sb.append("dataLength:").append(StringUtils.byteToHexStr(dataLength)).append(" | ");
            sb.append("OD:").append(StringUtils.byteToHexStr(od1)).append(StringUtils.byteToHexStr(od2)).append(" | ");
            sb.append("over:").append(StringUtils.byteToHexStr(over));

            log.warn(sb.toString());

            if (head == 0x2A && dataLength == 0x3D
                    && od1 == 0x03 && od2 == Integer.valueOf("EB", 16).byteValue()
                    && over == 0x23) {
                return true;
            }
        }

        return false;
    }

    private void sendMobileSuc(Channel channel) {
        byte[] sucBytes = new byte[]{0x55, 01, 0x41, 0x42, Integer.valueOf("AA", 16).byteValue()};
        channel.write(ChannelBuffers.wrappedBuffer(sucBytes));
    }

    private void sendMobileError(Channel channel, byte errorCode) {
        byte[] errorBytes = new byte[]{0x55, 02, 0x7F, 00, 00, Integer.valueOf("AA", 16).byteValue()};
        errorBytes[3] = errorCode;
        String cs = Integer.toHexString(2 + 127 + errorCode);
        errorBytes[4] = Integer.valueOf(cs, 16).byteValue();
        channel.write(ChannelBuffers.wrappedBuffer(errorBytes));
    }

    private boolean isMobileChannel(Channel channel) {
        String localAddress = channel.getLocalAddress().toString();
        String port = localAddress.substring(localAddress.indexOf(":") + 1);
        if (port.equals(ConfigUtils.getProperty("mobilePort"))) {
            return true;
        } else if (port.equals(ConfigUtils.getProperty("gateWayPort"))) {
            return false;
        } else {
            String msg = "未知端口错误,请检查配置文件!端口:" + port;
            log.error(msg);
            throw new AppException(msg);
        }

    }

    /**
     * 是否为心跳数据
     *
     * @param receiveData 接收数据
     * @return true:心跳数据
     */
    public static boolean isHeartbeat(byte[] receiveData) {
        // 检测数据是否合理
        if (receiveData == null || receiveData.length <= 4) {
            return false;
        }
        // 检测是否心跳数据头尾
        if (receiveData[0] != 0x3A || receiveData[receiveData.length - 1] != 0x33) {
            return false;
        }
        // 数据长度
        byte dataLength = receiveData[1];
        // 检测数据是否合法
        if (dataLength != (receiveData.length - 4)) {
            return false;
        }
        // 解析出数据
        byte[] data = new byte[dataLength];
        System.arraycopy(receiveData, 2, data, 0, dataLength);
        // 检测校验和
        byte checkSum = heartbeatCheckSum(data);
        byte receiveDataCheckSum = receiveData[receiveData.length - 2];
        if (checkSum != receiveDataCheckSum) {
            return false;
        }
        // 检测数据是否为心跳数据
        String dataStr = new String(data);
        if (!ConfigUtils.HEARTBEAT_DATA.equals(dataStr)) {
            return false;
        }
        return true;
    }

    /**
     * 心跳数据-校验和
     *
     * @param data
     * @return
     */
    private static byte heartbeatCheckSum(byte[] data) {
        if (data == null || data.length == 0) {
            return 0x00;
        }
        byte checkSum = 0x00;
        for (byte item : data) {
            checkSum += item;
        }
        checkSum = (byte) (checkSum % 256);
        return checkSum;
    }
}
