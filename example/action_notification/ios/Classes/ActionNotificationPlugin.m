#import "ActionNotificationPlugin.h"
#if __has_include(<action_notification/action_notification-Swift.h>)
#import <action_notification/action_notification-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "action_notification-Swift.h"
#endif

@implementation ActionNotificationPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftActionNotificationPlugin registerWithRegistrar:registrar];
}
@end
