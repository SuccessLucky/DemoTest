//
//  SecurityTableCell.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/20.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "SecurityTableCell.h"
#import "SecurityDeviceCVCell.h"

@interface SecurityTableCell()<UICollectionViewDataSource,UICollectionViewDelegate>

@property(nonatomic,strong)UICollectionView *collevtionview;

@end

@implementation SecurityTableCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.backgroundColor = [UIColor clearColor];
    [self LoadObjectUI];
}

-(void)LoadObjectUI{
    
    UICollectionViewFlowLayout *layout =[[UICollectionViewFlowLayout alloc]init];
    //    //    设置collectionView滚动方向
    [layout setScrollDirection:UICollectionViewScrollDirectionHorizontal];
//    layout.headerReferenceSize = CGSizeMake(SCREEN_WIDTH/2, 60);
    layout.itemSize = CGSizeMake(115, 70);
    layout.sectionInset = UIEdgeInsetsMake(20, 20, 20, 20);
    layout.minimumLineSpacing = 15;
    
    
    _collevtionview=[[UICollectionView alloc]initWithFrame:CGRectMake(0, 25, SCREEN_WIDTH, 70) collectionViewLayout:layout];
    _collevtionview.backgroundColor=[UIColor clearColor];
    _collevtionview.showsVerticalScrollIndicator=NO;
    _collevtionview.showsHorizontalScrollIndicator=NO;
    _collevtionview.dataSource = self;
    _collevtionview.delegate = self;
    
    [_collevtionview registerNib:[UINib nibWithNibName:@"SecurityDeviceCVCell" bundle:[NSBundle mainBundle]] forCellWithReuseIdentifier:@"SecurityDeviceCVCell"];
    
    [_collevtionview registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header"];
    [_collevtionview registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:@"footer"];
    
    _collevtionview.showsVerticalScrollIndicator = NO;
    [self.contentView addSubview:_collevtionview];
    [_collevtionview mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.bottom.left.right.mas_equalTo(0);
    }];
}

- (void)setArrData:(NSArray *)arrData
{
    _arrData = arrData;
    [self.collevtionview reloadData];
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.arrData.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identify = @"SecurityDeviceCVCell";
    SecurityDeviceCVCell *cell = (SecurityDeviceCVCell *)[collectionView dequeueReusableCellWithReuseIdentifier:identify
                                                                                   forIndexPath:indexPath];
    SHModelDevice *device = self.arrData[indexPath.row];
    cell.strDeviceName = device.strDevice_device_name;
    cell.strDeviceRegion = [NSString stringWithFormat:@"%@%@",device.strDevice_floor_name,device.strDevice_room_name];
    if (device.iDevice_device_state1 == 1) {
        cell.isShowRedDot = YES;
        cell.backgroundColor = [UIColor orangeColor];
    }else{
        cell.isShowRedDot = NO;
        cell.backgroundColor=[UIColor lightTextColor];
    }
    return cell;
}

- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath
{
    UICollectionReusableView *reusableView = nil;
    
    if (kind == UICollectionElementKindSectionHeader) {
        UICollectionReusableView *footer = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header" forIndexPath:indexPath];
        reusableView = footer;
    }else {
        UICollectionReusableView *footer = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:@"footer" forIndexPath:indexPath];
        reusableView = footer;
    }
    
    
    return reusableView;
}






- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
