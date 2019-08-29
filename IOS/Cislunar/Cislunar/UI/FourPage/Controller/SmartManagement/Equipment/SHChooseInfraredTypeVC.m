//
//  SHChooseInfraredTypeVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/27.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHChooseInfraredTypeVC.h"

@interface SHChooseInfraredTypeVC ()<UITableViewDataSource, UITableViewDelegate>

@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (nonatomic, strong) NSArray *dataArray;
@property (nonatomic, strong) NSArray *typeArray;

@end

@implementation SHChooseInfraredTypeVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"选择类型";
    
//    self.tableView.tableFooterView = [[UIView alloc] init];
//    self.dataArray = @[@"电视", @"空调", @"DVD", @"机顶盒", @"投影", @"音响", @"风扇"];
//    self.typeArray = @[kTVDeviceType, kAirConditonDeviceType, kDVDType, kJDHType, kTYYType, kYXType, kFSType];
//    [self.tableView setSeparatorInset:UIEdgeInsetsMake(0, 0, 0, 0)];
}

//- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
//    
//    return self.dataArray.count;
//}
//
//- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
//    
//    return 60.0f;
//}
//
//- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
//    
//    static NSString *cellIdentifier = @"Cell";
//    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
//    if (!cell) {
//        
//        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
//        cell.selectionStyle = UITableViewCellSelectionStyleNone;
//    }
//    
//    cell.textLabel.text = self.dataArray[indexPath.row];
//    cell.imageView.image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:[NSString stringWithFormat:@"hw%d@2x",(int)indexPath.row + 1] ofType:@"png"]];
//    return cell;
//}
//
//- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
//    
//    
//}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
