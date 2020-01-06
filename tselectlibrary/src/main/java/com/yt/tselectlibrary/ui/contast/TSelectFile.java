package com.yt.tselectlibrary.ui.contast;

import android.content.Context;
import android.content.Intent;

import com.yt.tselectlibrary.SelectedFileActivity;

/**
 * 对外暴露的接口
 */
public class TSelectFile {
    private int mMaxCount; //最多选择多少张图片
    private FileType mFileType = FileType.IMAGE;
    private boolean mIsSingle = false;//默认是多些
    private SelectedStyleType mStyleType = SelectedStyleType.COMMON;
    private boolean mIsShowCamra=false;//默认不显示拍照

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
     * 设置是否显示相机
     * @param isShowCamra
     * @return
     */
    public TSelectFile setIsShowCamra(boolean isShowCamra) {
        mIsShowCamra=isShowCamra;
        return this;
    }

    public TSelectFile creat() {

        if (null == mContext) {
            throw new NullPointerException();
        }
        Intent intent = new Intent(mContext, SelectedFileActivity.class);
        intent.putExtra("data", new SelectParms(mMaxCount, mIsSingle, mFileType, mStyleType,mIsShowCamra));
        mContext.startActivity(intent);
        return this;

    }


}
