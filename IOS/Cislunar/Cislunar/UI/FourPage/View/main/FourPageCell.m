//
//  FourPageCell.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/22.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "FourPageCell.h"
@interface FourPageCell()
@property (weak, nonatomic) IBOutlet UILabel *labelTitle;
@property (weak, nonatomic) IBOutlet UIImageView *imageIcon;

@end

@implementation FourPageCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setStrTitle:(NSString *)strTitle
{
    self.labelTitle.text = strTitle;
}

-(void)setStrIcon:(NSString *)strIcon
{
    self.imageIcon.image = [UIImage imageNamed:strIcon];
}

@end
