import 'dart:async';

import 'package:flutter/cupertino.dart';

import 'flutter_plugin.dart';

mixin PluginMixin<T extends StatefulWidget> on State<T> implements RouteAware {
  StreamSubscription _stream;

  void _startListening() {
    try {
      _stream = FlutterPlugin.communicatorStream.listen((data) {
        print(data.toString());
        onOpenFromNotification(data);
      }, onError: (error) {
        print('Error: ' + error.toString());
      }, onDone: () {
        print('done');
      });
    } catch (error) {
      print("Stream Error: " + error.toString());
    }
  }

  void _stopListening() {
    _stream.cancel();
  }

  void onOpenFromNotification(dynamic data);

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    FlutterPlugin.routeObserver.subscribe(this, ModalRoute.of(context));
  }

  @override
  void didPush() {
    _startListening();
    print('Second did push');
  }

  @override
  void didPop() {
    _stopListening();
    print('Second  didPop');
  }

  @override
  void didPopNext() {
    _startListening();
    print('Second  didPopNext');
  }

  @override
  void didPushNext() {
    _stopListening();
    print('Second  didPushNext');
  }
}
