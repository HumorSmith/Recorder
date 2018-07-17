package com.freedomer.recorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author wuyihua
 * @since 2018/5/29
 */

public class AudioRecorderImpl implements RecorderApi {
    private static final String TAG = AudioRecorderImpl.class.getSimpleName();
    private String tempPCMPath = Environment.getExternalStorageDirectory().getPath() + "/temp.pcm";
    private int mAudioChannel ;
    private int mAudioSamplingRate;
    private int mAudioEncoder ;
    private String mOutputFilePath ;
    private int mOutputFileFormat;
    private int mAudioEncodingBitRate ;
    private boolean isRecord = false;// 设置正在录制的状态
    private  int mAudioSource = MediaRecorder.AudioSource.MIC;

    //缓存的大小
    private  AudioRecord mAudioRecord;
    private String recorderPath;
    private  int bufferSize;

    public AudioRecorderImpl(int audioChannel, int audioSamplingRate, int audioEncoder, String outputFilePath, int outputFileFormat, int audioEncodingBitRate) {
        this.mAudioChannel = audioChannel;
        this.mAudioSamplingRate = audioSamplingRate;
        this.mAudioEncoder = audioEncoder;
        this.mOutputFilePath = outputFilePath;
        this.mOutputFileFormat = outputFileFormat;
        this.mAudioEncodingBitRate = audioEncodingBitRate;
        this.bufferSize = AudioRecord.getMinBufferSize(audioSamplingRate, audioChannel, AudioFormat.ENCODING_PCM_16BIT);
        Log.d(TAG,toString());
        this.mAudioRecord = new AudioRecord(mAudioSource,audioEncodingBitRate,audioChannel,AudioFormat.ENCODING_PCM_16BIT,bufferSize);

    }

    @Override
    public String toString() {
        return "AudioRecorderImpl{" +
                "tempPCMPath='" + tempPCMPath + '\'' +
                ", mAudioChannel=" + mAudioChannel +
                ", mAudioSamplingRate=" + mAudioSamplingRate +
                ", mAudioEncoder=" + mAudioEncoder +
                ", mOutputFilePath='" + mOutputFilePath + '\'' +
                ", mOutputFileFormat=" + mOutputFileFormat +
                ", mAudioEncodingBitRate=" + mAudioEncodingBitRate +
                ", isRecord=" + isRecord +
                ", mAudioSource=" + mAudioSource +
                ", mAudioRecord=" + mAudioRecord +
                ", recorderPath='" + recorderPath + '\'' +
                ", bufferSize=" + bufferSize +
                ", recorderRunnable=" + recorderRunnable +
                '}';
    }

    public static class Builder {
        private int mAudioChannel = 1;
        private int mAudioSamplingRate = 16000;
        private int mAudioEncoder = MediaRecorder.AudioEncoder.AAC;
        private String mOutputFilePath = Environment.getExternalStorageDirectory() + File.separator + "media.mp3";
        private int mOutputFileFormat = AudioFormat.ENCODING_PCM_16BIT;
        private int mAudioEncodingBitRate = 16000;

        public int getAudioChannel() {
            return mAudioChannel;
        }

        public AudioRecorderImpl.Builder setAudioChannel(int mAudioChannel) {
            this.mAudioChannel = mAudioChannel;
            return this;
        }

        public int getAudioSamplingRate() {
            return mAudioSamplingRate;
        }

        public AudioRecorderImpl.Builder setAudioSamplingRate(int mAudioSamplingRate) {
            this.mAudioSamplingRate = mAudioSamplingRate;
            return this;
        }

        public int getAudioEncoder() {
            return mAudioEncoder;
        }

        public AudioRecorderImpl.Builder setAudioEncoder(int mAudioEncoder) {
            this.mAudioEncoder = mAudioEncoder;
            return this;
        }

        public String getOutputFilePath() {
            return mOutputFilePath;
        }

        public AudioRecorderImpl.Builder setOutputFilePath(String mOutputFilePath) {
            this.mOutputFilePath = mOutputFilePath;
            return this;
        }

        public int getOutputFileFormat() {
            return mOutputFileFormat;
        }

        public AudioRecorderImpl.Builder setOutputFileFormat(int mOutputFileFormat) {
            this.mOutputFileFormat = mOutputFileFormat;
            return this;
        }

        public int getAudioEncodingBitRate() {
            return mAudioEncodingBitRate;
        }

        public AudioRecorderImpl.Builder setAudioEncodingBitRate(int mAudioEncodingBitRate) {
            this.mAudioEncodingBitRate = mAudioEncodingBitRate;
            return this;
        }

        public AudioRecorderImpl build() {
            AudioRecorderImpl mediaRecorder = new AudioRecorderImpl(mAudioChannel, mAudioSamplingRate, mAudioEncoder, mOutputFilePath, mOutputFileFormat, mAudioEncodingBitRate);
            return mediaRecorder;
        }
    }

    private Runnable recorderRunnable = new Runnable() {
        @Override
        public void run() {
            writeData();
            PcmToWavUtil pcmToWavUtil = new PcmToWavUtil();
            pcmToWavUtil.pcmToWav(tempPCMPath, recorderPath);
        }
    };


    @Override
    public void startRecorder(String path) {
        this.recorderPath = path;
        mAudioRecord.startRecording();
        isRecord = true;
        new Thread(recorderRunnable).start();
    }

    @Override
    public void stopRecorder() {
        isRecord = false;

        if (mAudioRecord != null) {
            mAudioRecord.stop();
        }

    }


    //将数据写入文件夹,文件的写入没有做优化
    public void writeData() {
        BufferedOutputStream os = null;
        byte[] noteArray = new byte[bufferSize];
        //建立文件输出流
        try {
            os = new BufferedOutputStream(new FileOutputStream(new File(tempPCMPath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (isRecord) {
            int recordSize = mAudioRecord.read(noteArray, 0, bufferSize);
            if (recordSize > 0) {
                try {
                    os.write(noteArray);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {

            }
        }
    }

}
