//
//  SHAirHandleTypeVC.m
//  SmartHouseNew
//
//  Created by yyh on 15/3/16.
//  Copyright (c) 2015年 yuchangtao. All rights reserved.
//

#import "SHAirHandleTypeVC.h"
#import "SHAdaptiveVC.h"

@interface SHAirHandleTypeVC () <UITableViewDataSource, UITableViewDelegate,UINavigationControllerDelegate>

@property (nonatomic, strong) NSMutableArray *dataArray;
@property (weak, nonatomic) IBOutlet UITableView *tableView;

@end

@implementation SHAirHandleTypeVC

#pragma mark - UINavigationControllerDelegate
- (void)navigationController:(UINavigationController *)navigationController
      willShowViewController:(UIViewController *)viewController
                    animated:(BOOL)animated {
    BOOL isHomePage = [viewController isKindOfClass:[self class]];
    
    [self.navigationController setNavigationBarHidden:!isHomePage animated:YES];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.delegate =self;
}

- (void)viewDidLoad {
    self.title = @"选择空调品牌";
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.tableView.tableFooterView = [[UIView alloc] init];
    
    NSMutableArray *arr = [[NSMutableArray alloc] init];
    self.dataArray = [[NSMutableArray alloc] init];
    for (int i = 0; i < 1000; i ++) {
        
        if (i < 10) {
            
            [arr addObject:[NSString stringWithFormat:@"00%d",i]];
        }else if (i >= 10 && i < 100) {
            
            [arr addObject:[NSString stringWithFormat:@"0%d",i]];
        }else{
            
            [arr addObject:[NSString stringWithFormat:@"%d",i]];
        }
        
        if (i == 0) {
        
            [arr removeAllObjects];
        }
        
        if (i == 19) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"海尔", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 39) {
            
            [arr insertObject:[NSString stringWithFormat:@"00%d",0] atIndex:0];
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"格力", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 59) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"美的", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 79) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"长虹", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 99) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"志高", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 109) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"华宝", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 119) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"科龙", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 139) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"TCL", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 149) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"格兰仕", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 169) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"华凌", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 179) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"春兰", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 179) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"奥克斯", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 209) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"新科", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 229) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"澳柯玛", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 239) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"海信", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 292) {
            
            [arr removeAllObjects];
        }
        if (i == 295) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"爱德龙", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 299) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"爱特", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 300) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"奥力", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 302) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"澳科", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 303) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"高士达", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 365) {
            
            [arr removeAllObjects];
        }
        if (i == 367) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"康佳", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 414) {
            
            [arr removeAllObjects];
        }
        if (i == 415) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"双菱", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 449) {
            
            [arr removeAllObjects];
        }
        if (i == 452) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"先科", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 463) {
            
            [arr removeAllObjects];
        }
        if (i == 466) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"熊猫", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 471) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"扬子", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 499) {
            
            [arr removeAllObjects];
        }
        if (i == 550) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"三洋/NEC", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 599) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"三菱", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 609) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"LG", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 629) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"三星", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 639) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"冬至", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 659) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"日立", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 699) {
            
            [arr removeAllObjects];
        }
        if (i == 719) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"富士通（珍宝）", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 739) {
            
            [arr removeAllObjects];
        }
        if (i == 759) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"大金", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }
        if (i == 779) {
            
            [arr removeAllObjects];
        }
        if (i == 789) {
            
            NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSArray arrayWithArray:arr], @"code", @"现代（大宇）", @"name", nil];
            [self.dataArray addObject:[NSDictionary dictionaryWithObject:dic forKey:@"data"]];
            [arr removeAllObjects];
        }

    }
}

#pragma mark -
#pragma mark TableView
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return self.dataArray.count;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *cellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (!cell) {
        
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    
    NSDictionary *dic = self.dataArray[indexPath.row];
    cell.textLabel.text = dic[@"data"][@"name"];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    NSDictionary *dic = self.dataArray[indexPath.row];
    if (self.selectAirHandleType) {
        self.selectAirHandleType(dic);
    }
    
    [self performSegueWithIdentifier:@"seg_to_SHAdaptiveVC" sender:dic];
 //   [self.navigationController popViewControllerAnimated:YES];
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
    if ([segue.identifier isEqualToString:@"seg_to_SHAdaptiveVC"]) {
        SHAdaptiveVC *vc = (SHAdaptiveVC *)segue.destinationViewController;
        vc.dict = (NSDictionary *)sender;
        vc.device = self.device;
    }
}


@end
