//
//  ScreenEditTableView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/24.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import "ScreenEditTableView.h"
#import "ScreenEditHeaderView.h"
#import "ScreenEditCell.h"
#import "ScreenEditDeviceCell.h"

typedef enum {
    ScreenEditTableTypePeform                = 0,
    ScreenEditTableTypeLinkage               = 1,
}ScreenEditTableType;

@interface ScreenEditTableView ()<UITableViewDelegate,UITableViewDataSource,UITextFieldDelegate>

@property (assign, nonatomic) ScreenEditTableType screenEditTableType;

@end

@implementation ScreenEditTableView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style
{
    self = [super initWithFrame:frame style:style];
    if (self) {
        self.delegate = self;
        self.dataSource = self;
        self.showsVerticalScrollIndicator = NO;//隐藏滚动条
        [self setSeparatorStyle:UITableViewCellSeparatorStyleNone];
        self.backgroundColor = [UIColor clearColor];
        self.screenEditTableType = ScreenEditTableTypePeform;
    }
    return self;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    if (self.screenEditTableType == ScreenEditTableTypeLinkage) {
        return 3;
    }else{
        return 2;
    }
}


-(void)setEditPropertyModel:(ScreenEditPropertyModel *)editPropertyModel
{
    _editPropertyModel = editPropertyModel;
    
    NSString *strIsLinkage = editPropertyModel.strScreenIsLinkage;
    if ([strIsLinkage intValue] == 1) {
        self.screenEditTableType = ScreenEditTableTypeLinkage;
    }else{
        self.screenEditTableType = ScreenEditTableTypePeform;
    }
    
    [self reloadData];
}

-(void)setArrLinkageDeviceList:(NSArray *)arrLinkageDeviceList
{
    _arrLinkageDeviceList = arrLinkageDeviceList;
    [self reloadData];
}

-(void)setArrPerformDeviceList:(NSArray *)arrPerformDeviceList
{
    _arrPerformDeviceList = arrPerformDeviceList;
    [self reloadData];
}


- (void)setScreenEditTableType:(ScreenEditTableType)screenEditTableType
{
    _screenEditTableType = screenEditTableType;
    [self reloadData];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
    if (self.screenEditTableType == ScreenEditTableTypeLinkage) {
        if (section == 0) {
            return 1;
        }else if (section == 1){
            return self.arrLinkageDeviceList.count;
        }else if (section == 2){
            return self.arrPerformDeviceList.count;
        }else
            return 0;
    }else{
        if (section == 0) {
            return 1;
        }else if (section == 1){
            return self.arrPerformDeviceList.count;
        }else
            return 0;
    }
    
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (self.screenEditTableType == ScreenEditTableTypeLinkage) {
        //联动
        switch (indexPath.section) {
            case 0:
            {
                ScreenEditCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ScreenEditCell"];
                if (!cell) {
                    
                    cell = [[[NSBundle mainBundle] loadNibNamed:@"ScreenEditCell" owner:nil options:nil] firstObject];
                }
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                cell.textFieldName.delegate = self;
                cell.editProperty = self.editPropertyModel;
                [self doHanleAddFirstCellAction:cell];
                return cell;
            }
                break;
            case 1:
            {
                ScreenEditDeviceCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ScreenEditDeviceCell"];
                if (!cell) {
                    cell = [[[NSBundle mainBundle] loadNibNamed:@"ScreenEditDeviceCell" owner:nil options:nil] firstObject];
                    
                }
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                if (indexPath.row == 0) {
                    cell.imageViewTopLine.hidden = NO;
                    
                }else if(indexPath.row == self.arrLinkageDeviceList.count - 1){
                    
                    cell.imageViewTopLine.hidden = NO;
                }else{
                    cell.imageViewTopLine.hidden = NO;
                }
                
                cell.imageViewBg.image =[UIImage imageNamed:@"联动背景02"];
                
                SHModelDevice *device = self.arrLinkageDeviceList[indexPath.row];
                cell.labelDeviceName.text = device.strDevice_device_name;
                cell.labelLocation.text = [NSString stringWithFormat:@"%@%@",device.strDevice_floor_name,device.strDevice_room_name];
                cell.labelDetail.text = @"暂无描述";
                NSString *strUnUrl = [[SHAppCommonRequest shareInstance] doGetDeviceGrayUIPicWithPicName:device.strDevice_image];
                [cell.imageViewIcon sd_setImageWithURL:[NSURL URLWithString:strUnUrl]];
                
                [self doHandleSwitchOn:device switchTr:cell.switchDevice];
                
                @weakify(self);
                [cell setBlockDeviceSwitchPressed:^(UISwitch *switchTr){
                    @strongify(self);
                    if (self.blockLinkageOrPerformDeviceSwitchPressed) {
                        self.blockLinkageOrPerformDeviceSwitchPressed(device,ScreenEditAddDeviceTypeLinkage,switchTr);
                    }
                }];
                
                return cell;
            }
                break;
            case 2:
            {
                ScreenEditDeviceCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ScreenEditDeviceCell"];
                if (!cell) {
                    
                    cell = [[[NSBundle mainBundle] loadNibNamed:@"ScreenEditDeviceCell" owner:nil options:nil] firstObject];
                }
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                if (indexPath.row == 0) {
                    cell.imageViewTopLine.hidden = NO;
                }else if(indexPath.row == self.arrPerformDeviceList.count - 1){
                    
                    cell.imageViewTopLine.hidden = NO;
                }else{
                    
                    cell.imageViewTopLine.hidden = NO;
                }
                cell.imageViewBg.image =[UIImage imageNamed:@"联动背景02"];
                SHModelDevice *device = self.arrPerformDeviceList[indexPath.row];
                cell.labelDeviceName.text = device.strDevice_device_name;
                cell.labelLocation.text = [NSString stringWithFormat:@"%@%@",device.strDevice_floor_name,device.strDevice_room_name];
                cell.labelDetail.text = @"暂无状态";
                
                NSString *strUnUrl = [[SHAppCommonRequest shareInstance] doGetDeviceGrayUIPicWithPicName:device.strDevice_image];
                [cell.imageViewIcon sd_setImageWithURL:[NSURL URLWithString:strUnUrl]];
                
                [self doHandleSwitchOn:device switchTr:cell.switchDevice];

                @weakify(self);
                [cell setBlockDeviceSwitchPressed:^(UISwitch *switchTr){
                    @strongify(self);
                    if (self.blockLinkageOrPerformDeviceSwitchPressed) {
                        self.blockLinkageOrPerformDeviceSwitchPressed(device,ScreenEditAddDeviceTypePeform, switchTr);
                    }
                }];
                
                return cell;
            }
                break;
                
            default:
                return nil;
                break;
        }
        
    }else{
        //非联动
        switch (indexPath.section) {
            case 0:
            {
                ScreenEditCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ScreenEditCell"];
                if (!cell) {
                    
                    cell = [[[NSBundle mainBundle] loadNibNamed:@"ScreenEditCell" owner:nil options:nil] firstObject];
                }
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                cell.textFieldName.delegate =self;
                cell.editProperty = self.editPropertyModel;
                
                [self doHanleAddFirstCellAction:cell];
                return cell;
            }
                break;
            case 1:
            {
                ScreenEditDeviceCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ScreenEditDeviceCell"];
                if (!cell) {
                    cell = [[[NSBundle mainBundle] loadNibNamed:@"ScreenEditDeviceCell" owner:nil options:nil] firstObject];
                    
                }
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                if (indexPath.row == 0) {
                    cell.imageViewTopLine.hidden = NO;
                    
                }else if(indexPath.row == self.arrPerformDeviceList.count - 1){
                    
                    cell.imageViewTopLine.hidden = NO;
                }else{
                    cell.imageViewTopLine.hidden = NO;
                }
                cell.imageViewBg.image =[UIImage imageNamed:@"联动背景02"];
                
                SHModelDevice *device = self.arrPerformDeviceList[indexPath.row];
                cell.labelDeviceName.text = device.strDevice_device_name;
                cell.labelLocation.text = [NSString stringWithFormat:@"%@%@",device.strDevice_floor_name,device.strDevice_room_name];
                cell.labelDetail.text = @"暂无状态";
                NSString *strUnUrl = [[SHAppCommonRequest shareInstance] doGetDeviceGrayUIPicWithPicName:device.strDevice_image];
                //    NSString *strPrUrl = [[SHAppCommonRequest shareInstance] doGetDeviceHighLightUIPicWithPicName:deviceModel.strDevice_image];
                [cell.imageViewIcon sd_setImageWithURL:[NSURL URLWithString:strUnUrl]];
                
                [self doHandleSwitchOn:device switchTr:cell.switchDevice];
//                if (device.iDevice_device_state1 == 2) {
//                    [cell.switchDevice setOn:NO];
//                }else{
//                    [cell.switchDevice setOn:YES];
//                }
                @weakify(self);
                [cell setBlockDeviceSwitchPressed:^(UISwitch *switchTr){
                    @strongify(self);
                    if (self.blockLinkageOrPerformDeviceSwitchPressed) {
                        self.blockLinkageOrPerformDeviceSwitchPressed(device, ScreenEditAddDeviceTypePeform,switchTr);
                    }
                }];
                
                return cell;
            }
                break;
            default:
                return nil;
                break;
        }
    }
    
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (self.screenEditTableType == ScreenEditTableTypeLinkage) {
        
        switch (indexPath.section) {
            case 0:
                return 254;
                break;
            case 1:
            {
                if (indexPath.row == self.arrPerformDeviceList.count - 1) {
                    return 85;
                }else
                    return 80;
            }
                break;
            case 2:
            {
                if (indexPath.row == self.arrPerformDeviceList.count - 1) {
                    return 85;
                }else
                    return 80;
            }
                break;
            default:
                return 0;
                break;
        }
        
    }else{
        
        switch (indexPath.section) {
            case 0:
                return 172;
                break;
            case 1:
            {
                if (indexPath.row == self.arrPerformDeviceList.count - 1) {
                    return 85;
                }else
                    return 80;
            }
                break;
            default:
                return 0;
                break;
        }
    }
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    
    if (self.screenEditTableType == ScreenEditTableTypeLinkage) {
        if (section == 0) {
            return 0.01;
        }else if (section == 1){
            return 54;
        }else if(section == 2){
            return 54;
        } else {
            return 0.01;
        }
    }else{
        if (section == 0) {
            return 0.01;
        }else if (section == 1){
            return 54;
        }else {
            return 0.01;
        }
    }
    
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 6.0f;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    
    if (self.screenEditTableType == ScreenEditTableTypeLinkage) {
        if (section == 1) {
            ScreenEditHeaderView *view = [[ScreenEditHeaderView alloc] init];
            view.imageViewIcon.image = [UIImage imageNamed:@"联动"];
            view.labelName.text = @"联动设备列表";
            view.imageViewHeaderBg.image = [UIImage imageNamed:@"联动背景01"];
            @weakify(self);
            [view setBlockScreenEditHeaderViewAddDevicePressed:^{
                @strongify(self);
                if (self.blockAddDeivcePressed) {
                    self.blockAddDeivcePressed(ScreenEditAddDeviceTypeLinkage);
                }
            }];
            return view;
        }else if(section == 2){
            ScreenEditHeaderView *view = [[ScreenEditHeaderView alloc] init];
            view.imageViewIcon.image = [UIImage imageNamed:@"执行"];
            view.labelName.text = @"执行设备列表";
            view.imageViewHeaderBg.image = [UIImage imageNamed:@"联动背景01"];
            @weakify(self);
            [view setBlockScreenEditHeaderViewAddDevicePressed:^{
                @strongify(self);
                if (self.blockAddDeivcePressed) {
                    self.blockAddDeivcePressed(ScreenEditAddDeviceTypePeform);
                }
            }];
            return view;
        } else {
            
            UIView *view = [UIView new];
            return view;
        }
    }else{
        if(section == 1){
            ScreenEditHeaderView *view = [[ScreenEditHeaderView alloc] init];
            view.imageViewIcon.image = [UIImage imageNamed:@"执行"];
            view.labelName.text = @"执行设备列表";
            view.imageViewHeaderBg.image = [UIImage imageNamed:@"联动背景01"];
            @weakify(self);
            [view setBlockScreenEditHeaderViewAddDevicePressed:^{
                @strongify(self);
                if (self.blockAddDeivcePressed) {
                    self.blockAddDeivcePressed(ScreenEditAddDeviceTypePeform);
                }
            }];
            return view;
        } else {
            
            UIView *view = [UIView new];
            return view;
        }
    }
    
    
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    if (self.screenEditTableType == ScreenEditTableTypeLinkage) {
        
        if (section == 1) {
            UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, self.frame.size.width, 6)];
            UIImageView *imageV = [[UIImageView alloc] initWithFrame:view.frame];
            imageV.image = [UIImage imageNamed:@"联动背景03"];
            [view addSubview:imageV];
            view.backgroundColor = [UIColor clearColor];
            return view;
        } else if (section == 2){
            
            UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, self.frame.size.width, 6)];
            UIImageView *imageV = [[UIImageView alloc] initWithFrame:view.frame];
            imageV.image = [UIImage imageNamed:@"联动背景03"];
            [view addSubview:imageV];
            view.backgroundColor = [UIColor clearColor];
            return view;
            
        }else {
            
            UIView *view = [UIView new];
            return view;
        }
        
    }else{
        if (section == 1) {
            UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, self.frame.size.width, 6)];
            UIImageView *imageV = [[UIImageView alloc] initWithFrame:view.frame];
            imageV.image = [UIImage imageNamed:@"联动背景03"];
            [view addSubview:imageV];
            view.backgroundColor = [UIColor clearColor];
            return view;
        } else {
            
            UIView *view = [UIView new];
            return view;
        }
    }
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:NO];
    
    
    if (self.screenEditTableType == ScreenEditTableTypeLinkage) {
    }else{
    }
    
    
    if (self.didSelectedRowAtIndexPath) {
        self.didSelectedRowAtIndexPath(indexPath);
    }
    
    switch (indexPath.section) {
        case 0:
            break;
        case 1:
        {
            
        }
            break;
        case 2:
        {
            
        }
            break;
            
        default:
            break;
    }
}


#pragma mark -
#pragma mark - firstCellAction

- (void)doHanleAddFirstCellAction:(ScreenEditCell *)cell
{
    @weakify(self)
    [cell setBlockSwitchPressed:^(int iTag,BOOL isButtonOn){
        @strongify(self);
        if (self.blockFirstCellSwitchPressed) {
            self.blockFirstCellSwitchPressed(iTag, isButtonOn);
        }
    }];
    
    [cell setBlockScreenIconPressed:^(UIButton *btn){
        @strongify(self);
        if (self.blockFirstCellScreenIconPressed) {
            self.blockFirstCellScreenIconPressed(btn);
        }
    }];
    
    [cell setBlockDelayedTextfieldPressed:^(UITextField *textF){
        @strongify(self);
        if (self.blockFirstCellDelayedTextfieldPressed) {
            self.blockFirstCellDelayedTextfieldPressed(textF);
        }
    }];
    
    [cell setBlockTimerTextfieldPressed:^(UITextField *textF){
        @strongify(self);
        if (self.blockFirstCellTimerTextfieldPressed) {
            self.blockFirstCellTimerTextfieldPressed(textF);
        }
    }];
}

#pragma mark -
#pragma mark - textFieldDelegate
- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    ScreenEditPropertyModel *propertyModel = [ScreenEditPropertyModel new];
    propertyModel.strScreeenEditIconUrl     = self.editPropertyModel.strScreeenEditIconUrl;
    propertyModel.strScreenEditName         = textField.text;
    propertyModel.strScreenIsLinkage        = self.editPropertyModel.strScreenIsLinkage;
    propertyModel.strScreenEditDelayed      = self.editPropertyModel.strScreenEditDelayed;
    propertyModel.strScreenEditTimer        = self.editPropertyModel.strScreenEditTimer;
    propertyModel.strScreenEditArming       = self.editPropertyModel.strScreenEditArming;
    propertyModel.strScreenEditDisarming    = self.editPropertyModel.strScreenEditDisarming;//默认撤防有效
    propertyModel.iScreen_need_timing       = self.editPropertyModel.iScreen_need_timing;
    propertyModel.iScreen_need_time_delay   = self.editPropertyModel.iScreen_need_time_delay;
    propertyModel.str_linkage_time          = self.editPropertyModel.str_linkage_time;
    self.editPropertyModel = propertyModel;
}


#pragma mark -
#pragma mark - private
- (void)doHandleSwitchOn:(SHModelDevice *)device switchTr:(UISwitch *)sender
{
    if ([device.strDevice_device_OD isEqualToString:@"0F AA"]) {
        if ([device.strDevice_device_type isEqualToString:@"07"]){
            if ([device.strDevice_category isEqualToString:@"02"]||[device.strDevice_category isEqualToString:@"04"]) {
                NSLog(@"三路灯开关或者多联多控");
                if ([device.strDevice_other_status intValue] == 1)
                {
                    if (device.iDevice_device_state1 == 1)
                    {
                        sender.on = YES;
                    }else
                        sender.on = NO;
                }
                else if ([device.strDevice_other_status intValue] == 2)
                {
                    if (device.iDevice_device_state2 == 1) {
                        sender.on = YES;
                    }else
                        sender.on = NO;
                    
                }
                else if ([device.strDevice_other_status intValue] == 3)
                {
                    if (device.iDevice_device_state3 == 1) {
                        sender.on = YES;
                    }else
                        sender.on = NO;
                }
            }
        }else if ([device.strDevice_device_type isEqualToString:@"06"]){
            
                if ([device.strDevice_category isEqualToString:@"02"]||[device.strDevice_category isEqualToString:@"04"]) {
                    NSLog(@"二路灯开关或者二路多联多控");
                    if ([device.strDevice_other_status intValue] == 1) {
                        if (device.iDevice_device_state1 == 1) {
                            sender.on = YES;
                        }else
                            sender.on = NO;
                    }else if ([device.strDevice_other_status intValue] == 2){
                        if (device.iDevice_device_state2 == 1) {
                            sender.on = YES;
                        }else
                            sender.on = NO;
                    }
                }
            
        }else if ([device.strDevice_device_type isEqualToString:@"81"]){
            
            if (device.iDevice_device_state1 == 1) {
                sender.on = YES;
            }else
                sender.on = NO;
            NSLog(@"SOS设备");
            
        }else{
            if (device.iDevice_device_state1 == 1) {
                sender.on = YES;
            }else
                sender.on = NO;
            NSLog(@"其它设备");
        }
    }else if ([device.strDevice_device_OD isEqualToString:@"0F BE"]){
    
        if (device.iDevice_device_state1 == 1) {
            sender.on = YES;
        }else
            sender.on = NO;
        
    }else if ([device.strDevice_device_OD isEqualToString:@"0F E6"]){
        
        if ([device.strDevice_device_type isEqualToString:@"02"]){
            
            if ([device.strDevice_category isEqualToString:@"02"]){
                NSLog(@"红外学习仪");
                if ([device.strDevice_sindex_length intValue] == SHSindexLength_InfraredAirCondition) {
                    NSLog(@"空调");
                    if (device.iDevice_device_state1 == 1) {
                        sender.on = YES;
                    }else
                        sender.on = NO;
                }else if ([device.strDevice_sindex_length intValue] == SHSindexLength_InfraredOther_Other){
                    NSLog(@"其它遥控设备");
                    sender.on = YES;
                }else{
                    sender.on = YES;
                }
            }else {
            
                if (device.iDevice_device_state1 == 1) {
                    sender.on = YES;
                }else
                    sender.on = NO;
                
            }
        }
    }else if ([device.strDevice_device_OD isEqualToString:@"0F C8"]){
    
        if (device.iDevice_device_state1 == 1) {
            sender.on = YES;
        }else
            sender.on = NO;
    }
}



@end
