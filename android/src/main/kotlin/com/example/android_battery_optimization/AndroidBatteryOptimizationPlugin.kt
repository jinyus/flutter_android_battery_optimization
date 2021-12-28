package com.example.android_battery_optimization

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** AndroidBatteryOptimizationPlugin */
class AndroidBatteryOptimizationPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var appContext: Context
    private var activity: Activity? = null

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        onAttachedToEngine(flutterPluginBinding.applicationContext, flutterPluginBinding.binaryMessenger)
    }

    private fun onAttachedToEngine(applicationContext: Context, messenger: BinaryMessenger) {
        appContext = applicationContext
        channel = MethodChannel(messenger, "android_battery_optimization")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "getPlatformVersion" -> result.success("Android ${Build.VERSION.RELEASE}")
            "isIgnoringBatteryOptimizations" -> result.success(isIgnoringBatteryOptimizations())
            "openBatteryOptimizationSetting" -> {
                val showToast = call.argument<Boolean>("showToast")
                result.success(openBatteryOptimizationSetting(showToast ?: false))
            }
            else -> result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    private fun hasOptimizationSetting(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    private fun openBatteryOptimizationSetting(showToast: Boolean): Boolean {
        if (activity == null) {
            println("Activity is not set")
            return false
        }
        if (!isIgnoringBatteryOptimizations() && hasOptimizationSetting()) {
            val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
            if (showToast) {
                val appLabel = appContext.applicationInfo.nonLocalizedLabel
                Toast.makeText(appContext, "Click \"Not optimized\" -> All apps -> Find $appLabel -> Don't optimize", Toast.LENGTH_LONG).show()
            }
            activity!!.startActivity(intent)
            return true
        }
        return false
    }

    private fun isIgnoringBatteryOptimizations(): Boolean {
        val pwrm = appContext.applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        val name = appContext.applicationContext.packageName
        if (hasOptimizationSetting()) return pwrm.isIgnoringBatteryOptimizations(name)
        return true
    }

    override fun onDetachedFromActivity() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }
}
