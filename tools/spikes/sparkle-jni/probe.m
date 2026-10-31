#import <Foundation/Foundation.h>
#import <AppKit/AppKit.h>
#import <jni.h>
#import <dlfcn.h>
#import <objc/message.h>
JNIEXPORT jstring JNICALL Java_Probe_loadFramework(JNIEnv *env, jclass ignored, jstring path) {
  const char *name = (*env)->GetStringUTFChars(env, path, 0);
  void *handle = dlopen(name, RTLD_NOW | RTLD_LOCAL);
  (*env)->ReleaseStringUTFChars(env, path, name);
  if (!handle) return (*env)->NewStringUTF(env, dlerror());
  Class controllerClass = NSClassFromString(@"SPUStandardUpdaterController");
  if (!controllerClass) return (*env)->NewStringUTF(env, "class missing");
  dispatch_semaphore_t done = dispatch_semaphore_create(0);
  __block const char *result = "main queue timeout";
  dispatch_async(dispatch_get_main_queue(), ^{
    @try {
      id object = [controllerClass alloc];
      SEL init = NSSelectorFromString(@"initWithStartingUpdater:updaterDelegate:userDriverDelegate:");
      id (*initializer)(id, SEL, BOOL, id, id) = (void *)objc_msgSend;
      id controller = initializer(object, init, NO, nil, nil);
      result = controller ? "Sparkle controller initialized on AppKit main thread" : "controller init returned nil";
    } @catch (NSException *exception) { result = "controller init raised exception"; }
    dispatch_semaphore_signal(done);
  });
  dispatch_semaphore_wait(done, dispatch_time(DISPATCH_TIME_NOW, 5 * NSEC_PER_SEC));
  return (*env)->NewStringUTF(env, result);
}
