//
//  RoomManagementVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "RoomManagementVC.h"
#import "RoomCollectionView.h"
#import "SmartEquipmentBannerView.h"
#import "DropDownListView.h"
#import "CommonAlterView.h"
#import "SHRoomManager.h"
#import "DeviceManagementVC.h"

@interface RoomManagementVC ()<UINavigationControllerDelegate>

@property (nonatomic, strong) RoomCollectionView *collectionView;
@property (strong, nonatomic) SmartEquipmentBannerView *bannerView;
@property (strong, nonatomic) DropDownListView *listViewPop;
@property (strong, nonatomic) SHRoomManager *roomManager;

@end

@implementation RoomManagementVC

#pragma mark - UINavigationControllerDelegate
- (void)navigationController:(UINavigationController *)navigationController
      willShowViewController:(UIViewController *)viewController
                    animated:(BOOL)animated {
    BOOL isHomePage = [viewController isKindOfClass:[self class]];
    
    [self.navigationController setNavigationBarHidden:isHomePage animated:YES];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self doInitSubViews];
    [self doRegisterKVO];
    [self registerBlock];
    [self doGetRoomListWithFloorID:self.iFloorID];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.navigationController.delegate = self;
}

- (void)doInitSubViews
{
    self.bannerView.btnBack.hidden = NO;
    self.bannerView.btnAdd.hidden = NO;
    
    [self.view addSubview:self.bannerView];
    [self doAddBannerConstraints];
    
    [self.bannerView.btnTitle setTitle:self.strFloorName forState:UIControlStateNormal];
    
    [self.view addSubview:self.collectionView];
    [self doAddCollectionViewConstraints];
 }

- (void)doGetRoomListWithFloorID:(int)iFloorID
{
    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    [self.roomManager doGetRoomListDataFromDBWithFloorID:iFloorID];
    [self.roomManager doGetRoomListFromNetworkWithFloorID:iFloorID];
}

- (void)doRegisterKVO
{
    @weakify(self);
    [self observeKeyPath:@keypath(self.roomManager.arrRoom)
                   block:^(id value) {
                       @strongify(self);
                       [XWHUDManager hideInWindow];
                       NSArray *arr = (NSArray *)value;
                       self.collectionView.hidden = NO;
                       self.collectionView.arrList = [NSMutableArray arrayWithArray:arr];
                       [self.collectionView reloadData];
                   }];
    
    [self observeKeyPath:@keypath(self.roomManager.errorInfo)
                   block:^(id value) {
                       [XWHUDManager hideInWindow];
                       NSDictionary *dict = (NSDictionary *)value;
                       [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",dict[@"message"]]];
                       
                   }];
}


#pragma mark -
#pragma mark - add action
- (void)registerBlock
{
    @weakify(self);
    [self.collectionView setDidSelectedItemBlock:^(NSIndexPath *indexPath, RoomCollectionViewActionType type,SHModelRoom *modelRoom) {
        @strongify(self);
        switch (type) {
            case RoomCollectionViewActionType_Common:
            {
                NSLog(@"进行房间点击操作%ld",(long)indexPath.row);
                [self performSegueWithIdentifier:@"seg_to_DeviceManagementVC" sender:modelRoom];
            }
                break;
            case RoomCollectionViewActionType_LongPressed:
            {
                NSLog(@"长按操作%ld",(long)indexPath.row);
                [self doShowSheet:modelRoom];
            }
                break;
            case RoomCollectionViewActionType_Delete:
            {
                NSMutableArray *mutTemp = [NSMutableArray arrayWithArray:self.collectionView.arrList];
                [mutTemp removeObjectAtIndex:indexPath.row];
                self.collectionView.arrList = mutTemp;
                NSLog(@"删除指令%ld",(long)indexPath.row);
                [self.collectionView performBatchUpdates:^{
                    
                    [self.collectionView deleteItemsAtIndexPaths:@[indexPath]];
                } completion:^(BOOL finished) {
                    if (finished) {
                        //进行服务器删除操作
                    }
                }];
            }
                break;
            case RoomCollectionViewActionType_Add:
            {
                NSLog(@"进行房间添加操作%ld",(long)indexPath.row);
                NSArray *arrRoomPicList = [[SHAppCommonRequest shareInstance] doGetRoomUIPicAll];
                CommonAlterView *alter = [[CommonAlterView alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, UI_SCREEN_HEIGHT)
                                                                  dataSourceArr:arrRoomPicList
                                                                           type:CommonAlterViewType_Common];
                [alter setStrDefaultTitle:@"添加房间"];
                [alter popWithFatherView:self.view];
                [alter setBlockGetNameAndPic:^(NSString *strName, SHUIPicModel *picModel) {
                    //进行添加房间的操作
                    SHModelRoom *modelRoom = [SHModelRoom new];
                    modelRoom.iRoom_floor_id = self.iFloorID;
                    modelRoom.strRoom_name = strName;
                    modelRoom.strRoom_image = picModel.strUIPic_name;
                    
                    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
                    [self.roomManager handleTheAddRoomDataWithModel:modelRoom completeHandle:^(BOOL success, id result) {
                        
                        if (success) {
                            [self.roomManager doGetRoomListFromNetworkWithFloorID:self.iFloorID];
                        }else{
                            [XWHUDManager hideInWindow];
                            [XWHUDManager showErrorTipHUD:@"加载失败"];
                            
                        }
                    }];
                }];
            }
                break;
                
            default:
                break;
        }
    }];
    
    
    //弹出选择楼层
    [self.bannerView setBlockBtnTitlePressed:^(id object,UIButton *btn) {
        @strongify(self);
        if (btn.selected) {
            [self doShowPopView:btn];
        }else{
            [self.listViewPop dismiss];
        }
    }];
    
    [self.bannerView setBlockBtnBackPressed:^(id object, UIButton *btn) {
        @strongify(self);
        [self.navigationController popViewControllerAnimated:YES];
    }];
}

- (void)doShowSheet:(SHModelRoom *)modelRoom
{
    @weakify(self);
    
    LCActionSheet *actionSheet = [LCActionSheet sheetWithTitle:@"请选择你要进行的操作"
                                             cancelButtonTitle:@"取消"
                                                       clicked:^(LCActionSheet * _Nonnull actionSheet, NSInteger buttonIndex) {
                                                           @strongify(self);
                                                           switch (buttonIndex) {
                                                               case 1:
                                                               {
                                                                   NSArray *arrRoomPicList = [[SHAppCommonRequest shareInstance] doGetRoomUIPicAll];
                                                                   CommonAlterView *alter = [[CommonAlterView alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, UI_SCREEN_HEIGHT)
                                                                                                                     dataSourceArr:arrRoomPicList
                                                                                                                              type:CommonAlterViewType_Common];
                                                                   [alter popWithFatherView:self.view];
                                                                   
                                                                   
                                                                   [alter setBlockGetNameAndPic:^(NSString *strName, SHUIPicModel *picModel) {
                                                                       //进行添加房间的操作
                                                                       SHModelRoom *modelRoomNew = [SHModelRoom new];
                                                                       modelRoomNew.iRoom_id = modelRoom.iRoom_id;
                                                                       modelRoomNew.strRoom_name = strName;
                                                                       modelRoomNew.strRoom_image = picModel.strUIPic_name;
                                                                       
                                                                       [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
                                                                       [self.roomManager handleTheUpdateRoomDataWithModel:modelRoomNew
                                                                                                           completeHandle:^(BOOL success, id result)
                                                                        {
                                                                            [XWHUDManager hideInWindow];
                                                                            if (success) {
                                                                                [self.roomManager doGetRoomListFromNetworkWithFloorID:self.iFloorID];
                                                                            }else{
                                                                                [XWHUDManager showErrorTipHUD:@"更新失败"];
                                                                                
                                                                            }
                                                                        }];
                                                                   }];
                                                               }
                                                                   break;
                                                               case 2:
                                                               {
                                                                   @strongify(self);
                                                                   [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
                                                                   [self.roomManager handleTheDeleteRoomDataWithRoomId:modelRoom.iRoom_id completeHandle:^(BOOL success, id result)
                                                                    {
                                                                        [XWHUDManager hideInWindow];
                                                                        if (success) {
                                                                            [self.roomManager doGetRoomListFromNetworkWithFloorID:self.iFloorID];
                                                                        }else{
                                                                            [XWHUDManager showErrorTipHUD:@"删除失败"];
                                                                        }
                                                                    }];
                                                               }
                                                                   break;
                                                               case 0:
                                                               {
                                                                   NSLog(@"点了取消");
                                                               }
                                                                   break;
                                                               default:
                                                                   break;
                                                           }

                                                       }
                                             otherButtonTitles:@"编辑",@"删除", nil];
    [actionSheet show];

}




- (void)doShowPopView:(UIButton *)btn
{
    self.listViewPop = [[DropDownListView alloc] initWithFrame:CGRectMake(0,
                                                                          64,
                                                                          UI_SCREEN_WIDTH,
                                                                          UI_SCREEN_HEIGHT - 64 - 49)
                                                       arrList:self.arrFloor];
    [self.view addSubview:self.listViewPop];
    [self.listViewPop show];
    
    [self.listViewPop setBlockTapOnTheBackgroundView:^{
        btn.selected = NO;
    }];
    
    @weakify(self);
    [self.listViewPop setBlockGetObjectCompleteHandle:^(id object) {
        @strongify(self);
        SHModelFloor *modelFloor = (SHModelFloor *)object;
        [self doGetRoomListWithFloorID:modelFloor.iFloor_id];
        [self.bannerView.btnTitle setTitle:modelFloor.strFloor_name forState:UIControlStateNormal];
        [self.listViewPop dismiss];
    }];
}



#pragma mark -
#pragma mark - 初始化
- (RoomCollectionView *)collectionView
{
    if (!_collectionView) {
        UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
        _collectionView = [[RoomCollectionView alloc] initWithFrame:self.view.bounds collectionViewLayout:layout];
    }
    return _collectionView;
}

- (void)doAddCollectionViewConstraints
{
    @weakify(self);
    [self.collectionView mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.top.equalTo(self.bannerView.mas_bottom);
        make.left.equalTo(self.view.mas_left);
        make.right.equalTo(self.view.mas_right);
        make.bottom.equalTo(self.view.mas_bottom);
    }];
}

-(SmartEquipmentBannerView *)bannerView
{
    if (!_bannerView) {
        NSArray *libs = [[NSBundle mainBundle] loadNibNamed:@"SmartEquipmentBannerView" owner:nil options:nil];
        
        _bannerView = (SmartEquipmentBannerView *)libs[0];
    }
    return _bannerView;
}

- (void)doAddBannerConstraints
{
    @weakify(self);
    [self.bannerView mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.top.equalTo(self.view.mas_top);
        make.left.equalTo(self.view.mas_left);
        make.right.equalTo(self.view.mas_right);
        CGFloat height = 160;
        make.height.equalTo(@(height));
    }];
    
}

- (SHRoomManager *)roomManager
{
    if (!_roomManager) {
        _roomManager = [SHRoomManager new];
    }
    return _roomManager;
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    if ([segue.identifier isEqualToString:@"seg_to_DeviceManagementVC"]) {
        DeviceManagementVC *vc =(DeviceManagementVC *)segue.destinationViewController;
        vc.room = (SHModelRoom *)sender;
    }
}


@end
