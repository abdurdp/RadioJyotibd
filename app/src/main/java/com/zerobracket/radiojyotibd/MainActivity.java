package com.zerobracket.radiojyotibd;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageButton btnPause;
    ImageButton btnPlay, ibPower;
    MediaPlayer mediaPlayer;
    ProgressBar progress_circular;
    SeekBar seek_bar;
    String stream = "http://188.165.192.5:9413/stream";
    boolean prepared = false,
            started = false;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPause = findViewById(R.id.btnPause);
        btnPlay = findViewById(R.id.btnPlay);
        progress_circular = findViewById(R.id.progress_circular);
        seek_bar = findViewById(R.id.seek_bar);
        ibPower = findViewById(R.id.ibPower);
        mediaPlayer = new MediaPlayer();
        btnPlay.setEnabled(false);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        try {
            initControls();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        ActivityCompat.requestPermissions(MainActivity.this,
//                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                1);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!started) {
                    mediaPlayer.start();
                    started = true;
                    btnPlay.setVisibility(View.INVISIBLE);
                    btnPause.setVisibility(View.VISIBLE);
                } else {
                    started = false;
                    mediaPlayer.pause();
                    btnPause.setVisibility(View.INVISIBLE);
                    btnPlay.setVisibility(View.VISIBLE);
                }

            }
        });
        try {
            prepareRadio();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!started) {
                    mediaPlayer.start();
                    started = true;
                    btnPlay.setVisibility(View.INVISIBLE);
                    btnPause.setVisibility(View.VISIBLE);
                } else {
                    started = false;
                    mediaPlayer.pause();
                    btnPause.setVisibility(View.INVISIBLE);
                    btnPlay.setVisibility(View.VISIBLE);

                }
            }
        });
        //catching the error if any
        mediaPlayer.setOnErrorListener((mediaPlayer, what, extra) -> {
            mediaPlayer.reset();
            Log.d("ERRORFOUND", " " + what + " " + extra);
            return false;
        });
        ibPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    finish();
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void prepareRadio() {
        if (!prepared) {
            try {
                mediaPlayer.setDataSource(stream);
                progress_circular.setVisibility(View.VISIBLE);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        btnPlay.setVisibility(View.INVISIBLE);
                        btnPause.setVisibility(View.VISIBLE);
                        started = true;
                        progress_circular.setVisibility(View.GONE);
                        prepared = true;
                        btnPlay.setEnabled(true);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mediaPlayer.reset();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(!started){
//                    mediaPlayer.release();
//                    started=false;
//                    prepared=false;
//                    mediaPlayer=null;
//                }
//            }
//        }, 30000);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (started) {
                if (prepared) {
                    mediaPlayer.start();
                    btnPlay.setVisibility(View.INVISIBLE);
                    btnPause.setVisibility(View.VISIBLE);
                } else {
                    prepareRadio();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (started) {
                mediaPlayer.pause();
                btnPause.setVisibility(View.INVISIBLE);
                btnPlay.setVisibility(View.VISIBLE);
            }
            if (prepared) {
                mediaPlayer.release();
                prepared = false;
                mediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case 1: {
//
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }

    private void initControls() {
        try {
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            seek_bar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            seek_bar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));


            seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
