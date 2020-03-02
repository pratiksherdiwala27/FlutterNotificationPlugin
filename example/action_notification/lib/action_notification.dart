import 'dart:async';

import 'package:flutter/services.dart';

class ActionNotification {
  static const MethodChannel _channel =
      const MethodChannel('action_notification');
  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
