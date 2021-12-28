import 'dart:async';

import 'package:flutter/services.dart';

class AndroidBatteryOptimization {
  static const MethodChannel _channel =
      const MethodChannel('android_battery_optimization');

  static Future<String> get platformVersion async {
    final String version =
        await (_channel.invokeMethod('getPlatformVersion') as FutureOr<String>);
    return version;
  }

  // Return whether the given application package name is on the device's power allowlist.
  // Being on the power allowlist means that the system will not apply most power saving features to the app.
  // You should check run this method before opening settings.
  // ref:  https://developer.android.com/reference/android/os/PowerManager#isIgnoringBatteryOptimizations(java.lang.String)
  static Future<bool> get isIgnoringBatteryOptimizations async {
    final bool status = await (_channel
        .invokeMethod('isIgnoringBatteryOptimizations') as FutureOr<bool>);
    return status;
  }

  // Open Battery Optimization Settings Page
  // showToast determines whether instructions will be given to the user
  // (in the form of a toast message) on how to disable optimization
  static Future<void> openBatteryOptimizationSetting(
      {bool showToast = false}) async {
    await _channel.invokeMethod(
      'openBatteryOptimizationSetting',
      {'showToast': showToast},
    );
  }
}
