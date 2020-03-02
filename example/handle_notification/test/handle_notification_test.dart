import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:handle_notification/handle_notification.dart';

void main() {
  const MethodChannel channel = MethodChannel('handle_notification');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await HandleNotification.platformVersion, '42');
  });
}
