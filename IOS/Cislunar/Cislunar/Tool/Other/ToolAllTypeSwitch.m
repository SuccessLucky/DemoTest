//
//  ToolAllTypeSwitch.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/1/11.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import "ToolAllTypeSwitch.h"
#include <stdio.h>
#include <string.h>

@implementation ToolAllTypeSwitch



/////////////////////////////////////////////////////
//
// 功能：二进制取反
//
// 输入： const unsigned char *src  二进制数据
//      int length                待转换的二进制数据长度
//
// 输出： unsigned char *dst        取反后的二进制数据
//
// 返回： 0    success
//
//////////////////////////////////////////////////////
int convert(unsigned char *dst, const unsigned char *src, int length)
{
    int i;
    
    for(i=0; i<length; i++)
    {
        dst[i] = src[i]^0xFF;
    }
    return 0;
}

//////////////////////////////////////////////////////////
//
// 功能：十六进制转为十进制
//
// 输入： const unsigned char *hex          待转换的十六进制数据
//      int length                       十六进制数据长度
//
// 输出：
//
// 返回： int  rslt                        转换后的十进制数据
//
// 思路：十六进制每个字符位所表示的十进制数的范围是 0 ~255 ，进制为 256
//      左移 8 位 (<<8) 等价乘以 256
//
/////////////////////////////////////////////////////////
unsigned long HextoDec(const unsigned char *hex, int length)
{
    int i;
    unsigned long rslt = 0;
    
    for(i=0; i<length; i++)
    {
        rslt += (unsigned long)(hex[i])<<(8*(length-1-i));
        
    }
    
    return rslt;
}


/////////////////////////////////////////////////////////
//
// 功能：十进制转十六进制
//
// 输入： int dec                     待转换的十进制数据
//      int length                  转换后的十六进制数据长度
//
// 输出： unsigned char *hex          转换后的十六进制数据
//
// 返回： 0    success
//
// 思路：原理同十六进制转十进制
//////////////////////////////////////////////////////////
int DectoHex(int dec, unsigned char *hex, int length)
{
    int i;
    
    for(i=length-1; i>=0; i--)
    {
        hex[i] = (dec%256)&0xFF;
        dec /= 256;
    }
    
    return 0;
}

/////////////////////////////////////////////////////////
//
// 功能：求权
//
// 输入： int base                    进制基数
//      int times                   权级数
//
// 输出：
//
// 返回： unsigned long               当前数据位的权
//
//////////////////////////////////////////////////////////
unsigned long power(int base, int times)
{
    int i;
    unsigned long rslt = 1;
    
    for(i=0; i<times; i++)
        rslt *= base;
    
    return rslt;
}

/////////////////////////////////////////////////////////
//
// 功能： BCD 转 10 进制
//
// 输入： const unsigned char *bcd     待转换的 BCD 码
//      int length                   BCD 码数据长度
//
// 输出：
//
// 返回： unsigned long               当前数据位的权
//
// 思路：压缩 BCD 码一个字符所表示的十进制数据范围为 0 ~ 99, 进制为 100
//      先求每个字符所表示的十进制值，然后乘以权
//////////////////////////////////////////////////////////
unsigned long BCDtoDec(const unsigned char *bcd, int length)
{
    int i, tmp;
    unsigned long dec = 0;
    
    for(i=0; i<length; i++)
    {
        tmp = ((bcd[i]>>4)&0x0F)*10 + (bcd[i]&0x0F);
        dec += tmp * power(100, length-1-i);
    }
    
    return dec;
}

/////////////////////////////////////////////////////////
//
// 功能：十进制转 BCD 码
//
// 输入： int Dec                      待转换的十进制数据
//      int length                   BCD 码数据长度
//
// 输出： unsigned char *Bcd           转换后的 BCD 码
//
// 返回： 0  success
//
// 思路：原理同 BCD 码转十进制
//
//////////////////////////////////////////////////////////
int DectoBCD(int Dec, unsigned char *Bcd, int length)
{
    int i;
    int temp;
    
    for(i=length-1; i>=0; i--)
    {
        temp = Dec%100;
        Bcd[i] = ((temp/10)<<4) + ((temp%10) & 0x0F);
        Dec /= 100;
    }
    
    return 0;
}

/*

//------------- 函数信息 ------------------------------------------
// 函 数 名 : BCDtoDec
// 函数描述 : BCD 码转换成十进制码
// 入口参数 : temp: 转化的 BCD 码
// 返     回 : 转化后的十进制码
// 作     者 :
// 日     期 : 2006.11.24
// 说     明 :
//
//---------------------------------------------------------------
uint8_t  BCDtoDec( uint8_t temp )
{
    return ( (temp/16 )*10+temp%16) ;
}




//------------- 函数信息 ------------------------------------------
// 函 数 名 : DectoBCD
// 函数描述 : 十 / 十六进制码转换成 BCD 码
// 入口参数 : temp: 转化的十进制码
// 返     回 : 转化后的 BCD 码
// 作     者 :
// 日     期 : 2006.11.24
// 说     明 :
//
//---------------------------------------------------------------
uint8_t DectoBCD( uint8_t temp )
{
    return ( (temp/10)*16 + temp%10 ) ;
}
 
 */

@end
