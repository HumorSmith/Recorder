package com.freedomer.recorder;

/**
 * @author wuyihua
 * @since 2018/5/29
 */

public class RecorderManager implements RecorderApi {
    private RecorderApi recorderApi;
    private static final RecorderManager ourInstance = new RecorderManager();

    public static RecorderManager getInstance() {
        return ourInstance;
    }


    public void init(RecorderApi recorderApi){
        this.recorderApi = recorderApi;
    }

    private RecorderManager() {
    }

    @Override
    public void startRecorder(String path) {
        this.recorderApi.startRecorder(path);
    }

    @Override
    public void stopRecorder() {
        this.recorderApi.stopRecorder();
    }
}
