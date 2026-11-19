#import <AppKit/AppKit.h>
#import <Foundation/Foundation.h>
#import <jni.h>
#import <dlfcn.h>
#import <objc/message.h>

static JavaVM *vm;
static jobject driver;
static jmethodID callback;
static id controller;
static id delegate;
static NSString *reasonKey;
static jlong discoveryId;
static BOOL discovered;
static NSInteger noUpdateReason;
static NSString *updateVersion;
static NSString *updateBuild;
static NSString *updateDescription;

static jstring toJava(JNIEnv *env, NSString *value) {
    if (!value) return NULL;
    NSUInteger length = MIN(value.length, 32768);
    unichar *characters = calloc(MAX(length, 1), sizeof(unichar));
    if (!characters) return NULL;
    [value getCharacters:characters range:NSMakeRange(0, length)];
    jstring result = (*env)->NewString(env, characters, (jsize)length);
    free(characters);
    return result;
}

static void reply(jlong id, jint code, NSString *version, NSString *build, NSString *description) {
    JNIEnv *env = NULL;
    BOOL attached = ((*vm)->GetEnv(vm, (void **)&env, JNI_VERSION_1_6) != JNI_OK);
    if (attached && (*vm)->AttachCurrentThread(vm, (void **)&env, NULL) != JNI_OK) return;
    jstring v = toJava(env, version), b = toJava(env, build), d = toJava(env, description);
    (*env)->CallVoidMethod(env, driver, callback, id, code, v, b, d);
    if ((*env)->ExceptionCheck(env)) (*env)->ExceptionClear(env);
    if (v) (*env)->DeleteLocalRef(env, v);
    if (b) (*env)->DeleteLocalRef(env, b);
    if (d) (*env)->DeleteLocalRef(env, d);
    if (attached) (*vm)->DetachCurrentThread(vm);
}

@interface SportsauceSparkleDelegate : NSObject
@end
@implementation SportsauceSparkleDelegate
- (void)updater:(id)updater didFindValidUpdate:(id)item {
    if (discoveryId == 0) return;
    id (*get)(id, SEL) = (void *)objc_msgSend;
    updateVersion = [get(item, NSSelectorFromString(@"displayVersionString")) copy];
    updateBuild = [get(item, NSSelectorFromString(@"versionString")) copy];
    updateDescription = [get(item, NSSelectorFromString(@"itemDescription")) copy];
    discovered = YES;
}
- (void)updaterDidNotFindUpdate:(id)updater error:(NSError *)error {
    NSNumber *reason = reasonKey ? error.userInfo[reasonKey] : nil;
    noUpdateReason = [reason isKindOfClass:[NSNumber class]] ? reason.integerValue : -1;
}
- (void)updater:(id)updater didFinishUpdateCycleForUpdateCheck:(NSInteger)check error:(NSError *)error {
    if (check != 2 || discoveryId == 0) return;
    jlong id = discoveryId;
    discoveryId = 0;
    if (discovered && error == nil) reply(id, 1, updateVersion, updateBuild, updateDescription);
    else if (error && [error.domain isEqualToString:@"SUSparkleErrorDomain"] && error.code == 1001) {
        if (noUpdateReason == 1 || noUpdateReason == 2) reply(id, 0, nil, nil, nil);
        else if (noUpdateReason >= 3 && noUpdateReason <= 5) reply(id, 2, nil, nil, nil);
        else reply(id, 3, nil, nil, nil);
    } else reply(id, 3, nil, nil, nil);
    updateVersion = nil; updateBuild = nil; updateDescription = nil;
}
@end

static id ensureUpdater(void) {
    if (controller) return ((id (*)(id, SEL))objc_msgSend)(controller, NSSelectorFromString(@"updater"));
    NSBundle *bundle = [NSBundle mainBundle];
    NSDictionary *info = bundle.infoDictionary;
    NSURL *url = [NSURL URLWithString:info[@"SUFeedURL"] ?: @""];
    NSData *key = [[NSData alloc] initWithBase64EncodedString:info[@"SUPublicEDKey"] ?: @"" options:0];
    BOOL validFeed = [url.scheme.lowercaseString isEqualToString:@"https"] && [url.host.lowercaseString isEqualToString:@"github.com"] &&
        [url.path isEqualToString:@"/MarketTwits/Sportsouce/releases/latest/download/appcast.xml"] && (!url.port || url.port.integerValue == 443);
#if defined(SPORTSAUCE_SPARKLE_TESTING)
    // Only the isolated, separately compiled integration-test binary permits local HTTP.
    validFeed = validFeed || ([url.scheme.lowercaseString isEqualToString:@"http"] &&
        [url.host isEqualToString:@"127.0.0.1"] && [url.path isEqualToString:@"/appcast.xml"] && url.port.integerValue > 0);
#endif
    if (![info[@"SURequireSignedFeed"] boolValue] || ![info[@"SUVerifyUpdateBeforeExtraction"] boolValue] ||
        !validFeed || url.user || url.password || url.query || url.fragment || key.length != 32) return nil;
    NSString *path = [[bundle privateFrameworksPath] stringByAppendingPathComponent:@"Sparkle.framework/Versions/B/Sparkle"];
    void *handle = dlopen(path.fileSystemRepresentation, RTLD_NOW | RTLD_LOCAL);
    if (!handle) return nil;
    NSString * __unsafe_unretained *keySymbol = (NSString * __unsafe_unretained *)dlsym(handle, "SPUNoUpdateFoundReasonKey");
    if (!keySymbol || !*keySymbol) return nil;
    reasonKey = *keySymbol;
    Class type = NSClassFromString(@"SPUStandardUpdaterController");
    if (!type) return nil;
    delegate = [[SportsauceSparkleDelegate alloc] init];
    id (*initializer)(id, SEL, BOOL, id, id) = (void *)objc_msgSend;
    id candidate = initializer([type alloc], NSSelectorFromString(@"initWithStartingUpdater:updaterDelegate:userDriverDelegate:"), NO, delegate, nil);
    if (!candidate) return nil;
    id updater = ((id (*)(id, SEL))objc_msgSend)(candidate, NSSelectorFromString(@"updater"));
    // The app coordinator owns scheduling; avoid Sparkle scheduling extra checks.
    ((void (*)(id, SEL, BOOL))objc_msgSend)(updater, NSSelectorFromString(@"setAutomaticallyChecksForUpdates:"), NO);
    NSError *error = nil;
    BOOL started = ((BOOL (*)(id, SEL, NSError **))objc_msgSend)(updater, NSSelectorFromString(@"startUpdater:"), &error);
    if (!started) return nil;
    // Remove an old user-default override before the next main-loop update cycle.
    ((void (*)(id, SEL))objc_msgSend)(updater, NSSelectorFromString(@"clearFeedURLFromUserDefaults"));
    controller = candidate;
    return updater;
}

JNIEXPORT void JNICALL Java_com_markettwits_selfupdater_thirdparty_api_SparkleJniDriver_nativeBind(JNIEnv *env, jobject object) {
    if (driver) { (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/IllegalStateException"), "Sparkle driver already bound"); return; }
    (*env)->GetJavaVM(env, &vm);
    jclass type = (*env)->GetObjectClass(env, object);
    callback = (*env)->GetMethodID(env, type, "onNativeReply", "(JILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
    (*env)->DeleteLocalRef(env, type);
    if (!callback) return;
    driver = (*env)->NewGlobalRef(env, object);
}

JNIEXPORT void JNICALL Java_com_markettwits_selfupdater_thirdparty_api_SparkleJniDriver_nativeRequest(JNIEnv *env, jobject object, jlong requestId, jint command) {
    if (!driver || requestId <= 0) { (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/IllegalStateException"), "Sparkle driver is not bound"); return; }
    dispatch_async(dispatch_get_main_queue(), ^{
        @autoreleasepool {
            @try {
                id updater = ensureUpdater();
                if (!updater) { reply(requestId, 3, nil, nil, nil); return; }
                BOOL (*getBool)(id, SEL) = (void *)objc_msgSend;
                if (command == 3) {
                    reply(requestId, getBool(updater, NSSelectorFromString(@"sessionInProgress")) ? 4 : 5, nil, nil, nil);
                } else if (command == 2) {
                    if (!getBool(updater, NSSelectorFromString(@"canCheckForUpdates")) || discoveryId != 0) { reply(requestId, 5, nil, nil, nil); return; }
                    ((void (*)(id, SEL))objc_msgSend)(updater, NSSelectorFromString(@"checkForUpdates"));
                    reply(requestId, 4, nil, nil, nil);
                } else if (command == 1) {
                    if (discoveryId != 0 || getBool(updater, NSSelectorFromString(@"sessionInProgress"))) { reply(requestId, 3, nil, nil, nil); return; }
                    discoveryId = requestId; discovered = NO; noUpdateReason = -1;
                    ((void (*)(id, SEL))objc_msgSend)(updater, NSSelectorFromString(@"checkForUpdateInformation"));
                } else reply(requestId, 3, nil, nil, nil);
            } @catch (NSException *exception) {
                if (discoveryId == requestId) discoveryId = 0;
                reply(requestId, 3, nil, nil, nil);
            }
        }
    });
}
