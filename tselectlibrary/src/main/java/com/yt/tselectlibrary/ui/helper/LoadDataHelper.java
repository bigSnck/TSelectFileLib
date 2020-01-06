package com.yt.tselectlibrary.ui.helper;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.yt.tselectlibrary.ui.contast.FileType;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;
import com.yt.tselectlibrary.ui.callback.OnLoadDataCallback;
import com.yt.tselectlibrary.ui.util.DateUtils;

import java.io.File;

public class LoadDataHelper {
    private volatile static LoadDataHelper mLoadDataHelper;
    private Activity mActivity;
    // 加载所有的数据，唯一的id，啥值都行
    private static final int LOADER_TYPE_IMAGE = 0x0055;
    private static final int LOADER_TYPE_VIDEO = 0x0056;
    private OnLoadDataCallback mLoadCallback;


    private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media._ID};

    private final String[] VIDEO_PROJECTION = {
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DURATION,
    };

    public static LoadDataHelper getInstance() {

        if (null == mLoadDataHelper) {

            synchronized (LoadDataHelper.class) {
                if (null == mLoadDataHelper) {
                    mLoadDataHelper = new LoadDataHelper();
                }

            }

        }


        return mLoadDataHelper;
    }

    private LoadDataHelper() {
    }

    public void getLoadData(Activity activity, FileType fileType) {
        if (fileType == FileType.IMAGE) {
            getLoadImageData(activity, fileType);
        }
        if (fileType == FileType.VIDEO) {
            getLoadVideoData(activity, fileType);
        }
    }

    public void getLoadImageData(final Activity activity, final FileType fileType) {
        activity.getLoaderManager().initLoader(LOADER_TYPE_IMAGE, null, new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader cursorLoader = new CursorLoader(activity,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null,null, IMAGE_PROJECTION[1] + " DESC");
                return cursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

                // 如果有数据变量数据
                if (data != null && data.getCount() > 0) {


                    // 不断的遍历循环
                    while (data.moveToNext()) {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        // String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        //long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        long IdLong = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));


                        // 判断文件是不是存在
                        if (!pathExist(path)) {
                            continue;
                        }

                        SelectFileEntity entity = new SelectFileEntity(false, path, path, fileType, (int) IdLong);
                        mLoadCallback.loadData(entity);


                    }


                }
            }

            /**
             * 判断该路径文件是不是存在
             */
            private boolean pathExist(String path) {
                if (!TextUtils.isEmpty(path)) {
                    return new File(path).exists();
                }
                return false;
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });

    }

    public void getLoadVideoData(final Activity activity, final FileType fileType) {
        activity.getLoaderManager().initLoader(LOADER_TYPE_VIDEO, null, new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader cursorLoader = new CursorLoader(activity,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJECTION,
                        null,null, VIDEO_PROJECTION[1] + " DESC");
                return cursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

                // 如果有数据变量数据
                if (data != null && data.getCount() > 0) {
                    // 不断的遍历循环
                    while (data.moveToNext()) {
                        String path = data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[0]));
                        long IdLong = data.getLong(data.getColumnIndexOrThrow(VIDEO_PROJECTION[4]));
                        long durationTime = data.getLong(data.getColumnIndexOrThrow(VIDEO_PROJECTION[5]));
                        String durationTimeStr = DateUtils.timeParse(durationTime);
                        // 判断文件是不是存在
                        if (!pathExist(path)) {
                            continue;
                        }
                        SelectFileEntity entity = new SelectFileEntity(false, path, path,fileType, (int) IdLong, durationTimeStr);
                        mLoadCallback.loadData(entity);


                    }


                }
            }

            /**
             * 判断该路径文件是不是存在
             */
            private boolean pathExist(String path) {
                if (!TextUtils.isEmpty(path)) {
                    return new File(path).exists();
                }
                return false;
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });

    }


    public void setOnLoadDataCallback(OnLoadDataCallback callback) {
        mLoadCallback = callback;
    }
}
