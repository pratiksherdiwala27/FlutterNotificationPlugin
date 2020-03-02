import 'package:flutter/material.dart';
import 'package:handle_notification/handle_notification.dart';
import 'package:handle_notification/notification_mixin.dart';

class FirstScreen extends StatefulWidget {
  @override
  _FirstScreenState createState() => _FirstScreenState();
}

class _FirstScreenState extends State<FirstScreen> with NotificationMixin {
  HandleNotification _plugin = HandleNotification();

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

  void navigate() {
    Navigator.of(context).pushNamed('/second');
  }

  @override
  void onOpenFromNotification(data) {
    navigate();
  }
}
