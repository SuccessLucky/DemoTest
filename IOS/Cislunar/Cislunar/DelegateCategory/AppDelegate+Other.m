//
//  AppDelegate+Other.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/14.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "AppDelegate+Other.h"
#import "SELUpdateAlert.h"

@implementation AppDelegate (Other)
- (void)doGetVisonUpdate
{
    /** 添加更新提示 */
   [SELUpdateAlert showUpdateAlertWithVersion:Major_Version Descriptions:@[@"1.xxxxxxxxxx",@"2.xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",@"3.xxxxxxxxxx",@"4.xxxxxxxxxx"]];
    
    
}

- (void)doStartLogDetection
{
    [[LLDebugTool sharedTool] startWorking];
    
    NSLog(@"走了delegate");
}


#pragma mark -
#pragma mark -private

-(void)IterationVersion {
    
    NSDictionary *infoDic = [[NSBundle mainBundle] infoDictionary];
    
    NSString *currentVersion = [infoDic objectForKey:@"CFBundleShortVersionString"];
    
    NSString *URL = @"https://itunes.apple.com/lookup?id=1438339230";
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    
    [request setURL:[NSURL URLWithString:URL]];
    
    [request setHTTPMethod:@"POST"];
    
    NSOperationQueue *queue = [[NSOperationQueue alloc] init];
    
    [NSURLConnection sendAsynchronousRequest:request queue:queue completionHandler:^(NSURLResponse * _Nullable response, NSData * _Nullable data, NSError * _Nullable connectionError) {
        
        if (data != nil) {
            
            NSMutableDictionary *jsondata = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableLeaves error:nil];
            
            NSArray *infoArray = [jsondata objectForKey:@"results"];
            
            if ([infoArray count]) {
                
                NSDictionary *releaseInfo = [infoArray objectAtIndex:0];
                
                NSString *lastVersion = [releaseInfo objectForKey:@"version"];
                
                NSString *trackViewUrl = [releaseInfo objectForKey:@"trackViewUrl"];
                
                self.trackViewUrl = trackViewUrl;
                
                int lastVersionnum = [[lastVersion stringByReplacingOccurrencesOfString:@"." withString:@""]intValue];
                
                int currentVersionnum = [[currentVersion stringByReplacingOccurrencesOfString:@"." withString:@""] intValue];
                
                if (lastVersionnum > currentVersionnum) {
                    
                    dispatch_sync(dispatch_get_main_queue(), ^(){

                        [UIAlertView bk_showAlertViewWithTitle:@"更新" message:@"有新的版本更新，是否前往更新？" cancelButtonTitle:@"取消" otherButtonTitles:@[@"更新"] handler:^(UIAlertView *alertView, NSInteger buttonIndex) {
                            if (buttonIndex == 1) {
                                NSURL *url = [NSURL URLWithString:self.trackViewUrl];
                                [[UIApplication sharedApplication]openURL:url];
                            }
                        }];
                    });
                }
            }
        }
    }];
    
}



@end
