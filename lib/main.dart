import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      initialRoute: '/',
      routes: {
        '/': (context) => RecordingPage(),
      },
    );
  }
}

String typeOfCall(int caller) {
  if (caller == 1)
    return ('Outgoing Call');
  else
    return ('Incoming Call');
}

class RecordingPage extends StatelessWidget {
  int caller = 1;
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Call Recorder'),
      ),
      body: Container(
        child: Column(
          children: [
            RecordsList(
              time: '10: 10 PM',
              typeOfCall: typeOfCall(caller),
            ),
          ],
        ),
      ),
    );
  }
}

class RecordsList extends StatefulWidget {
  RecordsList({this.time, this.typeOfCall});
  final String typeOfCall;
  final String time;

  @override
  _RecordsListState createState() => _RecordsListState();
}

class _RecordsListState extends State<RecordsList> {
  static const platform = const MethodChannel("com.flutter.record/record");
  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Padding(
          padding: EdgeInsets.only(left: 16),
          child: Image.asset(
            'images/defaultImage.png',
            width: 40,
            height: 40,
          ),
        ),
        Expanded(
          child: Container(
            height: 60,
            width: double.infinity,
            child: FlatButton(
              onPressed: () {
                setState(() {
                  Printy();
                });
              },
              child: Align(
                alignment: Alignment.centerLeft,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text('${widget.typeOfCall}'),
                    Text('${widget.time}'),
                  ],
                ),
              ),
            ),
          ),
        ),
      ],
    );
  }

  void Printy() async {
    String value;
    try {
      value = await platform.invokeMethod("Printy");
    } catch (e) {
      print(e);
    }

    print(value);
  }
}
