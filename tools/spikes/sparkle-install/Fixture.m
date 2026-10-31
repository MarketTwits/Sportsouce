#import <AppKit/AppKit.h>
#import <Sparkle/Sparkle.h>
static void logEvent(NSString *s) { NSString *p = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"FixtureLog"]; NSFileHandle *f = [NSFileHandle fileHandleForWritingAtPath:p]; if (!f) { [[NSFileManager defaultManager] createFileAtPath:p contents:nil attributes:nil]; f = [NSFileHandle fileHandleForWritingAtPath:p]; } [f seekToEndOfFile]; [f writeData:[[s stringByAppendingString:@"\n"] dataUsingEncoding:NSUTF8StringEncoding]]; [f closeFile]; }
@interface Driver : NSObject <SPUUserDriver> @end
@implementation Driver
- (void)showUpdatePermissionRequest:(SPUUpdatePermissionRequest *)r reply:(void (^)(SUUpdatePermissionResponse *))reply { logEvent(@"permission"); }
- (void)showUserInitiatedUpdateCheckWithCancellation:(void (^)(void))c { logEvent(@"checking"); }
- (void)showUpdateFoundWithAppcastItem:(SUAppcastItem *)i state:(SPUUserUpdateState *)s reply:(void (^)(SPUUserUpdateChoice))r { logEvent(@"found"); r(SPUUserUpdateChoiceInstall); }
- (void)showUpdateReleaseNotesWithDownloadData:(SPUDownloadData *)d {}
- (void)showUpdateReleaseNotesFailedToDownloadWithError:(NSError *)e {}
- (void)showUpdateNotFoundWithError:(NSError *)e acknowledgement:(void (^)(void))a { logEvent([@"notfound: " stringByAppendingString:e.description]); a(); }
- (void)showUpdaterError:(NSError *)e acknowledgement:(void (^)(void))a { logEvent([@"error: " stringByAppendingString:e.description]); a(); }
- (void)showDownloadInitiatedWithCancellation:(void (^)(void))c { logEvent(@"download"); }
- (void)showDownloadDidReceiveExpectedContentLength:(uint64_t)n {}
- (void)showDownloadDidReceiveDataOfLength:(uint64_t)n {}
- (void)showDownloadDidStartExtractingUpdate { logEvent(@"extract"); }
- (void)showExtractionReceivedProgress:(double)p {}
- (void)showReadyToInstallAndRelaunch:(void (^)(SPUUserUpdateChoice))r { logEvent(@"install"); r(SPUUserUpdateChoiceInstall); }
- (void)showInstallingUpdateWithApplicationTerminated:(BOOL)t retryTerminatingApplication:(void (^)(void))r { logEvent(@"installing"); }
- (void)showUpdateInstalledAndRelaunched:(BOOL)b acknowledgement:(void (^)(void))a { logEvent(@"installed"); a(); }
- (void)dismissUpdateInstallation {}
- (void)showUpdateInFocus {}
@end
@interface App : NSObject <NSApplicationDelegate> @property SPUUpdater *updater; @property Driver *driver; @end
@implementation App
- (void)applicationDidFinishLaunching:(NSNotification *)n { NSString *v = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleVersion"]; logEvent([@"started: " stringByAppendingString:v]); if ([v isEqual:@"2"]) { [NSApp terminate:nil]; return; } self.driver = [Driver new]; self.updater = [[SPUUpdater alloc] initWithHostBundle:NSBundle.mainBundle applicationBundle:NSBundle.mainBundle userDriver:self.driver delegate:nil]; NSError *error; if (![self.updater startUpdater:&error]) { logEvent([@"start-error: " stringByAppendingString:error.description]); [NSApp terminate:nil]; return; } [self.updater checkForUpdates]; }
@end
int main(void) { @autoreleasepool { NSApplication *app = NSApplication.sharedApplication; App *delegate = [App new]; app.delegate = delegate; [app run]; } }
