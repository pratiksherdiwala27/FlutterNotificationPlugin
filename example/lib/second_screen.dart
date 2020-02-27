import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class SecondScreen extends StatefulWidget {
  @override
  _SecondScreenState createState() => _SecondScreenState();
}

class _SecondScreenState extends State<SecondScreen>
    with WidgetsBindingObserver {
  static const platform = const MethodChannel("flutter_plugin");

  String resultFromNative = "Waiting for result";

  String _data = 'Second Screen';

  Future<void> _handleMethod(MethodCall call) async {
    switch (call.method) {
      case "onNotification":
        setState(() {
          _data = call.arguments['data'];
        });
    }
  }

  @override
  void initState() {
    super.initState();
    platform.setMethodCallHandler(_handleMethod);
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Scaffold(
        appBar: AppBar(),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Center(
              child: Text(
                _data,
                style: TextStyle(
                  fontWeight: FontWeight.normal,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

/*


FlatButton(
              onPressed: () async {
                final data = await FlutterPlugin.showNotification(
                  'New Name',
                  'New Message',
                  'New EMail',
                );
                print('Second Screen ${Future.value(data)}');
              },
              child: Text(
                "Press",
                style: TextStyle(color: Colors.white),
              ),
            ),
 */
