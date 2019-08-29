//
//  Foundation+Security.h
//

#import <Foundation/Foundation.h>

@interface FoundationSecurity : NSObject

+ (void)swapMethod;

@end

@interface NSArray (Security)

- (id)myObjectAtIndex:(NSUInteger)index;

@end

@interface NSMutableArray (Security)

- (id)myObjectAtIndex:(NSUInteger)index;

- (void)myExchangeObjectAtIndex:(NSUInteger)index1 withObjectAtIndex:(NSUInteger)index2;
@end

@interface NSMutableDictionary (Security)

- (void)mySetObject:(id)object forKey:(id <NSCopying>)key;

@end