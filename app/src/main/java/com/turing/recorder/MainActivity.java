package com.turing.recorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.freedomer.recorder.AudioRecorderImpl;
import com.freedomer.recorder.MediaRecorderImpl;
import com.freedomer.recorder.PcmToWavUtil;
import com.freedomer.recorder.RecorderManager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.recorder16PCMBtn).setOnClickListener(this);
        findViewById(R.id.stopRecorderBtn).setOnClickListener(this);
        findViewById(R.id.playPcmBtn).setOnClickListener(this);
        AudioRecorderImpl mediaRecorderImpl = new AudioRecorderImpl.Builder().build();
        RecorderManager.getInstance().init(mediaRecorderImpl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recorder16PCMBtn:
                RecorderManager.getInstance().startRecorder("/sdcard/hello.pcm");
                break;
            case R.id.stopRecorderBtn:
                RecorderManager.getInstance().stopRecorder();
                break;
            case R.id.playPcmBtn:
                PcmToWavUtil pcmToWavUtil = new PcmToWavUtil(16000, 1, AudioFormat.ENCODING_PCM_16BIT);
                pcmToWavUtil.pcmToWav("/sdcard/hello.pcm","/sdcard/hello.wav");
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource("/sdcard/hello.wav");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
                break;

        }
    }
}
