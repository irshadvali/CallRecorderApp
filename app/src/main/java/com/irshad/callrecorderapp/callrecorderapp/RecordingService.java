package com.irshad.callrecorderapp.callrecorderapp;

import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by irshadvali on 14/12/17.
 */

public class RecordingService extends Service {
    private MediaRecorder rec;
    private boolean recoderstarted;
    private File file;
    String path = "sdcard/alarms/";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);

        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        Date date = new Date();
        String stringDate = DateFormat.getDateTimeInstance().format(date);
        rec = new MediaRecorder();
        rec.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        rec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        rec.setOutputFile(file.getAbsoluteFile() + "/" + stringDate + "callrec.3gp");
        rec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        manager.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                if (TelephonyManager.CALL_STATE_IDLE == state ){
                    try {
                        if (rec == null) {
                            rec.stop();
                            rec.reset();
                            rec.release();
                            recoderstarted = false;
                            rec = null;
                            stopSelf();
                            System.out.println("IRSHAD CALL_STATE_IDLE");
                        } else {
                            rec.stop();
                            rec.reset();
                            rec.release();
                            recoderstarted = false;
                            rec = null;
                            stopSelf();
                            System.out.println("IRSHAD CALL_STATE_IDLE");
                        }
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                else if(TelephonyManager.CALL_STATE_OFFHOOK==state){
                    System.out.println("IRSHAD CALL_STATE_OFFHOOK");
                    try {
                        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                        Date date = new Date();
                        String stringDate = DateFormat.getDateTimeInstance().format(date);
                        rec = new MediaRecorder();
                        rec.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
                        rec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        rec.setOutputFile(file.getAbsoluteFile() + "/" + stringDate + "callrec.3gp");
                        rec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
                    rec.prepare();
                    rec.start();
                    recoderstarted=true;
                    } catch (IOException e) {
                        System.out.println("irshad Exception ="+ e.getMessage());
                        e.printStackTrace();
                    }
                }


            }
        }, PhoneStateListener.LISTEN_CALL_STATE);

        return START_STICKY;
    }
}
