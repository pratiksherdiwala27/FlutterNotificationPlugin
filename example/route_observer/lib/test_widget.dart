import 'package:flutter/material.dart';
import 'package:route_observer/test.dart';

class TestPage extends StatefulWidget {
  const TestPage({this.routeObserver});

  final RouteObserver<PageRoute> routeObserver;

  @override
  State<StatefulWidget> createState() => TestPageState();
}

class TestPageState extends State<TestPage> with RouteAware {
  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    widget.routeObserver.subscribe(this, ModalRoute.of(context));
  }

  @override
  void didPush() {
    print("did push: test_page");
  }

  @override
  void didPushNext() {
    print("did push next: test_page");
  }

  @override
  void didPop() {
    print("did pop: test_page");
  }

  @override
  void didPopNext() {
    print("did pop next: test_page");
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.blue,
      appBar: AppBar(
        title: Text("Test Page"),
      ),
      body: Center(
        child: Container(
          width: 200,
          height: 200,
          child: Hero(
            tag: "HeroTag",
            child: TestWidget(routeObserver: widget.routeObserver),
          ),
        ),
      ),
    );
  }
}
