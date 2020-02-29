import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_plugin/flutter_plugin.dart';
import 'package:flutter_plugin/plugin_mixin.dart';

/*
final RouteObserver<PageRoute<dynamic>> routeObserver =
    RouteObserver<PageRoute<dynamic>>();
*/

class FirstScreen extends StatefulWidget {
  @override
  _FirstScreenState createState() => _FirstScreenState();
}

class _FirstScreenState extends State<FirstScreen> with PluginMixin {
  FlutterPlugin _plugin = FlutterPlugin();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            FlatButton(
              onPressed: () async {
                try {
                  final result = await _plugin.showNotification(
                    'Pratik',
                    'Hello',
                    'abc@gmail.com',
                  );
                  print(result);
                } catch (error) {
                  print(error.toString());
                }
              },
              child: Text('Press Here'),
            ),
            FlatButton(
              onPressed: () {
                Navigator.of(context).pushNamed('/second');
              },
              child: Text('Next Screen'),
            )
          ],
        ),
      ),
    );
  }

  Future<void> _handleMethod(MethodCall call) async {
    switch (call.method) {
      case "onNotification":
        print("_handleMethod");
        navigate();
    }
  }

  void navigate() {
    Navigator.of(context).pushNamed('/second');
  }

  @override
  void onOpenFromNotification(data) {
    navigate();
  }
}
