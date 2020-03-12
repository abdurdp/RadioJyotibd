package com.zerobracket.radiojyotibd;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity  {
Button btnPause;
    ImageButton btnPlay;
MediaPlayer mediaPlayer;
ProgressBar progress_circular;
String stream = "http://188.165.192.5:9413/stream";
boolean prepared=false,
        started=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPause = findViewById(R.id.btnPause);
        btnPlay = findViewById(R.id.btnPlay);
        progress_circular = findViewById(R.id.progress_circular);
         mediaPlayer = new MediaPlayer();
        try {
            started = true;

            mediaPlayer.setDataSource(stream);

            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    progress_circular.setVisibility(View.GONE);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
          btnPlay.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(!started) {
                     mediaPlayer.start();
                     started=true;
                  }
                  else{
                      started=false;
                      mediaPlayer.pause();
                  }

              }
          });
          btnPause.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  mediaPlayer.pause();
              }
          });
        //catching the error if any
        mediaPlayer.setOnErrorListener((mediaPlayer,what,extra)->{
            mediaPlayer.reset();
            return false;
        });
    }



    @Override
    protected void onPause() {
        super.onPause();
        if(started){
            mediaPlayer.pause();
            started=false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(prepared){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }
}
