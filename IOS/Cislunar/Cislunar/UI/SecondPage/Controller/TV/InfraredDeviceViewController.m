//
//  InfraredDeviceViewController.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/14.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "InfraredDeviceViewController.h"
#import "InfraredUICollectionView.h"
#import "SHInfraredHoueNOManager.h"

@interface InfraredDeviceViewController ()<UINavigationControllerDelegate>

@property (strong, nonatomic) InfraredUICollectionView *ifraredCollectionView;

@property (weak, nonatomic) IBOutlet UIButton *btnBack;

@property (weak, nonatomic) IBOutlet UILabel *labelTitle;

@property (weak, nonatomic) IBOutlet UIButton *btnAdd;

@property (strong, nonatomic) SHInfraredHoueNOManager *manager;

@property (assign, nonatomic) NetworkEngine *networkEngine;

@end

@implementation InfraredDeviceViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    if (self.itype == 0) {
        self.tabBarController.tabBar.hidden = YES;
    }
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    if (self.itype == 0) {
        self.tabBarController.tabBar.hidden = NO;
    }
}


- (void)viewDidLoad {
    [super viewDidLoad];
    [self doInitSubViews];
    [self doRegisterKVO];
    [self doAddAction];
    [self doLoadData];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)doInitSubViews
{
    self.networkEngine = [NetworkEngine shareInstance];
    self.labelTitle.text = self.device.strDevice_device_name;
    
    if (self.isCouldStudy) {
        self.btnAdd.hidden = NO;
    }else{
        self.btnAdd.hidden = YES;
    }
    
    [self.btnBack setEnlargeEdge:20];
    [self.btnAdd setEnlargeEdge:20];
    
    [self.view addSubview:self.ifraredCollectionView];
    [self doAddIfraredCollectionViewConstraints];
}

- (void)doRegisterKVO
{
    //监测设备上报添加
    [self observeKeyPath:@keypath(self.networkEngine.strStudyCode) block:^(id value) {
        NSString * strCode = (NSString *)value;
        [XWHUDManager hideInWindow];
        if ([strCode intValue] == SHStudyCodeSucc) {
              [XWHUDManager showSuccessTipHUD:@"学习成功"];
        }else if ([strCode intValue] == SHStudyCodeFail){
            [XWHUDManager showErrorTipHUD:@"学习失败"];
        }else{
            NSLog(@"红外strCode == %@",strCode);
        }
        
    }];
}

- (void)doAddAction
{
    @weakify(self);
    [self.ifraredCollectionView setDidSelectedItemBlock:^(NSIndexPath *indexPath, InfraredUICollectionViewActionType type, SHInfraredKeyModel *keyModel)
     {
         @strongify(self);
         switch (type) {
             case InfraredUICollectionView_Common:
             {
                 NSLog(@"普通按下NO=%@",keyModel.strWarehouseNO);
                  [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
                 dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                     [XWHUDManager hideInWindow];
                 });
                 NSData *data = [self.networkEngine doGetInfraredSendDataTargetAddr:self.strDeviceMacAddr
                                                                          studyCode:[keyModel.strWarehouseNO intValue]];
                 [self.networkEngine sendRequestData:data];
             }
                 break;
             case InfraredUICollectionView_LongPressed:
             {
                 if (self.isCouldStudy) {
                     NSLog(@"长按");
                     [self doAddActionSheetAboutDeleteAndEdit:keyModel indexPath:indexPath];
                 }
             }
                 break;
             case InfraredUICollectionView_On:
             {
                 NSLog(@"按下开关");
                 NSData *data = [self.networkEngine doGetInfraredSendDataTargetAddr:self.strDeviceMacAddr
                                                                          studyCode:[keyModel.strWarehouseNO intValue]];
                 [self.networkEngine sendRequestData:data];
                 
             }
                 break;
                 
             default:
                 break;
         }
     }];
}

#pragma mark -
#pragma mark - loadData
- (void)doLoadData
{
    if (self.mutArrKeyBtns.count == 0) {
        SHInfraredKeyModel *model = [SHInfraredKeyModel new];
        model.iButton_id = -1;
        model.strName = @"开/关";
        //取还没有添加的仓库号的第一个
        model.strWarehouseNO = [self.mutArrHouseNO firstObject];
        [self.mutArrHouseNO removeObject:[self.mutArrHouseNO firstObject]];
        [self.mutArrKeyBtns addObject:model];
    }
    self.ifraredCollectionView.arrList = self.mutArrKeyBtns;
    [self.ifraredCollectionView reloadData];
}


#pragma mark -
#pragma mark - action

- (IBAction)btnBackPressed:(UIButton *)sender {
    
    if (self.isCouldStudy) {
        
        [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
        [self.manager handleTheAddInfraredBtnsDataWithDeviceId:self.iDeviceId
                                                      arrModel:self.mutArrKeyBtns
                                                completeHandle:^(BOOL success, id result)
         {
             [XWHUDManager hideInWindow];
             if (success) {
                  [XWHUDManager showSuccessTipHUD:@"添加成功"];
                 [self.navigationController popViewControllerAnimated:YES];
             }else{
                 [XWHUDManager showErrorTipHUD:@"加载失败"];
                 [self.navigationController popViewControllerAnimated:YES];
             }
         }];
        
    }else{
        [self.navigationController popViewControllerAnimated:YES];
    }
}


- (IBAction)btnAddPressed:(UIButton *)sender {
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"添加按键" message:nil preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    
    @weakify(self);
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
        SHInfraredKeyModel *model = [SHInfraredKeyModel new];
        
        model.iButton_id = -1;
        
        UITextField *textFiled = alertController.textFields.firstObject;
        model.strName = textFiled.text;
        
        //取还没有添加的仓库号的第一个
        model.strWarehouseNO = [self.mutArrHouseNO firstObject];
        [self.mutArrHouseNO removeObject:[self.mutArrHouseNO firstObject]];
        
        [self addNewItem:model];
        
        [self.mutArrKeyBtns addObject:model];
        
    }];
    [alertController addTextFieldWithConfigurationHandler:^(UITextField *textField){
        textField.placeholder = @"请输入按键的名字";
    }];
    
    [alertController  addAction:cancelAction];
    [alertController addAction:okAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

-(void)addNewItem:(SHInfraredKeyModel *)model{
    
    NSMutableArray *mutArr = [NSMutableArray new];
    [mutArr addObjectsFromArray:self.ifraredCollectionView.arrList];
    [mutArr addObject:model];
    self.ifraredCollectionView.arrList = mutArr;
    @weakify(self);
    [self.ifraredCollectionView performBatchUpdates:^{
        @strongify(self);
        NSIndexPath *indexPath = [NSIndexPath indexPathForItem:mutArr.count-1 inSection:0];
        [self.ifraredCollectionView insertItemsAtIndexPaths:@[indexPath]];
    }completion:nil];
}

- (void)reloadNewItemWithIndexPath:(NSIndexPath *)indexPath
{
    self.ifraredCollectionView.arrList = self.mutArrKeyBtns;
    @weakify(self);
    [self.ifraredCollectionView performBatchUpdates:^{
        @strongify(self);
        [self.ifraredCollectionView insertItemsAtIndexPaths:@[indexPath]];
    }completion:nil];
}

#pragma mark -
#pragma mark - 编辑和删除
- (void)doAddActionSheetAboutDeleteAndEdit:(SHInfraredKeyModel *)keyModel indexPath:(NSIndexPath *)indexPath
{
    @weakify(self);
    
    LCActionSheet *actionSheetEdit = [LCActionSheet sheetWithTitle:@"删除无法恢复"
                                                 cancelButtonTitle:@"删除"
                                                           clicked:^(LCActionSheet * _Nonnull actionSheet, NSInteger buttonIndex) {
                                                               @strongify(self);
                                                               switch (buttonIndex) {
                                                                   case 1:
                                                                   {
                                                                       NSLog(@"走了学习");
                                                                       [XWHUDManager showHUDMessage:@"学习中..." afterDelay:20];
                                                                       dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(20 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                                                                           [XWHUDManager hideInWindow];
                                                                       });
                                                                       
                                                                       NSData *data = [[NetworkEngine shareInstance] doGetInfraredStudyDataTargetAddr:self.strDeviceMacAddr studyCode:[keyModel.strWarehouseNO intValue]];
                                                                       [[NetworkEngine  shareInstance] sendRequestData:data];
                                                                   }
                                                                       break;
                                                                   case 2:
                                                                   {
                                                                       NSLog(@"走了编辑");
                                                                       [self doChangeTheInfraredBtns:keyModel indexPath:indexPath];
                                                                       
                                                                   }
                                                                       break;
                                                                   case 0:
                                                                   {
                                                                       NSLog(@"走了删除");
                                                                       
                                                                       if ([keyModel.strName isEqualToString:@"开/关"]) {
                                                                           [XWHUDManager showWarningTipHUD:@"请勿删除开关"];
                                                                           dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                                                                               [XWHUDManager hideInWindow];
                                                                           });
                                                                       }else{
                                                                           [self.mutArrKeyBtns removeObject:keyModel];
                                                                       }
                                                                       
                                                                       
                                                                       self.ifraredCollectionView.arrList = self.mutArrKeyBtns;
                                                                       [self.ifraredCollectionView reloadData];
                                                                   }
                                                                       break;
                                                                   case 3:
                                                                   {
                                                                       NSLog(@"走了取消");
                                                                   }
                                                                       break;
                                                                       
                                                                   default:
                                                                       break;
                                                               }
                                                               
                                                           }
                                                 otherButtonTitles:@"学习",@"编辑", nil];
    [actionSheetEdit show];
    
    /*
    LCActionSheet *actionSheetEdit = [LCActionSheet sheetWithTitle:@"删除无法恢复"
                                                      buttonTitles:@[@"学习",@"编辑",@"删除"]
                                                    redButtonIndex:2
                                                           clicked:^(NSInteger buttonIndex)
                                      {
                                          @strongify(self);
                                          switch (buttonIndex) {
                                              case 0:
                                              {
                                                  NSLog(@"走了学习");
                                                  [[GAlertMessageManager shareInstance] showMessag:@"学习中..." delayTime:20];
                                                  NSData *data = [[NetworkEngine shareInstance] doGetInfraredStudyDataTargetAddr:self.strDeviceMacAddr studyCode:[keyModel.strWarehouseNO intValue]];
                                                  [[NetworkEngine  shareInstance] sendRequestData:data];
                                              }
                                                  break;
                                              case 1:
                                              {
                                                  NSLog(@"走了编辑");
                                                  [self doChangeTheInfraredBtns:keyModel indexPath:indexPath];
                                                  
                                              }
                                                  break;
                                              case 2:
                                              {
                                                  NSLog(@"走了删除");
                                                  
                                                  if ([keyModel.strName isEqualToString:@"开/关"]) {
                                                      [[GAlertMessageManager shareInstance] showMessag:@"请勿删除开关" delayTime:1];
                                                  }else{
                                                      [self.mutArrKeyBtns removeObject:keyModel];
                                                  }
                                                  
                                                  
                                                  self.ifraredCollectionView.arrList = self.mutArrKeyBtns;
                                                  [self.ifraredCollectionView reloadData];
                                              }
                                                  break;
                                              case 3:
                                              {
                                                  NSLog(@"走了取消");
                                              }
                                                  break;
                                                  
                                              default:
                                                  break;
                                          }
                                      }];
     
    [actionSheetEdit show];*/
}

#pragma mark -
#pragma mark - 修改红外按钮
- (void)doChangeTheInfraredBtns:(SHInfraredKeyModel *)keyModel indexPath:(NSIndexPath *)indexPath
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"修改按键" message:nil preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    
    @weakify(self);
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
        SHInfraredKeyModel *model = [SHInfraredKeyModel new];
        
        model.iButton_id = keyModel.iButton_id;
        
        UITextField *textFiled = alertController.textFields.firstObject;
        model.strName = textFiled.text;
        
        model.strWarehouseNO = keyModel.strWarehouseNO;
        
        //        [self reloadNewItemWithIndexPath:indexPath];
        [self.mutArrKeyBtns replaceObjectAtIndex:indexPath.row withObject:model];
        self.ifraredCollectionView.arrList = self.mutArrKeyBtns;
        [self.ifraredCollectionView reloadData];
        
    }];
    [alertController addTextFieldWithConfigurationHandler:^(UITextField *textField){
        textField.placeholder = @"请输入按键的名字";
    }];
    
    [alertController  addAction:cancelAction];
    [alertController addAction:okAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}


#pragma mark -
#pragma mark - 编辑删除添加
- (void)doHandleAddToNetwork
{
#warning 最好能加一个  没有动作的时候不要走网络添加 可考虑增加一个 增加的按钮
    @weakify(self);
    dispatch_async(dispatch_get_main_queue(), ^{
        @strongify(self);
        [self.manager handleTheAddInfraredBtnsDataWithDeviceId:self.iDeviceId
                                                      arrModel:self.mutArrKeyBtns
                                                completeHandle:^(BOOL success, id result)
         {
             [XWHUDManager hideInWindow];
             if (success) {
                  [XWHUDManager showSuccessTipHUD:@"操作成功"];
             }else{
                 
                 [XWHUDManager showErrorTipHUD:@"操作失败"];
             }
         }];
    });
    
}


#pragma mark -
#pragma mark - init

- (SHInfraredHoueNOManager *)manager
{
    if (!_manager) {
        _manager = [SHInfraredHoueNOManager new];
    }
    return _manager;
}

- (InfraredUICollectionView *)ifraredCollectionView
{
    if (!_ifraredCollectionView) {
        UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
        flowLayout.sectionHeadersPinToVisibleBounds = YES;
        [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
        _ifraredCollectionView = [[InfraredUICollectionView alloc] initWithFrame:CGRectMake(0,
                                                                                            64,
                                                                                            UI_SCREEN_WIDTH,
                                                                                            UI_SCREEN_HEIGHT - 64 - 49)
                                                            collectionViewLayout:flowLayout];
    }
    return _ifraredCollectionView;
}

- (void)doAddIfraredCollectionViewConstraints
{
    @weakify(self);
    [self.ifraredCollectionView mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.top.equalTo(self.view.mas_top).with.offset(64);
        make.left.equalTo(self.view.mas_left);
        make.right.equalTo(self.view.mas_right);
        make.bottom.equalTo(self.view.mas_bottom);
    }];
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
