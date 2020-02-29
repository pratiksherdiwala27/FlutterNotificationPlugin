import 'package:flutter/material.dart';

class TestWidget extends StatefulWidget {
  const TestWidget({this.routeObserver});

  final RouteObserver<PageRoute> routeObserver;

  @override
  State<StatefulWidget> createState() => TestWidgetState();
}

class TestWidgetState extends State<TestWidget> with RouteAware {
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
    print("did push: test_widget");
  }

  @override
  void didPushNext() {
    print("did push next: test_widget");
  }

  @override
  void didPop() {
    print("did pop: test_widget");
  }

  @override
  void didPopNext() {
    print("did pop next: test_widget");
  }

  @override
  Widget build(BuildContext context) {
    return Container(
        color: Colors.red,
        child: Center(
          child: Text("Hero"),
        ));
  }
}
