package com.example.herry_wang.week4_media_player;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;



public class MainActivity extends Activity {

    private MainFragment mainFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null){
            mainFragment=new MainFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, mainFragment);
            transaction.commit();
        }
    }

}
