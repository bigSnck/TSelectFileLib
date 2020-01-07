package com.yt.tselectlibrary.ui.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * description:媒体扫描
 */
public class SingleMediaScanner {


    private ScanListener mListener;
    private long createTime;
    private Context mContext;

    public interface ScanListener {

        /**
         * scan finish
         */
        void onScanFinish();
    }

    public SingleMediaScanner(Context context, ScanListener mListener) {

        this.mListener = mListener;
        mContext = context;

    }

    //针对非系统影音资源文件夹
    public void insertIntoMediaStore(boolean isVideo, String saveFileStr) {

        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(saveFileStr);
            mediaPlayer.prepare();
            long mDuration = mediaPlayer.getDuration();

            File saveFile = new File(saveFileStr);
            ContentResolver mContentResolver = mContext.getContentResolver();
            if (createTime == 0)
                createTime = System.currentTimeMillis();
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.TITLE, saveFile.getName());
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, saveFile.getName());
            values.put(MediaStore.MediaColumns.DURATION, mDuration);
            //值一样，但是还是用常量区分对待
            values.put(isVideo ? MediaStore.Video.VideoColumns.DATE_TAKEN
                    : MediaStore.Images.ImageColumns.DATE_TAKEN, createTime);
            values.put(MediaStore.MediaColumns.DATE_MODIFIED, System.currentTimeMillis());
            values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis());
            if (!isVideo)
                values.put(MediaStore.Images.ImageColumns.ORIENTATION, 0);
            values.put(MediaStore.MediaColumns.DATA, saveFile.getAbsolutePath());
            values.put(MediaStore.MediaColumns.SIZE, saveFile.length());
            values.put(MediaStore.MediaColumns.MIME_TYPE, isVideo ? "video/mp4" : "image/jpeg");
            //插入
            mContentResolver.insert(isVideo
                    ? MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    : MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            mListener.onScanFinish();

        } catch (IOException e) {

        }


    }


    public void insertIntoImageStore(Bitmap bitmap) {

        MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, null, null);

        mListener.onScanFinish();
    }

}
