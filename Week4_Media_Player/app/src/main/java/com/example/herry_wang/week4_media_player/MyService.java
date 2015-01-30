package com.example.herry_wang.week4_media_player;

/**
 * Created by Herry_Wang on 2015/1/29.
 */
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;


public class MyService extends Service {
    private static final String ACTION_PLAY = "com.example.action.PLAY";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!MainFragment.mediaPlayer.isPlaying()) {
            MainFragment.mediaPlayer.start();
        }
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        MainFragment.mediaPlayer.pause();
    }


}