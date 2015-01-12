package com.tatum.android;

import com.echonest.api.v4.EchoNestException;
import com.tatum.handlers.FileUploaderGDX;
import com.tatum.handlers.Uploader;

import java.io.IOException;

/**
 * Created by Ben on 08/01/2015.
 */
public class androidUploader implements Uploader {
    @Override
    public void upload() {
        FileUploaderGDX uploaderGDX = new FileUploaderGDX("/storage/removable/sdcard1/ALarum/09 Leftovers.mp3");
        try {
            uploaderGDX.uploadGDX();
        } catch (EchoNestException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
