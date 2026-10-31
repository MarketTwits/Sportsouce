#import <Foundation/Foundation.h>
#import <AppKit/AppKit.h>
#import <jni.h>
#import <dlfcn.h>
#import <objc/message.h>

static JavaVM *probeVM;
static jclass probeClass;
static id probeController;
static id probeDelegate;
static BOOL probeFoundUpdate;
static NSInteger probeNoUpdateReason = -1;
static NSString *probeNoUpdateReasonKey;

@interface UpdateProbeDelegate : NSObject
@end
@implementation UpdateProbeDelegate
- (void)updater:(id)updater didFindValidUpdate:(id)item {
  probeFoundUpdate = YES;
}
- (void)updaterDidNotFindUpdate:(id)updater error:(NSError *)error {
  NSNumber *reason = error.userInfo[probeNoUpdateReasonKey];
  if ([reason isKindOfClass:[NSNumber class]]) probeNoUpdateReason = reason.integerValue;
}
- (void)updater:(id)updater didFinishUpdateCycleForUpdateCheck:(NSInteger)check error:(NSError *)error {
  JNIEnv *env = NULL;
  BOOL attached = ((*probeVM)->GetEnv(probeVM, (void **)&env, JNI_VERSION_1_6) != JNI_OK);
  if (attached && (*probeVM)->AttachCurrentThread(probeVM, (void **)&env, NULL) != JNI_OK) return;
  jmethodID method = (*env)->GetStaticMethodID(env, probeClass, "onResult", "(Ljava/lang/String;)V");
  NSString *message = @"Unexpected update result";
  if (check == 2) {
    if (probeFoundUpdate && (error == nil || error.code == 1001)) message = @"Sparkle informational check found an update";
    else if (error != nil && error.code == 1001) {
      if (probeNoUpdateReason == 1 || probeNoUpdateReason == 2) message = @"Sparkle informational check found no update";
      else if (probeNoUpdateReason >= 3 && probeNoUpdateReason <= 5) message = @"Sparkle informational check found an incompatible update";
      else message = @"Sparkle informational check could not determine update status";
    }
    else if (error != nil) message = @"Sparkle informational check reported an error to JVM";
  }
  jstring value = (*env)->NewStringUTF(env, message.UTF8String);
  (*env)->CallStaticVoidMethod(env, probeClass, method, value);
  (*env)->DeleteLocalRef(env, value);
  if (attached) (*probeVM)->DetachCurrentThread(probeVM);
}
@end

JNIEXPORT jstring JNICALL Java_PackagedProbe_start(JNIEnv *env, jclass cls) {
  (*env)->GetJavaVM(env, &probeVM);
  probeClass = (*env)->NewGlobalRef(env, cls);
  dispatch_semaphore_t done = dispatch_semaphore_create(0);
  __block NSString *result = @"main queue timeout";
  dispatch_async(dispatch_get_main_queue(), ^{
    @try {
      NSString *framework = [[NSBundle mainBundle] privateFrameworksPath];
      NSString *path = [framework stringByAppendingPathComponent:@"Sparkle.framework/Versions/B/Sparkle"];
      void *handle = dlopen(path.fileSystemRepresentation, RTLD_NOW | RTLD_LOCAL);
      if (!handle) result = [NSString stringWithFormat:@"dlopen failed: %s", dlerror()];
      else {
        NSString **reasonKey = (NSString **)dlsym(handle, "SPUNoUpdateFoundReasonKey");
        if (reasonKey == NULL || *reasonKey == nil) @throw [NSException exceptionWithName:@"MissingSparkleSymbol" reason:@"Missing no-update reason key" userInfo:nil];
        probeNoUpdateReasonKey = *reasonKey;
        Class c = NSClassFromString(@"SPUStandardUpdaterController");
        SEL init = NSSelectorFromString(@"initWithStartingUpdater:updaterDelegate:userDriverDelegate:");
        id (*initializer)(id, SEL, BOOL, id, id) = (void *)objc_msgSend;
        probeDelegate = [[UpdateProbeDelegate alloc] init];
        probeController = initializer([c alloc], init, NO, probeDelegate, nil);
        id (*getUpdater)(id, SEL) = (void *)objc_msgSend;
        id updater = getUpdater(probeController, NSSelectorFromString(@"updater"));
        BOOL (*startUpdater)(id, SEL, NSError **) = (void *)objc_msgSend;
        NSError *error = nil;
        BOOL started = startUpdater(updater, NSSelectorFromString(@"startUpdater:"), &error);
        result = started ? @"Sparkle updater started in packaged JVM" : [NSString stringWithFormat:@"Sparkle start failed: %@", error];
        if (started) ((void (*)(id, SEL))objc_msgSend)(updater, NSSelectorFromString(@"checkForUpdateInformation"));
      }
    } @catch (NSException *exception) { result = [NSString stringWithFormat:@"exception: %@", exception]; }
    dispatch_semaphore_signal(done);
  });
  dispatch_semaphore_wait(done, dispatch_time(DISPATCH_TIME_NOW, 10 * NSEC_PER_SEC));
  return (*env)->NewStringUTF(env, result.UTF8String);
}
