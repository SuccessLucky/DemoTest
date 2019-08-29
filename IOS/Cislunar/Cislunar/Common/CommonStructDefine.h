//
//  CommonStructDefine.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#ifndef CommonStructDefine_h
#define CommonStructDefine_h

//登录
typedef NS_ENUM(NSInteger, SHUserLoginState)
{
    SHUserLoginStateNone                = -1,           //未知状态
    SHUserLoginState_NeedLogin           = 0,            //需要用户手动登录
    SHUserLoginStateLocal_LoginSucc      = 1,            //本地验证成功
};

typedef NS_ENUM(NSInteger, GSocketConState)
{
    GSocketConStateSucc         = 0,    //连接成功
    GSocketConStateConnecting   = 1,    //正在连接
    GSocketConStateFail         = 2,    //连接失败
};

//错误码定义
typedef NS_ENUM(NSInteger, GNetErrorCode)
{
    GNetErrorCodeClient     = -3,       //客户端错误
    GNetErrorCodeCancel     = -2,       //取消
    GNetErrorCodeDecodePack = -1,       //解包失败
    GNetErrorCodeNone       = 0,        //成功
    GNetErrorCodeTimeOut    = -11,      //超时
    GNetErrorCodeMemLack    = -12,      //内存不足
    GNetErrorCodeDisconnect = -13,      //网络断开
    GNetErrorCodeNetUnusual = -14,      //网络异常
};

//红外设备类型
typedef NS_ENUM(NSInteger, SHSindexLength)
{
    
    SHSindexLength_OtherReport                    = 100,       //其它上报设备
    SHSindexLength_InfraredAirCondition           = 101,       //空调
    SHSindexLength_InfraredOther_Other            = 102,       //其他红外设备
};

typedef NS_ENUM(NSInteger, SHStudyCode)
{
    SHStudyCodeSucc         = 200,    //学习成功
    SHStudyCodeFail         = 201,    //学习失败
};

typedef NS_ENUM(NSInteger, SHCloudSeverConState)
{
    SHCloudSeverConStateSucc            = 0,    //连接成功
    SHCloudSeverConState_NORecord       = 1,    //无此记录
    SHCloudSeverConState_DisCon          = 2,    //断开连接
};


typedef enum {
    AirConditionBtnType_Mode                    = 0,
    AirConditionBtnType_WindSpeed               = 1,
    AirConditionBtnType_RightAndLight           = 2,
    AirConditionBtnType_UpAndDown               = 3,
    
    AirConditionBtnType_Automatic               = 4,
    AirConditionBtnType_Cold                    = 5,
    AirConditionBtnType_Hot                     = 6,
    AirConditionBtnType_BlowIn                  = 7,
    AirConditionBtnType_Dehumidifier            = 8,
    
    AirConditionBtnType_AutomaticWind           = 9,
    AirConditionBtnType_HighWind                = 10,
    AirConditionBtnType_MiddleWind              = 11,
    AirConditionBtnType_LowWind                 = 12

}AirConditionBtnType;

typedef NS_ENUM(NSInteger, SHDataType)
{
    SHDataType_Year         =  0,
    SHDataType_Month        =  1,
    SHDataType_Day          =  2,
    SHDataType_Hour         =  3,
    SHDataType_Minute       =  4,
    SHDataType_Second       =  5,
    
};

//净水器
typedef enum {
    PositiveRotationType,   //正向转动
    ReverseRotationType,    //反向转动
    StopRotationType,       //停止转动
    RestartTypeType,        //重启
    ReadStatusType,         //状态读取
    LocationSWType,         //开启到指定位置
}ElectricTransimitObjecActionType;


typedef NS_ENUM(NSInteger, SHWaterPurifierFunctionType)
{
    SHWaterPurifierFunctionType_GetStatue                   = 400,   //读取设备状态
    SHWaterPurifierFunctionType_Water                       = 401,   //下发冲洗
    SHWaterPurifierFunctionType_Heart                       = 402,   //心跳
    SHWaterPurifierFunctionType_FilterElementOne            = 403,   //清滤芯1
    SHWaterPurifierFunctionType_FilterElementSec            = 404,   //清滤芯2
    SHWaterPurifierFunctionType_FilterElementThird          = 405,   //清滤芯3
    SHWaterPurifierFunctionType_FilterElementFourth         = 406,   //清滤芯4
    SHWaterPurifierFunctionType_FilterElementFifth          = 407,   //清滤芯5
    SHWaterPurifierFunctionType_Off                         = 408,   //关机
    SHWaterPurifierFunctionType_On                          = 409,   //开机
};

typedef enum {
    ScreenEditAddDeviceTypePeform                = 0,
    ScreenEditAddDeviceTypeLinkage               = 1,
}ScreenEditAddDeviceType;

typedef enum {
    ScreenEditCellTypeTimer                = 0,
    ScreenEditCellTypePeriodOfTime         = 1,
}ScreenEditCellType;

//设备展示列表
typedef enum {
    CommonCollectionViewType_Common               = 0,
    CommonCollectionViewType_Edit                 = 1,
}CommonCollectionViewType;

typedef enum {
    CommonCollectionViewActionType_Common               = 0,
    CommonCollectionViewActionType_LongPressed          = 1,
    CommonCollectionViewActionType_Add                  = 2,
    CommonCollectionViewActionType_Control              = 3,
}CommonCollectionViewActionType;

//红外转发
typedef enum {
    InfraredUICollectionView_Common               = 0,
    InfraredUICollectionView_LongPressed          = 1,
    InfraredUICollectionView_On                  = 2,
}InfraredUICollectionViewActionType;



#endif /* CommonStructDefine_h */
