//
//  FirstPageHeaderCR.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/18.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "FirstPageHeaderCR.h"
#import "DCTitleRolling.h"

@interface FirstPageHeaderCR()<CDDRollingDelegate>

@property (strong , nonatomic)DCTitleRolling *gmRollingView;

@property (weak, nonatomic) IBOutlet UILabel *labelDayTime;

@property (weak, nonatomic) IBOutlet UIImageView *imageVWeather;

@property (weak, nonatomic) IBOutlet UILabel *labelWeather;

@property (weak, nonatomic) IBOutlet UILabel *labelTemperature;

@property (weak, nonatomic) IBOutlet UILabel *labelHumidity;

@property (weak, nonatomic) IBOutlet UIView *viewHistory;

@property (weak, nonatomic) IBOutlet UILabel *labelScreenTitle;

@end

@implementation FirstPageHeaderCR

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setStrGreetings:(NSString *)strGreetings
{
    self.labelDayTime.text = strGreetings;
}

- (void)setStrImageWeather:(NSString *)strImageWeather
{
    self.imageVWeather.image = [UIImage imageNamed:strImageWeather];
}

- (void)setStrWeatherStatus:(NSString *)strWeatherStatus
{
    self.labelWeather.text = strWeatherStatus;
}

- (void)setStrTemperature:(NSString *)strTemperature
{
    
    self.labelTemperature.text = strTemperature;
}

- (void)setStrHumidity:(NSString *)strHumidity
{
    self.labelHumidity.text = strHumidity;
}

- (void)setArrHistory:(NSArray *)arrHistory
{
    _arrHistory = arrHistory;
    [self setUpGMRolling];
}

- (void)setUpGMRolling
{
    /*
    @weakify(self);
    _gmRollingView = [[DCTitleRolling alloc] initWithFrame:CGRectMake(-10, 0, self.viewHistory.frame.size.width + 10, 44) WithTitleData:^(CDDRollingGroupStyle *rollingGroupStyle, NSString *__autoreleasing *leftImage, NSArray *__autoreleasing *rolTitles, NSArray *__autoreleasing *rolTags, NSArray *__autoreleasing *rightImages, NSString *__autoreleasing *rightbuttonTitle, NSInteger *interval, float *rollingTime, NSInteger *titleFont, UIColor *__autoreleasing *titleColor, BOOL *isShowTagBorder) {
        @strongify(self);
        
        NSMutableArray *arrNeedList = [NSMutableArray arrayWithCapacity:0];
        
        for (int i = 0 ; i < self.arrHistory.count; i ++) {
            SHAlarmModel *model = self.arrHistory[i];
            NSString *str1 = model.strAlarm_alarm_msg;
            NSString *str2 = [model.strAlarm_create_date substringWithRange:NSMakeRange(model.strAlarm_create_date.length - 8, 8)];
            NSString *strTemp = [NSString stringWithFormat:@"%@    %@",str1,str2];
            [arrNeedList addObject:strTemp];
        }
        
        //        *rolTags = @[@"object",@"github",@"java/php"];
        *rolTitles = arrNeedList;
        *leftImage = @"zb_broadcast";
        //        *rightbuttonTitle = @"更多";
        *interval = 3.0;
        *titleFont = 14;
        //        *titleColor = RGB(244, 180, 40);
        *titleColor = [UIColor whiteColor];
        *isShowTagBorder = YES;
    }];
    
    _gmRollingView.delegate = self;
    [_gmRollingView dc_beginRolling];
    _gmRollingView.backgroundColor = [UIColor clearColor];
    [self.viewHistory addSubview:_gmRollingView];
    
    //    _gmRollingView.moreClickBlock = ^{
    //        NSLog(@"jd----more");
    //    };
    */
    [self.gmRollingView removeFromSuperview];
    @weakify(self);
    self.gmRollingView = [[DCTitleRolling alloc] initWithFrame:CGRectMake(-10, 0, self.viewHistory.frame.size.width + 10, 44) WithTitleData:^(CDDRollingGroupStyle *rollingGroupStyle, NSString *__autoreleasing *leftImage, NSArray *__autoreleasing *rolTitles, NSArray *__autoreleasing *rolTags, NSArray *__autoreleasing *rightImages, NSString *__autoreleasing *rightbuttonTitle, NSInteger *interval, float *rollingTime, NSInteger *titleFont, UIColor *__autoreleasing *titleColor, BOOL *isShowTagBorder) {
        @strongify(self);
        NSMutableArray *arrNeedList = [NSMutableArray arrayWithCapacity:0];
        
        for (int i = 0 ; i < self.arrHistory.count; i ++) {
            SHAlarmModel *model = self.arrHistory[i];
            NSString *str1 = model.strAlarm_alarm_msg;
            NSString *str2 = [model.strAlarm_create_date substringWithRange:NSMakeRange(model.strAlarm_create_date.length - 8, 8)];
            NSString *strTemp = [NSString stringWithFormat:@"%@    %@",str1,str2];
            [arrNeedList addObject:strTemp];
        }
        
//        *rolTags = @[@"object",@"github",@"java/php"];
        *rolTitles = arrNeedList;
        *leftImage = @"zb_broadcast";
        *interval = 3.0;
        *titleFont = 14;
        *titleColor = RGB(244, 180, 40);
        *isShowTagBorder = YES;
    }];
    self.gmRollingView.delegate = self;
    [self.gmRollingView dc_beginRolling];
    self.gmRollingView.backgroundColor = [UIColor clearColor];
     [self.viewHistory addSubview:self.gmRollingView];
    
    
    
    
}

#pragma mark - <CDDRollingDelegate>
- (void)dc_RollingViewSelectWithActionAtIndex:(NSInteger)index
{
//    LLog(@"点击了历史记录");
    
    if (self.BlockHistroyPressed) {
        self.BlockHistroyPressed();
    }
}


@end
