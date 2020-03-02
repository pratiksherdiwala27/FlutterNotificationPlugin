import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class HandleNotification {
  final RouteObserver<PageRoute<dynamic>> routeObserver =
      RouteObserver<PageRoute<dynamic>>();

  MethodChannel _channel;

  EventChannel _eventChannel;

  StreamController _communicator = StreamController();

  Stream<dynamic> _stream;

  static HandleNotification _instance;

  factory HandleNotification() {
    if (_instance == null) {
      final MethodChannel methodChannel = const MethodChannel('flutter_plugin');
      final EventChannel eventChannel = const EventChannel('event_plugin');
      _instance = HandleNotification.private(methodChannel, eventChannel);
    }
    return _instance;
  }

  @visibleForTesting
  HandleNotification.private(this._channel, this._eventChannel) {
    _eventChannel.receiveBroadcastStream().listen((data) {
      _communicator.sink.add(data);
    }, onDone: () {
      _communicator.close();
    });

    _stream = _communicator.stream.asBroadcastStream();
  }

  Future<String> showNotification(
      String name, String message, String email) async {
    Map<String, String> details = {
      'name': name,
      'message': message,
      'email': email
    };
    try {
      return await _channel.invokeMethod('notification', details);
    } catch (error) {
      print(error.toString());
      throw error;
    }
  }

  get communicatorStream {
    return _stream;
  }
}
