一、注册应用获取appKey 和 appSecret
===


**（1）到[Mob官网](http://www.mob.com/developer/login)注册成为Mob开发者，老用户直接登录即可；**

![image](http://onmw6wg88.bkt.clouddn.com/mob.png)

**（2）注册或登录完成后，会返回至首页，点击右上角的“进入后台”，会跳转至[管理后台](http://dashboard.mob.com/#/main/index)，点击下拉列表，选择“创建新应用”。如下图：**

![Snip20170527_3.png](http://upload-images.jianshu.io/upload_images/4131265-83e3d15dff892fcf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

** 输入应用名称后点击“保存”，如下图：**
![image](http://onmw6wg88.bkt.clouddn.com/Snip20170525_11.png)

** 应用创建后在左边导航栏点击“添加产品”，选择 SecurityCodeSDK ,点击马上开始,如下图：**

![Snip20170527_4.png](http://upload-images.jianshu.io/upload_images/4131265-7a1ee158c6de8f9b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

** 此时左边导航栏就能看到您添加的产品了，点击“概况”即可看到您接下来需要的AppKey和AppSecret了，你也可以在菜单中看到设置和短信记录等信息，修改完设置记得保存,如下图：**

![Snip20170527_7.png](http://upload-images.jianshu.io/upload_images/4131265-2b8572fbf516711c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

二、 集成MobApi
===
通过cocoapods集成
---

** 通过 CocoaPods进行安装，只需在 Podfile文件中添加：**

```
pod 'MobAPI-For-iOS'
```
** 添加之后执行 pod install / pod update 命令即可。**

手动集成
---

### 1. 获取 MobApi SDK:

** 点击[链接](http://www.mob.com/downloadDetail/mobAPI/ios)下载最新版SDK，解压后得到以下文件结构：**

![Snip20170525_43.png](http://upload-images.jianshu.io/upload_images/4131265-bfaf7a276d4fa4b7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 2. 导入SDK:

**将下图中红色框标记的文件夹（包含MOBFoundation.framework 和 MobAPI.framework）拖入到工程中**

![Snip20170525_44.png](http://upload-images.jianshu.io/upload_images/4131265-4a1b0818d606e25e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**确认勾选，点击finish完成导入**

![Snip20170525_45.png](http://upload-images.jianshu.io/upload_images/4131265-9299fec10613294e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 3. 添加依赖库:

**SDK所需依赖库列表(Xcode 7 下.dylib库后缀名更改为.tbd)：**

```
libz.dylib
libstdc++.dylib
```

![Snip20170525_47.png](http://upload-images.jianshu.io/upload_images/4131265-7bc3b7db641faa92.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

三、配置appkey和appSecret
===

**在项目中的info.plist文件中添加键值对，键分别为==MOBAppKey==和==MOBAppSecret==，值为步骤一申请的appkey和appSecret**

![Snip20170525_56.png](http://upload-images.jianshu.io/upload_images/4131265-098683832dd276d8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

四、使用MobApi接口
===

**MOBApiCloud类中提供了一个sendRequest方法用于请求不同的功能接口，如查询手机号码归属地：**
```
#import <MobAPI/MobAPI.h>

[MobAPI sendRequest:[MOBAPhoneRequest ownershipRequestByPhone:@"13333333333"]

onResult:^(MOBAResponse *response) {

if (response.error)

{

NSLog(@”request error = %@”, response.error);

}

else

{

NSLog(@”request success = %@”, response.responder);

}

}];
```

**==注==：使用不同的Request类对象来进行请求，可以请求不同功能的API，其中MOBARequest为请求基类，可以进行各种API的请求。对于其他Request类中未封装的请求可以使用基类进行。请求类功能对照表如下：**


类型 | 功能
---|---
MOBACookRequest | 菜谱API相关请求
MOBAPhoneRequest | 手机号API相关请求 
MOBAPostcodeRequest | 邮编API相关请求
MOBAWeatherRequest | 天气API相关请求
MOBAIdRequest | 身份证信息API相关请求
MOBAEnvironmentRequest | 空气质量API相关请求
MOBAIpRequest | ip信息API相关请求
MOBAKvRequest | K-v存储API相关请求
MOBAKvRequest | K-v查询API相关请求
MOBACalendarRequest | 万年历API相关请求
MOBAMobileLuckyRequest | 号码吉凶查询API相关请求
MOBABankCardRequest | 银行卡信息API相关请求
MOBALaohuangliRequest | 老黄历查询API相关请求
MOBAHealthRequest | 健康查询API相关请求
MOBAMarriageRequest | 婚姻匹配API相关请求
MOBAHistoryRequest | 历史上的今天API相关请求
MOBADreamRequest | 周公解梦API相关请求
MOBAIdiomRequest | 成语API相关请求
MOBADictionaryRequest | 新华字典API相关请求
MOBAHoroScopeRequest | 算八字API相关请求

