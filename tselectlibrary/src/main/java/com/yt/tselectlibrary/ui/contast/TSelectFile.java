package com.yt.tselectlibrary.ui.contast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.yt.tselectlibrary.SelectedFileActivity;
import com.yt.tselectlibrary.ui.bean.CaptureStrategy;
import com.yt.tselectlibrary.ui.callback.OnResultCallback;

/**
 * 对外暴露的接口
 */
public class TSelectFile {
    private int mMaxCount; //最多选择多少张图片
    private FileType mFileType = FileType.IMAGE;
    private boolean mIsSingle ;//默认是多些
    private SelectedStyleType mStyleType = SelectedStyleType.COMMON;
    private boolean mIsShowCamra;//默认不显示拍照
    private OperationType mOperationType=OperationType.TakeSelect;//TakePhoto：仅仅拍照 TakeVideo：仅仅拍视频 TakeSelect：选择相册

    private CaptureStrategy mCaptureStrategy;

    private Context mContext;

    /**
     * 最多选择多少张图片
     *
     * @param context
     * @return
     */
    public TSelectFile from(Context context) {
        mContext = context;
        return this;
    }


    /**
     * 最多选择多少张图片
     *
     * @param maxCount
     * @return
     */
    public TSelectFile setSelectMax(int maxCount) {
        mMaxCount = maxCount;
        return this;
    }

    /**
     * 设置选中文件的类型  默认选择图片
     *
     * @param fileType 文件类型
     * @return
     */
    public TSelectFile setSeletctFileType(FileType fileType) {
        mFileType = fileType;
        return this;
    }

    /**
     * 设置成单选 默认为多选
     *
     * @return
     */
    public TSelectFile setSingle() {
        mIsSingle = true;
        return this;
    }

    /**
     * 设置选中的样式
     * @param selectedStyle
     * @return
     */
    public TSelectFile setSelectedStyle(SelectedStyleType selectedStyle) {
        mStyleType = selectedStyle;
        return this;
    }

    /**
     * 设置文件保存路径
     * @param captureStrategy
     * @return
     */
    public TSelectFile setCaptureStrategy(CaptureStrategy captureStrategy) {
        mCaptureStrategy=captureStrategy;
        return this;
    }

    /**
     * 设置是否显示相机
     * @param isShowCamra
     * @return
     */
    public TSelectFile setIsShowCamra(boolean isShowCamra) {
        mIsShowCamra=isShowCamra;
        return this;
    }


    /**
     * 设置 TakePhoto，TakeVideo，TakeSelect
     *
     * @param operationType
     * @return
     */
    public TSelectFile setOperationType(OperationType operationType) {
        mOperationType=operationType;
        return this;
    }


    public TSelectFile creat(OnResultCallback callback) {

        if (null == mContext||null==mCaptureStrategy) {
            throw new NullPointerException();
        }
        //mMaxCount, mIsSingle, mFileType, mStyleType,mIsShowCamra,mCaptureStrategy,mOperationType
        SelectParms selectParms=  new SelectParms();
        selectParms.setMaxCount(mMaxCount);
        selectParms.setSingle(mIsSingle);
        selectParms.setFileType(mFileType);
        selectParms.setShowCamra(mIsShowCamra);
        selectParms.setCaptureStrategy(mCaptureStrategy);
        selectParms.setOperationType(mOperationType);

        SelectedFileActivity.selectFile(mContext,selectParms,callback);
        return this;
    }




}
