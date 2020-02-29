import 'package:flutter/material.dart';
import 'package:flutter_plugin_example/first_screen.dart';
import 'package:flutter_plugin_example/second_screen.dart';

final RouteObserver<PageRoute<dynamic>> routeObserver =
    RouteObserver<PageRoute<dynamic>>();

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  bool isDetailIntent;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      navigatorObservers: [routeObserver],
      initialRoute: '/',
      routes: <String, WidgetBuilder>{
        "/": (BuildContext context) => FirstScreen(),
        "/second": (BuildContext context) => SecondScreen()
      },
      //navigatorObservers: [routeObserver],
    );
  }

//  void navigate(BuildContext context) {
//    navigatorKey.currentState
//        .push(MaterialPageRoute(builder: (context) => SecondScreen()));
//  }
}
