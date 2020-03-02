import 'package:flutter/material.dart';
import 'package:route_observer/test.dart';
import 'package:route_observer/test_widget.dart';

final RouteObserver<PageRoute> routeObserver = RouteObserver<PageRoute>();

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'HERO and RouteObserver'),
      routes: <String, WidgetBuilder>{
        'test': (BuildContext context) => TestPage(routeObserver: routeObserver)
      },
      navigatorObservers: [routeObserver],
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.blue,
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Hero(
        tag: "HeroTag",
        child: TestWidget(routeObserver: routeObserver),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _toTestPage,
        child: Icon(Icons.add),
      ),
    );
  }

  void _toTestPage() {
    Navigator.of(context).pushNamed("test");
  }
}
