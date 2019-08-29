//
//  SHDataBaseDefine.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/25.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#ifndef SHDataBaseDefine_h
#define SHDataBaseDefine_h

//表名
#define Table_Cache                                     @"CacheTable"
#define Cache_ID                                        @"CacheID"
#define Cache_GatewayMacAddr                            @"CacheGatewayMacAddr"
#define Cache_UserAccount                               @"CacheUserAccount"
#define Cache_Identifer                                 @"CacheIdentifer"
#define Cache_JsonStr                                   @"CacheJsonStr"

#define Table_Cache_Room                                @"CacheTableRoom"
#define Table_Cache_Room_FloorID                        @"CacheTableRoom_FloorID"

#define Table_Cache_Device                              @"CacheTableDevice"
#define Table_Cache_Device_RoomID                       @"CacheTableDevice_RoomID"

#define Table_Cache_MemberControlList                   @"Table_Cache_MemberDeviceList"
#define Table_Cache_MemberControlList_ID                @"Table_Cache_MemberDeviceListID"

#define Table_Cache_Lock                                @"Table_Cache_Lock"
#define Table_Cache_Lock_DeviceID                       @"Table_Cache_Lock_DeviceID"

#define Table_Cache_LockMemberList                      @"Table_Cache_LockMemberList"
#define Table_Cache_LockMemberList_Addr                 @"Table_Cache_LockMemberList_Addr"




//标示
#define SHIdentifer_AlarmInfo           @"SHIdentifer_AlarmInfo"
#define SHIdentifer_Floor               @"SHIdentifer_Floor"
#define SHIdentifer_Screen              @"SHIdentifer_Screen"
#define SHIdentifer_AllDevice           @"SHIdentifer_AllDevice"
#define SHIdentifer_AllDeviceV2           @"SHIdentifer_AllDeviceV2"


#define SHIdentifer_GatewayList         @"SHIdentifer_GatewayList"
#define SHIdentifer_DevicePublicPic     @"SHIdentifer_DevicePublicPic"
#define SHIdentifer_DevicePic           @"SHIdentifer_DevicePic"
#define SHIdentifer_RoomPic             @"SHIdentifer_RoomPic"
#define SHIdentifer_ScreenPic           @"SHIdentifer_ScreenPic"

#define SHIdentifer_MemberList          @"SHIdentifer_MemberList"

#endif /* SHDataBaseDefine_h */
