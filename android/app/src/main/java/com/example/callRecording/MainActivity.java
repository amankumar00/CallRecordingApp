package com.example.callRecording;

import androidx.annotation.NonNull;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

import android.app.admin.DeviceAdminInfo;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.widget.Toast;


public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "com.flutter.record/record";
    private static final int REQUEST_CODE = 0;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        final int[] counter = {0};
        super.configureFlutterEngine(flutterEngine);
        try {
            // Initiate DevicePolicyManager.
            mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            mAdminName = new ComponentName(this, DeviceAdminInfo.class);

            if (!mDPM.isAdminActive(mAdminName)) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click on Activate button to secure your application.");
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                // mDPM.lockNow();
                // Intent intent = new Intent(MainActivity.this,
                // TrackDeviceService.class);
                // startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                         //Add Your code
//                            result.success("Message From JAVA");
                            if(counter[0] %2==0)
                                { counter[0]++;
                                    Intent intent = new Intent(this, RecordingService.class);
                                startService(intent);
                            Toast.makeText(this, "Call Recording Started", Toast.LENGTH_SHORT).show();}
                            else{
                                counter[0]++;
                                Intent intent = new Intent(this, RecordingService.class);
                                stopService(intent);
                                Toast.makeText(this, "Call Recording Stopped", Toast.LENGTH_SHORT).show();
                            }
                        }

                );
    }

}

