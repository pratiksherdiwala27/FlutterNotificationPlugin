#import "HandleNotificationPlugin.h"
#if __has_include(<handle_notification/handle_notification-Swift.h>)
#import <handle_notification/handle_notification-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "handle_notification-Swift.h"
#endif

@implementation HandleNotificationPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftHandleNotificationPlugin registerWithRegistrar:registrar];
}
@end
