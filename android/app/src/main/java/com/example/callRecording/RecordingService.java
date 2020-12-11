package com.example.callRecording;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordingService extends Service {

    private MediaRecorder rec;
    private boolean recordStarted;
    private File file;
    String path ="/sdcard/Alarms/";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        file= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
        Date date = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
        String dateString=sdf.format(date);
        rec=new MediaRecorder();
        rec.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        rec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        rec.setOutputFile(file.getAbsolutePath()+"/"+sdf+"rec.3gp");
        rec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
        TelephonyManager manager= (TelephonyManager)getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        manager.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
//                super.onCallStateChanged(state, phoneNumber) {
                    if(TelephonyManager.CALL_STATE_IDLE==state&& rec==null){
                        rec.stop();
                        rec.reset();
                        rec.release();
                        recordStarted=false;
                        stopSelf();
                    }//When Idle we stop,reset and release the call recorder
                        else if (TelephonyManager.CALL_STATE_OFFHOOK==state){
                        try {
                            rec.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        rec.start();
                                recordStarted=true;


                    }//When The Call Connects We start The Call recording Method
            }//In This Method We Decide When to record the call and When to Pause the Recording
        },PhoneStateListener.LISTEN_CALL_STATE);
        return  START_STICKY;
    }

}
