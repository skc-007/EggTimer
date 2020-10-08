package com.skc.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    boolean startPressed;
    int remainingTime = 0;
    CountDownTimer mainCountDownTimer;
    CountDownTimer alarmCountDownTimer;
    MediaPlayer mediaPlayer;
    ImageView imageView;
    Button button;
    SeekBar seekBar;
    TextView textView;

    public void reset() {
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

        imageView.setImageResource(R.drawable.before);
        button.setText("START");
        startPressed = false;
        seekBar.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekBar);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        int maxTime = 120;
        seekBar.setMax(maxTime);
        seekBar.setProgress(0);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    int minutes = progress / 60;
                    int seconds = progress - (minutes * 60);

                    if (seconds < 10) {
                        textView.setText("0" + String.valueOf(minutes) + ":" + "0" + String.valueOf(seconds));
                    } else {
                        textView.setText("0" + String.valueOf(minutes) + ":" + String.valueOf(seconds));
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        startPressed = false;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!startPressed) {
                    remainingTime = seekBar.getProgress();
                    startPressed = true;
                    seekBar.setEnabled(false);
                    button.setText("STOP");

                    mainCountDownTimer = new CountDownTimer(remainingTime*1000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            int minutes = (int) (millisUntilFinished / 60000);
                            int seconds = (int) ((millisUntilFinished/1000) - (minutes * 60));
                            seekBar.setProgress((int) (millisUntilFinished/1000));
                            if(seconds < 10) {
                                textView.setText("0" + String.valueOf(minutes) + ":" + "0" + String.valueOf(seconds));
                            } else {
                                textView.setText("0" + String.valueOf(minutes) + ":" + String.valueOf(seconds));
                            }
                        }

                        @Override
                        public void onFinish() {
                            imageView.setImageResource(R.drawable.after);
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
                            mediaPlayer.start();
                            alarmCountDownTimer = new CountDownTimer(10000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {}

                                @Override
                                public void onFinish() {
                                    reset();
                                }
                            }.start();
                        }
                    }.start();
                } else {
                    reset();

                    mainCountDownTimer.cancel();

                    if(alarmCountDownTimer != null) {
                        alarmCountDownTimer.cancel();
                    }
                }
            }
        });

    }
}