//
//  Foundation+Security.m
//

#import "Foundation+Security.h"
#import "JRSwizzle.h"
#import <objc/runtime.h>

@implementation FoundationSecurity

+ (void)swapMethod
{
    Class arrayI = [[NSArray array] class];
    [arrayI jr_swizzleMethod:@selector(objectAtIndex:) withMethod:@selector(myObjectAtIndex:) error:NULL];
    
    Class arrayM = [[NSMutableArray array] class];
    [arrayM jr_swizzleMethod:@selector(objectAtIndex:) withMethod:@selector(myObjectAtIndex:) error:NULL];
    [arrayM jr_swizzleMethod:@selector(exchangeObjectAtIndex:withObjectAtIndex:) withMethod:@selector(myExchangeObjectAtIndex:withObjectAtIndex:) error:NULL];

    Class dictionaryM = [[NSMutableDictionary dictionary] class];
    [dictionaryM jr_swizzleMethod:@selector(setObject:forKey:) withMethod:@selector(mySetObject:forKey:) error:NULL];
}

@end

@implementation NSArray (Security)

- (id)myObjectAtIndex:(NSUInteger)index
{
    if (self == nil || self.count == 0 || index > self.count-1)
        return nil;
    return [self myObjectAtIndex:index];
}

@end

@implementation NSMutableArray (Security)

- (id)myObjectAtIndex:(NSUInteger)index
{
    if (self == nil || self.count == 0 || index > self.count-1)
        return nil;
    return [self myObjectAtIndex:index];
}

- (void)myExchangeObjectAtIndex:(NSUInteger)index1 withObjectAtIndex:(NSUInteger)index2
{
    if (self && index1 < self.count && index2 < self.count) {
        [self myExchangeObjectAtIndex:index1 withObjectAtIndex:index2];
    }
}
@end


@implementation NSMutableDictionary (Security)

- (void)mySetObject:(id)object forKey:(id <NSCopying>)key
{
    if (object != nil && key != nil) {
        [self mySetObject:object forKey:key];
    }
}
@end
