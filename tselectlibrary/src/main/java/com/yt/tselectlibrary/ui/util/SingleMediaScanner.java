package com.yt.tselectlibrary.ui.util;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

/**
 * description:媒体扫描
 */
public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

    private MediaScannerConnection mMsc;
    private String mPath;
    private ScanListener mListener;

    public interface ScanListener {

        /**
         * scan finish
         */
        void onScanFinish();
    }

    public SingleMediaScanner(Context context, String mPath, ScanListener mListener) {
        this.mPath = mPath;
        this.mListener = mListener;
        this.mMsc = new MediaScannerConnection(context, this);
        this.mMsc.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mMsc.scanFile(mPath, null);
    }

    @Override
    public void onScanCompleted(String mPath, Uri mUri) {
        mMsc.disconnect();
        Log.i("AA","扫描------------->>");
        if (mListener != null) {
            Log.i("AA","扫描------------->>完成");

            mListener.onScanFinish();
        }
    }


}
