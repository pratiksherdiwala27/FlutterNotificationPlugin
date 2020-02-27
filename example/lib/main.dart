import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_plugin/flutter_plugin.dart';
import 'package:flutter_plugin_example/second_screen.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> implements Hello {
  String _platformVersion = 'Unknown';

  bool isDetailIntent;

  final GlobalKey<NavigatorState> navigatorKey = GlobalKey<NavigatorState>();

  static const platform = const MethodChannel("flutter_plugin");

  @override
  Future<void> initState() {
    super.initState();
    _initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> _initPlatformState() async {
    String platformVersion;

    try {
      isDetailIntent = await FlutterPlugin.openScreen();
      platform.setMethodCallHandler(_handleMethod);
    } catch (error) {
      print(error.toString());
    }

    if (isDetailIntent) {
      navigate(context);
    }

    if (!mounted) return;
  }

  Future<void> _handleMethod(MethodCall call) async {
    switch (call.method) {
      case "onNotification":
        print("_handleMethod");
        navigatorKey.currentState
            .push(MaterialPageRoute(builder: (context) => SecondScreen()));
    }
  }

  @override
  Widget build(BuildContext context) {
    String dataFromNative;
    return MaterialApp(
      navigatorKey: navigatorKey,
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              FlatButton(
                onPressed: () async {
                  try {
                    dataFromNative = await FlutterPlugin.showNotification(
                      'Pratik',
                      'Hello',
                      'abc@gmail.com',
                    );
                    print(dataFromNative);
                  } catch (error) {
                    print(error.toString());
                  }
                },
                child: Text('Press Here'),
              ),
              FlatButton(
                onPressed: () {
                  navigate(context);
                },
                child: Text('Next Screen'),
              )
            ],
          ),
        ),
      ),
    );
  }

  void navigate(BuildContext context) {
    navigatorKey.currentState
        .push(MaterialPageRoute(builder: (context) => SecondScreen()));
  }

  @override
  void add() {
    // TODO: implement add
  }
}
