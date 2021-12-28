# android_battery_optimization

Battery Optimization can only be disabled by the user so this plugin can check whether it's disabled and open the setting page if it's not.

```dart
final isIgnoringBatOp = await AndroidBatteryOptimization.isIgnoringBatteryOptimizations;

if (!isIgnoringBatOp) {
    // you can show a dialog here to ask the user for
    // permission before opening the settings page
    await AndroidBatteryOptimization.openBatteryOptimizationSetting(showToast: true);
}
```

