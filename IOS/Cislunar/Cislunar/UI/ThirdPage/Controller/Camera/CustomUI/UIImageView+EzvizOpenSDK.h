//
//  UIImageView+EzvizOpenSDK.h
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/11/18.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIImageView (EzvizOpenSDK)

- (void)ez_setImageWithURLRequest:(NSURLRequest *)urlRequest
                 placeholderImage:(nullable UIImage *)placeholderImage
                          success:(nullable void (^)(NSURLRequest *request, NSHTTPURLResponse * __nullable response, NSData *))success
                          failure:(nullable void (^)(NSURLRequest *request, NSHTTPURLResponse * __nullable response, NSError *error))failure;



@end


/**
 The `AFImageCache` protocol is adopted by an object used to cache images loaded by the AFNetworking category on `UIImageView`.
 */
@protocol EZDataCache <NSObject>

/**
 Returns a cached image for the specified request, if available.
 
 @param request The image request.
 
 @return The cached image.
 */
- (nullable NSData *)cachedDataForRequest:(NSURLRequest *)request;

/**
 Caches a particular image for the specified request.
 
 @param image The image to cache.
 @param request The request to be used as a cache key.
 */
- (void)cacheData:(NSData *)data
       forRequest:(NSURLRequest *)request;
@end

NS_ASSUME_NONNULL_END