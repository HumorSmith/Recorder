package com.freedomer.recorder;

import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * @author wuyihua
 * @since 2018/5/29
 */

public class MediaRecorderImpl implements RecorderApi {
    private static final String TAG = MediaRecorderImpl.class.getSimpleName();
    private MediaRecorder mRecorder;
    private int mAudioChannel = 1;
    private int mAudioSamplingRate = 16000;
    private int mAudioEncoder = MediaRecorder.AudioEncoder.AAC;
    private String mOutputFilePath = Environment.getExternalStorageDirectory() + File.separator + "media.mp3";
    private int mOutputFileFormat = MediaRecorder.OutputFormat.THREE_GPP;
    private int mAudioEncodingBitRate = AudioFormat.ENCODING_PCM_16BIT;

    public MediaRecorderImpl(int mAudioChannel, int mAudioSamplingRate, int mAudioEncoder, String mOutputFilePath, int mOutputFileFormat, int mAudioEncodingBitRate) {
        this.mAudioChannel = mAudioChannel;
        this.mAudioSamplingRate = mAudioSamplingRate;
        this.mAudioEncoder = mAudioEncoder;
        this.mOutputFilePath = mOutputFilePath;
        this.mOutputFileFormat = mOutputFileFormat;
        this.mAudioEncodingBitRate = mAudioEncodingBitRate;
    }

    public static class Builder {
        private int mAudioChannel = 1;
        private int mAudioSamplingRate = 16000;
        private int mAudioEncoder = MediaRecorder.AudioEncoder.AAC;
        private String mOutputFilePath = Environment.getExternalStorageDirectory() + File.separator + "media.mp3";
        private int mOutputFileFormat = MediaRecorder.OutputFormat.THREE_GPP;
        private int mAudioEncodingBitRate = AudioFormat.ENCODING_PCM_16BIT;

        public int getAudioChannel() {
            return mAudioChannel;
        }

        public Builder setAudioChannel(int mAudioChannel) {
            this.mAudioChannel = mAudioChannel;
            return this;
        }

        public int getAudioSamplingRate() {
            return mAudioSamplingRate;
        }

        public Builder setAudioSamplingRate(int mAudioSamplingRate) {
            this.mAudioSamplingRate = mAudioSamplingRate;
            return this;
        }

        public int getAudioEncoder() {
            return mAudioEncoder;
        }

        public Builder setAudioEncoder(int mAudioEncoder) {
            this.mAudioEncoder = mAudioEncoder;
            return this;
        }

        public String getOutputFilePath() {
            return mOutputFilePath;
        }

        public Builder setOutputFilePath(String mOutputFilePath) {
            this.mOutputFilePath = mOutputFilePath;
            return this;
        }

        public int getOutputFileFormat() {
            return mOutputFileFormat;
        }

        public Builder setOutputFileFormat(int mOutputFileFormat) {
            this.mOutputFileFormat = mOutputFileFormat;
            return this;
        }

        public int getAudioEncodingBitRate() {
            return mAudioEncodingBitRate;
        }

        public Builder setAudioEncodingBitRate(int mAudioEncodingBitRate) {
            this.mAudioEncodingBitRate = mAudioEncodingBitRate;
            return this;
        }

        public MediaRecorderImpl build() {
            MediaRecorderImpl mediaRecorder = new MediaRecorderImpl(mAudioChannel, mAudioSamplingRate, mAudioEncoder, mOutputFilePath, mOutputFileFormat, mAudioEncodingBitRate);
            return mediaRecorder;
        }
    }


    @Override
    public void startRecorder(String path) {
        Log.d(TAG, "startRecorder path = " + path);
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(mOutputFileFormat);
        mRecorder.setOutputFile(mOutputFilePath);
        mRecorder.setAudioChannels(mAudioChannel);
        mRecorder.setAudioEncoder(mAudioEncoder);
        mRecorder.setAudioEncodingBitRate(mAudioEncodingBitRate);
        // 设置录音文件的清晰度
        mRecorder.setAudioSamplingRate(mAudioSamplingRate);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed " + e.getMessage());
        }


    }

    @Override
    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }
}
