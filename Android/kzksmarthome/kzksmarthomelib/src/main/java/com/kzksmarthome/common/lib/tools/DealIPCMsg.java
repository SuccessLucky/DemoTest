package com.kzksmarthome.common.lib.tools;

import android.util.Log;

/**
 *
 * @Description: IPC消息处理类
 */
public class DealIPCMsg {

	/**
	 * Context对象
	 */
	//private Context context;
	/**
	 * 设备地址
	 */
	private String eqAdd;
	/**
	 * 上一次解析剩下的数据与本次数据加在一起再次解析
	 */
	private String leftString = "";

	private byte[] startMark = { -1, 85 };// 消息头标志

	/*public DealIPCMsg(Context context,String eqAdd){
		this.context = context;
		this.eqAdd = eqAdd;
	}*/
	public DealIPCMsg(){

	}
	/**
	 *
	 * 消息分包
	 * @param msg
	 */
	public void dealMessage(byte[] msg) {
		try {
			if (msg.length >= 29) {// 一个信息包,至少有29个字节长度
				String msgHead = new String(startMark, "ISO-8859-1");
				/**
				 * 因为消息一次性传了多个,有些消息被分解了,需要我们重新拼接，
				 * 而拼接的地方一般是数组tempstring的头一个或最后一个元素
				 * 出现需要拼接的消息分为以下几种情况(比如IPC发了两条数据信息msg1和msg2过来，msg1和msg2都包括多条消息)
				 * 1.有一条消息的消息头不完整，即消息头的一个字节在msg1中，另一个字节在msg2中(消息头占两个字节)
				 * 2.消息一段在msg1中，一段在msg2中
				 */
				String msgStr = leftString + new String(msg, "ISO-8859-1");
				leftString = "";
				int endTag = msgStr.lastIndexOf(msgHead);
				if (endTag > 0) {// 多条消息
					String[] tempstring = msgStr.split(msgHead, -1);// 以消息头分割消息组
					for (int i = 0; i < tempstring.length; i++) {
						if (!tempstring[i].equals("")) {
							byte[] src = tempstring[i].getBytes("ISO-8859-1");// 获取单条消息
							int len = src.length;
							if (len + 2 >= 29) {
								byte[] msg1 = new byte[len + 2];// 实例化消息数组
								// 设置头标志
								msg1[0] = -1;
								msg1[1] = 85;
								System.arraycopy(src, 0, msg1, 2, len);// 复制消息内容到消息数组
								//new Thread(new HandleMessage(eqAdd, context, msg1)).start();//开启消息处理线程
								System.out.println();
								System.out.println("receive--->");
								for(byte b:msg1)
									System.out.print("__"+b);
								System.out.println();
							}else{
								leftString += tempstring[i];
							}
						}
					}
				} else if (endTag == 0) {
					byte[] src = msgStr.getBytes("ISO-8859-1");// 获取单条消息
					if(src.length >= 29){
						//new Thread(new HandleMessage(eqAdd, context, src)).start();//开启消息处理线程
						System.out.println();
						System.out.println("receive--->");
						for(byte b:src)
							System.out.print("__"+b);
						System.out.println();
					}else{
						leftString += msgStr;
					}
				} else{
					leftString += msgStr;
				}
			}else{
				leftString += new String(msg, "ISO-8859-1");
			}
		} catch (Exception e) {
			Log.d("DealIPCMsg", "deal msg from IPC-->"+e.toString());
		}
	}
	/**
	 * 设置leftString
	 * @param leftString
	 */
	public void setLeftString(String leftString) {
		this.leftString = leftString;
	}

}

