package com.example.herry_wang.week4_media_player;

import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by Herry_Wang on 2015/1/28.
 */
public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    private ImageButton playButton,pauseButton,stopButton,forwardButton,backwardButton;
    private TextView playingTimeText, fullTimeText;
    static MediaPlayer mediaPlayer;
    private SeekBar seekbar;
    private boolean oneTimeOnly = false;
    private double fullTime = 0;
    private double playingTime = 0;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private Handler myHandler = new Handler();
    private View newsLayout;
    private boolean isPlaying;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
                             newsLayout = inflater.inflate(R.layout.fragment, container,
                             false);

        return newsLayout;
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();

        stop();
        myHandler.removeCallbacks(UpdateSongTime);
        mediaPlayer.release();
        mediaPlayer = null;

    }


    @Override
    public void onSaveInstanceState(Bundle out){
        super.onSaveInstanceState(out);
        out.putDouble("playingTime", mediaPlayer.getCurrentPosition());
        out.putDouble("fullTime", mediaPlayer.getDuration());
        out.putBoolean("isPlaying", isPlaying);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        playButton = (ImageButton) newsLayout.findViewById(R.id.play);
        pauseButton = (ImageButton) newsLayout.findViewById(R.id.pause);
        stopButton = (ImageButton) newsLayout.findViewById(R.id.stop);
        forwardButton = (ImageButton) newsLayout.findViewById(R.id.forward);
        backwardButton = (ImageButton) newsLayout.findViewById(R.id.backward);
        seekbar = (SeekBar)newsLayout.findViewById(R.id.seek_bar);
        playingTimeText =(TextView)newsLayout.findViewById(R.id.playing_time);
        fullTimeText =(TextView)newsLayout.findViewById(R.id.full_time);

        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.song);

        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);

        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                play();
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pause();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stop();
            }
        });
        backwardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                backward();
            }
        });
        forwardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                forward();
            }
        });

        if(savedInstanceState != null){
            restore(savedInstanceState);
        }

    }
    public void restore(Bundle out){
        if(out.containsKey("fullTime")) {
            fullTime = out.getDouble("fullTime", 0);
            seekbar.setMax((int) fullTime);
            fullTimeText.setText(TimeUnit.MILLISECONDS.toMinutes((long) fullTime)+":"+
                                (TimeUnit.MILLISECONDS.toSeconds((long) fullTime) -
                                 TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                 toMinutes((long) fullTime))));
        }
        if(out.containsKey("playingTime")) {
            playingTime =out.getDouble("playingTime", 0);
            mediaPlayer.seekTo((int) playingTime);
            playingTimeText.setText(TimeUnit.MILLISECONDS.toMinutes((long) playingTime)+":"+
                                    (TimeUnit.MILLISECONDS.toSeconds((long) playingTime) -
                                     TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                     toMinutes((long) playingTime))));

            seekbar.setProgress((int)playingTime);
        }
        if(out.containsKey("isPlaying")){
            isPlaying = out.getBoolean("isPlaying");
            if(isPlaying){
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);
                playButton.setEnabled(false);
                play();
            }
            else{
                pauseButton.setEnabled(false);
                playButton.setEnabled(true);
            }
        }



    }
    public void play(){
        Log.d("TAG","play");
        Intent intent = new Intent(getActivity(), MyService.class);
        getActivity().startService(intent);

        fullTime = mediaPlayer.getDuration();
        playingTime = mediaPlayer.getCurrentPosition();
        if(!oneTimeOnly){
            seekbar.setMax((int) fullTime);
            oneTimeOnly = true;
        }
        playingTimeText.setText(TimeUnit.MILLISECONDS.toMinutes((long) playingTime)+":"+
                                (TimeUnit.MILLISECONDS.toSeconds((long) playingTime) -
                                 TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                 toMinutes((long) playingTime))));

        fullTimeText.setText(TimeUnit.MILLISECONDS.toMinutes((long) fullTime)+":"+
                            (TimeUnit.MILLISECONDS.toSeconds((long) fullTime) -
                             TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                             toMinutes((long) fullTime))));

        seekbar.setProgress((int)playingTime);
        myHandler.postDelayed(UpdateSongTime,100);
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
        playButton.setEnabled(false);
        isPlaying = true;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            public void onCompletion(MediaPlayer arg0)
            {
                stop();
            }
        });

    }

    public void pause(){
        Log.d("TAG","pause");
        mediaPlayer.pause();
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
        isPlaying = false;
    }

    public void stop(){
        Log.d("TAG","stop");
        mediaPlayer.pause();
        playingTime = 0;
        mediaPlayer.seekTo((int) playingTime);
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
        isPlaying = false;
    }

    public void backward(){
        if(((int)playingTime-backwardTime)>0){
            playingTime = playingTime - backwardTime;
            mediaPlayer.seekTo((int) playingTime);
        }
        else{
            playingTime = 0;
            mediaPlayer.seekTo((int) playingTime);
        }
    }
    public void forward(){
        if(((int)playingTime+forwardTime)<=fullTime){
            playingTime = playingTime + forwardTime;
            mediaPlayer.seekTo((int) playingTime);
        }
        else{
            playingTime = fullTime;
            mediaPlayer.seekTo((int)playingTime);
        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            playingTime = mediaPlayer.getCurrentPosition();
            playingTimeText.setText(TimeUnit.MILLISECONDS.toMinutes((long) playingTime)+":"+
                                    (TimeUnit.MILLISECONDS.toSeconds((long) playingTime) -
                                     TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                     toMinutes((long) playingTime))));

            seekbar.setProgress((int)playingTime);
            myHandler.postDelayed(this, 100);
        }
    };

}
