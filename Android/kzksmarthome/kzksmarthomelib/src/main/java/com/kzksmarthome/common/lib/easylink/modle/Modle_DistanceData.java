package com.kzksmarthome.common.lib.easylink.modle;


import com.kzksmarthome.common.lib.easylink.utils.news.DataTypeUtil;

/**
 * 注意  此对象数据都是接受过来的原始数据
 * Created by Administrator on 2016/11/25 0025.
 */
public class Modle_DistanceData {
    private String dip;  //远程ip地址
    private String dp;   //远程端口号
    private String lp;   //本地端口号

    public Modle_DistanceData(String dip, String dp, String lp) {
        this.dip = dip;
        this.dp = dp;
        this.lp = lp;
    }

    public String getDip() {
        return DataTypeUtil.asciiToString(dip);
    }

    public void setDip(String dip) {
        this.dip = dip;
    }

    public String getDp() {  //转换成10进制输出
        return String.valueOf(DataTypeUtil.hexToDecimal(dp));
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getLp() { //转换成10进制输出
        return String.valueOf(DataTypeUtil.hexToDecimal(lp));
    }

    public void setLp(String lp) {
        this.lp = lp;
    }
}
