//
//  UIImageView+EzvizOpenSDK.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/11/18.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import "UIImageView+EzvizOpenSDK.h"
#import <objc/runtime.h>
#import "AFHTTPSessionManager.h"

@interface EZDataCache : NSCache <EZDataCache>
@end

@interface UIImageView ()
@property (readwrite, nonatomic, strong, setter = ez_setImageRequestOperation:) AFHTTPSessionManager *ez_imageRequestOperation;
@end

@implementation UIImageView (EzvizOpenSDK)

+ (id <EZDataCache>)sharedDataCache {
    static EZDataCache *_ez_defaultDataCache = nil;
    static dispatch_once_t oncePredicate;
    dispatch_once(&oncePredicate, ^{
        _ez_defaultDataCache = [[EZDataCache alloc] init];
        
        [[NSNotificationCenter defaultCenter] addObserverForName:UIApplicationDidReceiveMemoryWarningNotification object:nil queue:[NSOperationQueue mainQueue] usingBlock:^(NSNotification * __unused notification) {
            [_ez_defaultDataCache removeAllObjects];
        }];
    });
    
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wgnu"
    return objc_getAssociatedObject(self, @selector(sharedDataCache)) ?: _ez_defaultDataCache;
#pragma clang diagnostic pop
}

+ (void)setSharedDataCache:(__nullable id <EZDataCache>)dataCache {
    objc_setAssociatedObject(self, @selector(sharedDataCache), dataCache, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

#pragma mark -

+ (NSOperationQueue *)ez_sharedImageRequestOperationQueue {
    static NSOperationQueue *_ez_sharedImageRequestOperationQueue = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _ez_sharedImageRequestOperationQueue = [[NSOperationQueue alloc] init];
        _ez_sharedImageRequestOperationQueue.maxConcurrentOperationCount = NSOperationQueueDefaultMaxConcurrentOperationCount;
    });
    
    return _ez_sharedImageRequestOperationQueue;
}

- (AFHTTPSessionManager *)ez_imageRequestOperation {
    return (AFHTTPSessionManager *)objc_getAssociatedObject(self, @selector(ez_imageRequestOperation));
}

- (void)ez_setImageRequestOperation:(AFHTTPSessionManager *)imageRequestOperation {
    objc_setAssociatedObject(self, @selector(ez_imageRequestOperation), imageRequestOperation, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (void)ez_setImageWithURLRequest:(NSURLRequest *)urlRequest
                        urlString:(NSString *)URLString
                 placeholderImage:(UIImage *)placeholderImage
                          success:(void (^)(NSURLRequest *request, NSURLResponse * __nullable response, NSData *data))success
                          failure:(void (^)(NSURLRequest *request, NSURLResponse * __nullable response, NSError *error))failure
{
    [self cancelImageRequestOperation];
    
    NSData *cachedData = [[[self class] sharedDataCache] cachedDataForRequest:urlRequest];
    if (cachedData) {
        if (success) {
            success(urlRequest, nil, cachedData);
        } else {
            self.image = [[UIImage alloc] initWithData:cachedData];
        }
        
        self.ez_imageRequestOperation = nil;
    } else {
        if (placeholderImage) {
            self.image = placeholderImage;
        }
        
        
        __weak __typeof(self)weakSelf = self;
        self.ez_imageRequestOperation = [AFHTTPSessionManager manager];
        [self.ez_imageRequestOperation GET:URLString parameters:nil progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
            
            __strong __typeof(weakSelf)strongSelf = weakSelf;
            if ([[urlRequest URL] isEqual:[task.currentRequest URL]]) {
                if (success) {
                    success(urlRequest, task.response, responseObject);
                } else if (responseObject) {
                    strongSelf.image = responseObject;
                }
                
                strongSelf.ez_imageRequestOperation = nil;
            }
            
            [[[strongSelf class] sharedDataCache] cacheData:responseObject forRequest:urlRequest];
            
        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
            
            __strong __typeof(weakSelf)strongSelf = weakSelf;
            if ([[urlRequest URL] isEqual:[task.currentRequest URL]]) {
                if (failure) {
                    failure(urlRequest, task.response, error);
                }
                
                strongSelf.ez_imageRequestOperation = nil;
            }
            
        }];
        
    }
    //    //处理测试环境下证书不合法的情况，正式线上可以注释该代码。
    //    [self.ez_imageRequestOperation setWillSendRequestForAuthenticationChallengeBlock:^(NSURLConnection * _Nonnull connection, NSURLAuthenticationChallenge * _Nonnull challenge) {
    //        if ([challenge.protectionSpace.authenticationMethod isEqualToString:NSURLAuthenticationMethodServerTrust])
    //        {
    //            // 证书不认证  全部信任
    //            [[challenge sender]  useCredential:[NSURLCredential credentialForTrust:challenge.protectionSpace.serverTrust] forAuthenticationChallenge:challenge];
    //            [[challenge sender]  continueWithoutCredentialForAuthenticationChallenge: challenge];
    //        }
    //    }];
    //    [[[self class] ez_sharedImageRequestOperationQueue] addOperation:self.ez_imageRequestOperation];
}

- (void)cancelImageRequestOperation {
    //    [self.ez_imageRequestOperation cancel];
    self.ez_imageRequestOperation = nil;
}

@end

#pragma mark -

static inline NSString * EZDataCacheKeyFromURLRequest(NSURLRequest *request) {
    return [[request URL] absoluteString];
}

@implementation EZDataCache

- (NSData *)cachedDataForRequest:(NSURLRequest *)request {
    switch ([request cachePolicy]) {
        case NSURLRequestReloadIgnoringCacheData:
        case NSURLRequestReloadIgnoringLocalAndRemoteCacheData:
            return nil;
        default:
            break;
    }
    
    return [self objectForKey:EZDataCacheKeyFromURLRequest(request)];
}

- (void)cacheData:(NSData *)data forRequest:(NSURLRequest *)request
{
    if (data && request) {
        [self setObject:data forKey:EZDataCacheKeyFromURLRequest(request)];
    }
}

@end


