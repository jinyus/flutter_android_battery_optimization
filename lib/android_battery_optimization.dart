import 'dart:async';

import 'package:flutter/services.dart';

class AndroidBatteryOptimization {
  static const MethodChannel _channel =
      const MethodChannel('android_battery_optimization');

  static Future<String> get platformVersion async {
    final String version = await (_channel.invokeMethod('getPlatformVersion') as FutureOr<String>);
    return version;
  }

  static Future<bool> get isIgnoringBatteryOptimizations async {
    final bool status =
        await (_channel.invokeMethod('isIgnoringBatteryOptimizations') as FutureOr<bool>);
    return status;
  }

  static Future<void> openBatteryOptimizationSetting(
      {bool showToast = false}) async {
    await _channel.invokeMethod(
      'openBatteryOptimizationSetting',
      {'showToast': showToast},
    );
  }
}
