package com.project.utils.smarttools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;

/**
 *
 * @Description: 与IPC通信数据打包
 */
public class DataPack {

	private static Log logger = LogFactory.getLog(DataPack.class);

	/**
	 * 协议头
	 */
	public static final byte SMART_HEAD = 0x2A;
	/**
	 * 协议尾
	 */
	public static final byte SMART_END = 0x23;

	/**
	 *
	 * @param cmdId
	 * @param number
	 * @param srcAddr
	 *            源地址
	 * @param dstAddr
	 *            目的地址
	 * @param data
	 *            数据
	 * @return
	 */
	public static byte[] joinSmartMsg(byte cmdId, byte number, byte[] srcAddr,
									  byte[] dstAddr, byte[] data) {
		int dstAddrLength = 0;
		if (dstAddr != null) {
			dstAddrLength = dstAddr.length;
		}
		int dataLength = 0;
		if (data != null) {
			dataLength = data.length;
		}
		int srcAddrLength =0 ;
		if(srcAddr != null){
			srcAddrLength = srcAddr.length;
		}

		byte[] sendMsg = new byte[4 + 2 + srcAddrLength + dstAddrLength
				+ dataLength];// 创建发送数据byte数组
		sendMsg[0] = SMART_HEAD;
		sendMsg[1] = (byte) (2 + srcAddrLength + dstAddrLength + dataLength);
		sendMsg[2] = cmdId;
		sendMsg[3] = number;
		if(srcAddrLength >0 ){
			System.arraycopy(srcAddr, 0, sendMsg, 4, srcAddrLength);
		}
		if (dstAddrLength > 0) {
			System.arraycopy(dstAddr, 0, sendMsg, 4 + srcAddrLength,
					dstAddrLength);
		}
		if (dataLength > 0) {
			System.arraycopy(data, 0, sendMsg, 4 + srcAddrLength
					+ dstAddrLength, dataLength);
		}
		sendMsg[sendMsg.length - 2] = createSmartSum(cmdId, number, srcAddr,
				dstAddr, data);// 这个是校验码

		sendMsg[sendMsg.length - 1] = SMART_END;
		return sendMsg;
	}

	/**
	 * 计算校验码
	 *
	 * @param cmdId
	 * @param number
	 * @param srcAddr
	 * @param dstAddr
	 * @param data
	 * @return
	 */
	public static byte createSmartSum(byte cmdId, byte number, byte[] srcAddr,
									  byte[] dstAddr, byte[] data) {
		byte checkSum = 0x00;
		checkSum += cmdId;
		checkSum += number;
		if (srcAddr != null) {
			for (byte item : srcAddr) {
				checkSum += item;
			}
		}
		if (dstAddr != null) {
			for (byte item : dstAddr) {
				checkSum += item;
			}
		}
		if (data != null) {
			for (byte item : data) {
				checkSum += item;
			}
		}
		checkSum = (byte) (checkSum % 256);
		return checkSum;
	}

	/**
	 * 检查校验和
	 *
	 * @param receiverData
	 * @return
	 */
	public static boolean checkSmartSum(byte[] receiverData) {
		byte[] dataArray = new byte[receiverData.length - 4];
		System.arraycopy(receiverData, 2, dataArray, 0, dataArray.length);
		byte checkSum = 0;
		// 计算校验和
		for (byte data : dataArray) {
			checkSum += data;
		}
		checkSum = (byte) (checkSum % 256);
		// 对比校验和
		if (checkSum == receiverData[receiverData.length - 2]) {
			return true;
		}
		return false;
	}

	/**
	 * 获取数据长度
	 *
	 * @param receiverData
	 * @return
	 */
	public static byte getSmartDataLength(byte[] receiverData) {
		int length = 0;
		length = receiverData.length - 4;
		return (byte) length;
	}

	// ----------------------------------------------------------------------------

	/**
	 * 计算数据校验
	 *
	 * @param sendMsg
	 *            byte[]
	 * @return byte[] 校验数组
	 */
	public static byte[] checkSum(byte[] sendMsg) {
		try {
			byte[] chekSum = new byte[2];
			char H_data = 0;
			char L_data = 0;
			// 校验和 ：高位为数据的和
			if (sendMsg.length - DataPack.joinData(sendMsg[25], sendMsg[26]) < 29) {
				for (int i = 2; i < sendMsg.length; i++) {
					if (i == 2) {
						H_data = (char) sendMsg[i];
					} else {
						H_data = (char) (H_data + sendMsg[i]);
					}
				}
			} else {
				for (int i = 2; i < sendMsg.length - 2; i++) {
					if (i == 2) {
						H_data = (char) sendMsg[i];
					} else {
						H_data = (char) (H_data + sendMsg[i]);
					}
				}
			}
			// 校验和 ：低位为数据的异或
			if (sendMsg.length - DataPack.joinData(sendMsg[25], sendMsg[26]) < 29) {
				for (int i = 2; i < sendMsg.length; i++) {
					if (i == 2) {
						L_data = (char) sendMsg[i];
					} else {
						L_data = (char) (L_data ^ sendMsg[i]);
					}
				}
			} else {
				for (int i = 2; i < sendMsg.length - 2; i++) {
					if (i == 2) {
						L_data = (char) sendMsg[i];
					} else {
						L_data = (char) (L_data ^ sendMsg[i]);
					}
				}
			}
			chekSum[0] = (byte) H_data;
			chekSum[1] = (byte) L_data;
			return chekSum;
		} catch (Exception e) {
			logger.warn("DataPack-->70-->" + e.toString());
			return null;
		}
	}



	/**
	 * 小蛮腰指纹锁校验
	 * @param data 返回数据
	 * @return boolean
	 */
	public static boolean lockDataCheck(byte[] data){
		byte[] lockData = new byte[8];
		System.arraycopy(data,20,lockData,0,8);
		if(lockData[0] != (byte)0x80){
			return false;
		}
		byte sum = 0;
		for(int i = 2; i< 7; i++) {
			sum ^= lockData[i];
		}
		if(sum != lockData[7]){
			return false;
		}
		if(lockData[1] == 0x05 &&  lockData[2] == 0x02 && lockData[3] == 0x00 ){
			return true;
		}
		return false;
	}



	/**
	 * 组合发送消息
	 *
	 * @param msgNum
	 *            消息序号
	 * @param reNum
	 *            响应码
	 * @param eqNum
	 *            设备号
	 * @param msgType
	 *            消息类型
	 * @param nodeAdd
	 *            节点地址
	 * @param cOrder
	 *            控制命令
	 * @param data
	 *            数据内容
	 * @return byte[] 消息
	 */
	public static byte[] joinData(byte[] msgNum, byte[] reNum, byte[] eqNum,
								  byte msgType, byte[] nodeAdd, byte[] cOrder, byte[] data) {
		try {
			byte[] sendMsg = new byte[29 + data.length];
			// 起始标志2Byte
			sendMsg[0] = (byte) 0xFF;
			sendMsg[1] = 0x55;
			// 消息序号2Byte
			sendMsg[2] = msgNum[0];
			sendMsg[3] = msgNum[1];
			// 响应码3Byte
			sendMsg[4] = reNum[0];
			sendMsg[5] = reNum[1];
			sendMsg[6] = reNum[2];
			// 设备地址12Byte
			sendMsg[7] = eqNum[0];
			sendMsg[8] = eqNum[1];
			sendMsg[9] = eqNum[2];
			sendMsg[10] = eqNum[3];
			sendMsg[11] = eqNum[4];
			sendMsg[12] = eqNum[5];
			sendMsg[13] = eqNum[6];
			sendMsg[14] = eqNum[7];
			sendMsg[15] = eqNum[8];
			sendMsg[16] = eqNum[9];
			sendMsg[17] = eqNum[10];
			sendMsg[18] = eqNum[11];
			// 消息类型1Byte
			sendMsg[19] = msgType;
			// 预留字段1Byte
			sendMsg[20] = 0x00;
			// 节点地址2Byte
			sendMsg[21] = nodeAdd[0];
			sendMsg[22] = nodeAdd[1];
			// 操作指令2Byte
			sendMsg[23] = cOrder[0];
			sendMsg[24] = cOrder[1];
			// 数据长度
			byte[] datalength = DataPack.getDataLength(data.length);// 计算数据长度
			sendMsg[25] = datalength[0];
			sendMsg[26] = datalength[1];
			if (data.length > 0) {
				System.arraycopy(data, 0, sendMsg, 27, data.length);
			}
			byte[] t_msg = new byte[sendMsg.length - 2];
			System.arraycopy(sendMsg, 0, t_msg, 0, sendMsg.length - 2);
			byte[] chceksum = DataPack.checkSum(t_msg);// 计算校验位
			// 校验和2Byte
			sendMsg[sendMsg.length - 2] = chceksum[0];
			sendMsg[sendMsg.length - 1] = chceksum[1];
			return sendMsg;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("DataPack-->146-->" + e.toString());
			return null;
		}
	}

	/**
	 * 计算数据长度
	 *
	 * @param datalength
	 *            数据长度
	 * @return byte[]
	 */
	public static byte[] getDataLength(int datalength) {
		try {
			byte[] d_length = new byte[2];
			int h_len = 0;
			int l_len = 0;
			if (datalength > 0xFF) {
				h_len = datalength >> 8;
				l_len = datalength & 0x00FF;
			} else {
				h_len = 0x00;
				l_len = datalength;
			}
			d_length[0] = (byte) h_len;
			d_length[1] = (byte) l_len;
			return d_length;

		} catch (Exception e) {
			logger.warn("DataPack-->175-->" + e.toString());
			return null;
		}
	}

	/**
	 * 数据过滤
	 *
	 * @param reMsg
	 *            接收到的数据
	 * @return boolean
	 */
	public static boolean dataFilter(byte[] reMsg) {
		try {
			if (reMsg.length >= 29 && (reMsg[0] & 0xFF) == 0xFF
					&& (reMsg[1] & 0xFF) == 0x55) {// 判断数据头
				/*
				 * if (reMsg.length < 29) {// 判断是否为一条基本数据 return false; }
				 */
				byte[] checksum = DataPack.checkSum(reMsg);
				logger.warn(checksum[0] + ";" + reMsg[reMsg.length - 2]);
				logger.warn(checksum[1] + ";" + reMsg[reMsg.length - 1]);
				if (checksum[0] == reMsg[reMsg.length - 2]
						&& checksum[1] == reMsg[reMsg.length - 1]) {// 对比校验位
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.warn("DataPack-->206-->" + e.toString());
			return false;
		}

	}

	/**
	 * 组合操作类型码
	 *
	 * @param h_data
	 *            高位数据
	 * @param l_data
	 *            低位数据
	 * @return int
	 */
	public static int joinData(byte h_data, byte l_data) {
		int cOrder = h_data << 8;
		cOrder = cOrder + l_data;
		return cOrder & 0xFFFF;
	}

	/**
	 * 获取设备地址
	 *
	 * @param reMsg
	 *            数据
	 * @return String
	 */
	public static String getEqAdd(byte[] reMsg) {
		try {
			byte[] byte_eqAdd = new byte[12];
			System.arraycopy(reMsg, 7, byte_eqAdd, 0, 12);
			return new String(byte_eqAdd);
		} catch (Exception e) {
			logger.warn("DataPack-->240-->" + e.toString());
			return null;
		}
	}

	/**
	 * 组合高低位获取真实数据
	 *
	 * @param h_data
	 * @param l_data
	 * @return int
	 */
	public static int getIntData(byte h_data, byte l_data) {
		int data_h = h_data;
		int intData = ((data_h << 8) + l_data) & 0xffff;
		return intData;
	}

	/**
	 * 组合高低位获取真实数据
	 *
	 * @param h_data
	 * @param l_data
	 * @return byte
	 */
	public static byte getByteData(byte h_data, byte l_data) {
		int data_h = h_data;
		byte byteData = (byte) ((data_h << 8) + l_data);
		return byteData;
	}

	/**
	 * 获得消息中数据内容
	 *
	 * @param reMsg
	 * @return
	 */
	public static String getDataContent(byte[] reMsg) {
		try {
			byte[] byte_content = new byte[reMsg.length - 29];
			System.arraycopy(reMsg, 27, byte_content, 0, byte_content.length);
			return new String(byte_content);
		} catch (Exception e) {
			logger.warn("DataPack-->283-->" + e.toString());
			return null;
		}
	}

	/**
	 * 获得消息中消息序号
	 *
	 * @param reMsg
	 * @return
	 */
	public static byte[] getMsgNo(byte[] reMsg) {
		try {
			byte[] msgNo = new byte[2];
			System.arraycopy(reMsg, 2, msgNo, 0, 2);
			return msgNo;
		} catch (Exception e) {
			logger.warn("DataPack-->300-->" + e.toString());
			return null;
		}
	}

	/**
	 * 获得消息中的设备地址
	 *
	 * @param reMsg
	 * @return
	 */
	public static byte[] getEqNum(byte[] reMsg) {
		try {
			byte[] eqNum = new byte[12];
			System.arraycopy(reMsg, 7, eqNum, 0, 12);
			return eqNum;
		} catch (Exception e) {
			logger.warn("DataPack-->317-->" + e.toString());
			return null;
		}
	}

	/**
	 * 获得消息中的操作指令
	 *
	 * @param reMsg
	 * @return
	 */
	public static byte[] getOrderCode(byte[] reMsg) {
		try {
			byte[] orderCode = new byte[2];
			System.arraycopy(reMsg, 23, orderCode, 0, 2);
			return orderCode;
		} catch (Exception e) {
			logger.warn("DataPack-->334-->" + e.toString());
			return null;
		}
	}

	/**
	 * 获得消息中的节点地址
	 *
	 * @param reMsg
	 * @return
	 */
	public static byte[] getNodeAddr(byte[] reMsg) {
		try {
			byte[] nodeAddr = new byte[2];
			System.arraycopy(reMsg, 21, nodeAddr, 0, 2);
			return nodeAddr;
		} catch (Exception e) {
			logger.warn("DataPack-->351-->" + e.toString());
			return null;
		}
	}

	/**
	 * 获得消息中的响应码
	 *
	 * @param reMsg
	 * @return
	 */
	public static byte[] getRespCode(byte[] reMsg) {
		try {
			byte[] respCode = new byte[3];
			System.arraycopy(reMsg, 4, respCode, 0, 3);
			return respCode;
		} catch (Exception e) {
			logger.warn("DataPack-->368-->" + e.toString());
			return null;
		}
	}

	/**
	 * 获得消息中的考勤黑名单
	 *
	 * @param reMsg
	 * @return String[]
	 */
	public static String[] getAttendBlack(byte[] reMsg) {
		try {
			String[] blackInfo = new String[2];
			byte[] dataLen = new byte[2];// 数据总长度(字节数)
			byte[] marker = new byte[3];// 标志位
			byte[] blackLen = new byte[2];// 信息组长度
			System.arraycopy(reMsg, 25, dataLen, 0, 2);// 获得数据总产度
			System.arraycopy(reMsg, 27, marker, 0, 3);// 获得标志位
			System.arraycopy(reMsg, 30, blackLen, 0, 2);// 获得信息组长度
			int totalLength = Tools.byteArrayToShort(dataLen);// 数据总长度
			int blackLengh = Tools.byteArrayToShort(blackLen);// 信息组长度
			byte[] data = new byte[blackLengh];// 信息组
			logger.warn("totalLength--->" + totalLength);
			logger.warn("blacklist length--->" + blackLengh);
			if (totalLength - 5 == blackLengh) {// 判断信息是否"合法"
				System.arraycopy(reMsg, 32, data, 0, blackLengh);// 获得信息组
				if (blackLengh % 8 == 0 && blackLengh != 0) {
					int i = blackLengh / 8;// 计算消息组数
					StringBuffer cardsn = new StringBuffer("");
					byte[] codeStr = new byte[8];
					for (int j = 0; j < i; j++) {
						System.arraycopy(data, 8 * j, codeStr, 0, 8);// 获得单条卡序号
						cardsn.append(new String(codeStr)).append("|");// 拼接卡序号，由"|"分隔
					}
					logger.warn("cardsn--->" + cardsn.toString());
					cardsn.deleteCharAt(cardsn.length() - 1);// 删除最后一个"|"
					blackInfo[0] = new String(marker);// 标志位
					blackInfo[1] = cardsn.toString();// 包含卡序号的字符串
					logger.warn("marker bits-->" + blackInfo[0]);
					logger.warn("cardsn-->" + blackInfo[1]);
					return blackInfo;
				} else if (blackLengh == 0 && "str".equals(new String(marker))) {
					blackInfo[0] = new String(marker);// 标志位
					blackInfo[1] = null;// 包含卡序号的字符串
					logger.warn("marker bits-->" + blackInfo[0]);
					logger.warn("cardsn-->" + blackInfo[1]);
					return blackInfo;
				} else
					return null;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.warn("DataPack-->422-->" + e.toString());
			return null;
		}
	}

	/**
	 * 获得消息中的门禁白名单
	 *
	 * @param reMsg
	 * @return String[]
	 */
	public static String[] getDooraccessWhite(byte[] reMsg) {
		try {
			String[] whiteInfo = new String[2];
			byte[] dataLen = new byte[2];// 数据总长度(字节数)
			byte[] marker = new byte[3];// 标志位
			byte[] whiteLen = new byte[2];// 信息组长度
			System.arraycopy(reMsg, 25, dataLen, 0, 2);// 获得数据总产度
			System.arraycopy(reMsg, 27, marker, 0, 3);// 获得标志位
			System.arraycopy(reMsg, 30, whiteLen, 0, 2);// 获得信息组长度
			int totalLength = Tools.byteArrayToShort(dataLen);// 数据总长度
			int whiteLengh = Tools.byteArrayToShort(whiteLen);// 信息组长度
			logger.warn("totalLength--->" + totalLength);
			logger.warn("door whitelist len--->" + whiteLengh);
			byte[] data = new byte[whiteLengh];// 信息组
			if (totalLength - 5 == whiteLengh) {// 判断消息是否"合法"
				if (whiteLengh != 0) {
					System.arraycopy(reMsg, 32, data, 0, whiteLengh);// 获得信息组
					StringBuffer whiteLog = new StringBuffer("");
					for (int tag = 0; tag < whiteLengh;) {
						byte[] cardsn = new byte[8];
						System.arraycopy(data, tag, cardsn, 0, 8);// 获得单条卡序号
						whiteLog.append(new String(cardsn)).append("|");
						byte[] timeseg = new byte[1];
						System.arraycopy(data, tag + 8, timeseg, 0, 1);// 获得时段序号
						whiteLog.append(timeseg[0] & 0xff).append("|");
						byte[] len = new byte[1];
						System.arraycopy(data, tag + 9, len, 0, 1);// 获得门总数
						int length = len[0] & 0xff;
						logger.warn("door num-->" + length);
						for (int i = 0; i < length; i++) {
							byte[] whiteStr = new byte[1];
							// for (int j = 0; j < i; j++) {
							System.arraycopy(data, tag + 10 + i, whiteStr, 0, 1);// 获得门号
							logger.warn("door no-->" + (whiteStr[0] & 0xff));
							whiteLog.append(whiteStr[0] & 0xff).append("|");
							// }
						}
						logger.warn("door no-->" + whiteLog.toString());
						whiteLog.deleteCharAt(whiteLog.length() - 1);// 删除最后一个"|"
						whiteLog.append("#");
						logger.warn("door no-->" + whiteLog.toString());
						tag += 10 + length;
					}
					whiteLog.deleteCharAt(whiteLog.length() - 1);// 删除最后一个"#"
					logger.warn("door no-->" + whiteLog.toString());
					if (whiteLog.length() > 2) {
						whiteInfo[0] = new String(marker);// 标志位
						whiteInfo[1] = whiteLog.toString();// 包含门禁时段信息的字符串
						logger.warn("marker bits-->" + whiteInfo[0]);
						logger.warn("dooraccess whitelist-->" + whiteInfo[1]);
						return whiteInfo;
					} else {
						return null;
					}
				} else if (whiteLengh == 0 && "str".equals(new String(marker))) {
					whiteInfo[0] = new String(marker);// 标志位
					whiteInfo[1] = null;// 包含门禁时段信息的字符串
					logger.warn("marker bits-->" + whiteInfo[0]);
					logger.warn("dooraccess whitelist-->" + whiteInfo[1]);
					return whiteInfo;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.warn("DataPack-->507-->" + e.toString());
			return null;
		}
	}

	/**
	 * 获得消息中的门禁时段
	 *
	 * @param reMsg
	 * @return String[]
	 */
	public static String[] getDooraccessTime(byte[] reMsg) {
		try {
			String[] timeInfo = new String[2];
			byte[] dataLen = new byte[2];// 数据总长度(字节数)
			byte[] marker = new byte[3];// 标志位
			byte[] timeLen = new byte[2];// 信息组长度
			System.arraycopy(reMsg, 25, dataLen, 0, 2);// 获得数据总产度
			System.arraycopy(reMsg, 27, marker, 0, 3);// 获得标志位
			System.arraycopy(reMsg, 30, timeLen, 0, 2);// 获得信息组长度
			int totalLength = Tools.byteArrayToShort(dataLen);// 数据总长度
			int timeLengh = Tools.byteArrayToShort(timeLen);// 信息组长度
			logger.warn("totalLength-->" + totalLength);
			logger.warn("access times len-->" + timeLengh);
			byte[] data = new byte[timeLengh];// 信息组
			if (totalLength - 5 == timeLengh) {// 判断数据是否"合法"
				System.arraycopy(reMsg, 32, data, 0, timeLengh);// 获得信息组
				if (timeLengh % 38 == 0 && timeLengh != 0) {
					StringBuffer times = new StringBuffer("");
					int length = timeLengh / 38;// 计算消息的组数
					for (int i = 0; i < length; i++) {// 拼接门禁时段，单条门禁时段的内容由"|"分隔，门禁时段间用"#"分隔
						byte[] timeseg = new byte[1];// 时段序号
						System.arraycopy(data, 38 * i, timeseg, 0, 1);
						times.append(timeseg[0] & 0xff).append("|");
						byte[] weekctr = new byte[1];// 星期控制
						System.arraycopy(data, 38 * i + 1, weekctr, 0, 1);
						switch (weekctr[0] & 0xff) {
							case 127:
								times.append(0).append("|");
								break;
							case 126:
								times.append(1).append("|");
								break;
							case 124:
								times.append(2).append("|");
								break;
							default:
								break;
						}
						byte[] startdate = new byte[6];// 开始日期
						System.arraycopy(data, 38 * i + 2, startdate, 0, 6);
						times.append("20" + new String(startdate)).append("|");
						byte[] enddate = new byte[6];// 结束日期
						System.arraycopy(data, 38 * i + 8, enddate, 0, 6);
						times.append("20" + new String(enddate)).append("|");
						byte[] starttime1 = new byte[4];// 开始时段1
						System.arraycopy(data, 38 * i + 14, starttime1, 0, 4);
						times.append(new String(starttime1) + "00").append("|");
						byte[] endtime1 = new byte[4];// 结束时段1
						System.arraycopy(data, 38 * i + 18, endtime1, 0, 4);
						times.append(new String(endtime1) + "00").append("|");
						byte[] starttime2 = new byte[4];// 开始时段2
						System.arraycopy(data, 38 * i + 22, starttime2, 0, 4);
						times.append(new String(starttime2) + "00").append("|");
						byte[] endtime2 = new byte[4];// 结束时段2
						System.arraycopy(data, 38 * i + 26, endtime2, 0, 4);
						times.append(new String(endtime2) + "00").append("|");
						byte[] starttime3 = new byte[4];// 开始时段3
						System.arraycopy(data, 38 * i + 30, starttime3, 0, 4);
						times.append(new String(starttime3) + "00").append("|");
						byte[] endtime3 = new byte[4];// 结束时段3
						System.arraycopy(data, 38 * i + 34, endtime3, 0, 4);
						times.append(new String(endtime3) + "00");// .append("|");
						// times.deleteCharAt(times.length()-1);
						times.append("#");
					}
					times.deleteCharAt(times.length() - 1);// 删除最后一个"#"
					timeInfo[0] = new String(marker);// 标志位
					timeInfo[1] = times.toString();// 包含门禁时段的字符串
					return timeInfo;
				} else if (timeLengh == 0 && "str".equals(new String(marker))) {
					timeInfo[0] = new String(marker);// 标志位
					timeInfo[1] = null;// 包含门禁时段的字符串
					return timeInfo;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.warn("DataPack-->595-->" + e.toString());
			return null;
		}
	}

	/**
	 * 获得消息中的充值金额
	 *
	 * @param reMsg
	 * @return float
	 */
	public static float getRechargeNum(byte[] reMsg) {
		try {
			byte[] dataLen = new byte[2];// 数据总长度(字节数)
			System.arraycopy(reMsg, 25, dataLen, 0, 2);// 获得数据总产度
			int totalLength = Tools.byteArrayToShort(dataLen);// 数据总长度
			if (totalLength == 4) {
				byte[] data = new byte[totalLength];// 信息组
				System.arraycopy(reMsg, 27, data, 0, totalLength);// 获得信息组

				BigDecimal b1 = new BigDecimal(Integer.toString(Tools
						.byteArrayToInt(data)));
				BigDecimal b2 = new BigDecimal("100.00");

				float sMoney = b1.divide(b2)
						.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();// 计算充值金额

				return sMoney;

				// Log.d("RechargeNum", "充值金额-->"+Tools.byteArrayToInt(data) /
				// 100.00f);
				// return Tools.byteArrayToInt(data) / 100.00f;
			} else {
				return 0.00f;
			}
		} catch (Exception e) {
			logger.warn("DataPack-->630-->" + e.toString());
			return 0.00f;
		}
	}

	/**
	 * 获得消息中的补贴金额
	 *
	 * @param reMsg
	 * @return float
	 */
	public static float getAllowanceNum(byte[] reMsg) {
		try {
			byte[] dataLen = new byte[2];// 数据总长度(字节数)
			System.arraycopy(reMsg, 25, dataLen, 0, 2);// 获得数据总产度
			int totalLength = Tools.byteArrayToShort(dataLen);// 数据总长度
			if (totalLength == 4) {
				byte[] data = new byte[totalLength];// 信息组
				System.arraycopy(reMsg, 27, data, 0, totalLength);// 获得信息组

				BigDecimal b1 = new BigDecimal(Integer.toString(Tools
						.byteArrayToInt(data)));
				BigDecimal b2 = new BigDecimal("100.00");

				float sMoney = b1.divide(b2)
						.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();// 计算补贴金额

				return sMoney;

				// Log.d("AllowanceNum", "补贴金额-->"+Tools.byteArrayToInt(data) /
				// 100.00f);
				// return Tools.byteArrayToInt(data) / 100.00f;
			} else {
				return 0.00f;
			}
		} catch (Exception e) {
			logger.warn("DataPack-->665-->" + e.toString());
			return 0.00f;
		}
	}

	/**
	 * 获得消息中的出纳员白名单
	 *
	 * @param reMsg
	 * @return String[]
	 */
	public static String[] getCashierWhite(byte[] reMsg) {
		try {
			String[] whiteInfo = new String[2];// 初始化出纳员白名单数组
			byte[] dataLen = new byte[2];// 数据总长度(字节数)
			byte[] marker = new byte[3];// 标志位
			byte[] whiteLen = new byte[2];// 信息组长度
			System.arraycopy(reMsg, 25, dataLen, 0, 2);// 获得数据总产度
			System.arraycopy(reMsg, 27, marker, 0, 3);// 获得标志位
			System.arraycopy(reMsg, 30, whiteLen, 0, 2);// 获得信息组长度
			int totalLength = Tools.byteArrayToShort(dataLen);// 数据总长度
			int whiteLengh = Tools.byteArrayToShort(whiteLen);// 信息组长度
			byte[] data = new byte[whiteLengh];// 信息组
			if (totalLength - 5 == whiteLengh) {// 判断是否是"合法"数据
				System.arraycopy(reMsg, 32, data, 0, whiteLengh);// 获得信息组
				if (whiteLengh % 8 == 0 && whiteLengh != 0) {
					int i = whiteLengh / 8;// 计算卡序号的组数
					StringBuffer cardsn = new StringBuffer("");
					byte[] codeStr = new byte[8];
					for (int j = 0; j < i; j++) {
						System.arraycopy(data, 8 * j, codeStr, 0, 8);// 获取单个卡序号
						cardsn.append(new String(codeStr)).append("|");// 拼接卡序号，由"|"分隔
					}
					cardsn.deleteCharAt(cardsn.length() - 1);// 删除最后一个"|"
					whiteInfo[0] = new String(marker);// 标志位
					whiteInfo[1] = cardsn.toString();// 包含卡序号的字符串
					return whiteInfo;
				} else if (whiteLengh == 0 && "str".equals(new String(marker))) {
					whiteInfo[0] = new String(marker);// 标志位
					whiteInfo[1] = null;// 包含卡序号的字符串
					return whiteInfo;
				} else
					return null;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.warn("DataPack-->718-->" + e.toString());
			return null;
		}
	}

	/**
	 * 获得消息中的移动支付黑名单
	 *
	 * @param reMsg
	 * @return String[]
	 */
	public static String[] getPaymentBlack(byte[] reMsg) {
		try {
			String[] blackInfo = new String[2];// 初始化移动支付黑名单数组
			byte[] dataLen = new byte[2];// 数据总长度(字节数)
			byte[] marker = new byte[3];// 标志位
			byte[] blackLen = new byte[2];// 信息组长度
			System.arraycopy(reMsg, 25, dataLen, 0, 2);// 获得数据总产度
			System.arraycopy(reMsg, 27, marker, 0, 3);// 获得标志位
			System.arraycopy(reMsg, 30, blackLen, 0, 2);// 获得信息组长度
			int totalLength = Tools.byteArrayToShort(dataLen);// 数据总长度
			int blackLengh = Tools.byteArrayToShort(blackLen);// 信息组长度
			byte[] data = new byte[blackLengh];// 信息组
			if (totalLength - 5 == blackLengh) {// 判断是否是"合法"数据
				System.arraycopy(reMsg, 32, data, 0, blackLengh);// 获得信息组
				if (blackLengh % 8 == 0 && blackLengh != 0) {
					int i = blackLengh / 8;// 计算卡序号的组数
					StringBuffer cardsn = new StringBuffer("");
					byte[] codeStr = new byte[8];
					for (int j = 0; j < i; j++) {
						System.arraycopy(data, 8 * j, codeStr, 0, 8);// 获取单个卡号
						cardsn.append(new String(codeStr)).append("|");// 拼接卡序号，由"|"分隔
					}
					cardsn.deleteCharAt(cardsn.length() - 1);// 删除最后一个"|"
					blackInfo[0] = new String(marker);// 标志位
					blackInfo[1] = cardsn.toString();// 包含卡序号的字符串
					return blackInfo;
				} else if (blackLengh == 0 && "str".equals(new String(marker))) {
					blackInfo[0] = new String(marker);// 标志位
					blackInfo[1] = null;// 包含卡序号的字符串
					return blackInfo;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.warn("DataPack-->772-->" + e.toString());
			return null;
		}
	}

	/**
	 * 获得消息中的节点阀值
	 *
	 * @param reMsg
	 * @return String
	 */
	public static String getNodeThreshold(byte[] reMsg) {
		try {
			String nodeThreshold = "";// 初始化节点阀值数组
			byte[] dataLen = new byte[2];// 数据总长度(字节数)
			byte[] thresholdLen = new byte[2];// 信息组长度
			System.arraycopy(reMsg, 25, dataLen, 0, 2);// 获得数据总产度
			System.arraycopy(reMsg, 27, thresholdLen, 0, 2);// 获得信息组长度
			int totalLength = Tools.byteArrayToShort(dataLen);// 数据总长度
			int thresholdLength = Tools.byteArrayToShort(thresholdLen);// 信息组长度
			byte[] data = new byte[thresholdLength];// 信息组
			if (totalLength - 2 == thresholdLength) {// 判断是否是"合法"数据
				System.arraycopy(reMsg, 29, data, 0, thresholdLength);// 获得信息组
				if (thresholdLength % 5 == 0 && thresholdLength != 0) {
					int i = thresholdLength / 5;// 计算节点阀值的组数
					StringBuffer thresholdStr = new StringBuffer("");
					for (int j = 0; j < i; j++) {
						byte[] nodeAddr = new byte[1];
						System.arraycopy(data, 5 * j, nodeAddr, 0, 1);// 获取节点地址
						// 拼接最小值和最大值，由"|"分隔
						thresholdStr.append(nodeAddr[0] & 0xff).append("|");
						byte[] min = new byte[2];
						System.arraycopy(data, 5 * j + 1, min, 0, 2);// 获取最小值
						thresholdStr.append(Tools.byteArrayToShort(min))
								.append("|");
						byte[] max = new byte[2];
						System.arraycopy(data, 5 * j + 3, max, 0, 2);// 获取最大值
						thresholdStr.append(Tools.byteArrayToShort(max));
						thresholdStr.append("#");
					}
					thresholdStr.deleteCharAt(thresholdStr.length() - 1);// 删除最后一个"#"
					nodeThreshold = thresholdStr.toString();// 包含节点阀值的字符串
					return nodeThreshold;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.warn("DataPack-->823-->" + e.toString());
			return null;
		}
	}

	public static void main(String[] args) {

	}

	/**
	 * 获取开卡指令中的卡序号、金额
	 *
	 * @param reMsg
	 *            开卡指令
	 * @return String[]
	 */
	public static String[] getCardinfo(byte[] reMsg) {
		try {
			String[] cardinfo = new String[2];
			byte[] cardsn = new byte[8];
			byte[] money = new byte[4];
			if (reMsg.length > 40) {// 判断是否有数据
				System.arraycopy(reMsg, 27, cardsn, 0, 8);
				System.arraycopy(reMsg, 35, money, 0, 4);
				cardinfo[0] = new String(cardsn);

				BigDecimal b1 = new BigDecimal(Integer.toString(Tools
						.byteArrayToInt(money)));
				BigDecimal b2 = new BigDecimal("100.00");

				float sMoney = b1.divide(b2).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();// 获取指令中的金额
				cardinfo[1] = Float.toString(sMoney);
			}
			return cardinfo;
		} catch (Exception e) {
			logger.warn("DataPack-->857-->" + e.toString());
			return null;
		}
	}

	/**
	 * 获得允许节点上传数据的节点地址
	 *
	 * @param reMsg
	 * @return
	 */
	public static byte[] getNodeAddrArray(byte[] reMsg) {
		try {
			byte[] array = new byte[] { reMsg[27], reMsg[28] };
			int length = Tools.byteArrayToShort(array) / 2;
			byte[] nodeAddrArray = new byte[length];
			if (reMsg.length >= 32) {// 判断是否有数据
				for (int i = 0; i < length; i++) {
					System.arraycopy(reMsg, 29 + i * 2, nodeAddrArray, i, 1);
				}
			} else {
				return null;
			}
			return nodeAddrArray;
		} catch (Exception e) {
			logger.warn("DataPack-->901-->" + e.toString());
			return null;
		}
	}

	/**
	 * 获得消息中的服务器时间
	 *
	 * @param reMsg
	 * @return String
	 */
	public static String getServerTime(byte[] reMsg) {
		try {
			byte[] dataLen = new byte[2];// 数据总长度(字节数)
			System.arraycopy(reMsg, 25, dataLen, 0, 2);// 获得数据长度
			int totalLength = Tools.byteArrayToShort(dataLen);// 数据总长度
			if (totalLength == 17) {
				byte[] timeArray = new byte[totalLength];// 服务器时间数组
				System.arraycopy(reMsg, 27, timeArray, 0, totalLength);// 获得服务器时间
				return new String(timeArray);
			} else {
				return null;
			}

		} catch (Exception e) {
			logger.warn("DataPack---->" + e.toString());
			return null;
		}
	}
}
