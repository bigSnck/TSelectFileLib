package com.yt.tselectlibrary.ui.contast;


import android.os.Parcel;
import android.os.Parcelable;

import com.yt.tselectlibrary.ui.bean.CaptureStrategy;

public class SelectParms implements Parcelable {

    private int maxCount;
    private boolean single;
    private FileType fileType;
    private SelectedStyleType styleType;
    private CaptureStrategy captureStrategy;

    private boolean isShowCamra;


    public SelectParms(int maxCount, boolean single, FileType fileType) {
        this.maxCount = maxCount;
        this.single = single;
        this.fileType = fileType;
    }

    public SelectParms(int maxCount, boolean single, FileType fileType, SelectedStyleType styleType) {
        this.maxCount = maxCount;
        this.single = single;
        this.fileType = fileType;
        this.styleType = styleType;
    }

    public SelectParms(int maxCount, boolean single, FileType fileType, SelectedStyleType mStyleType, boolean isShowCamra) {
        this.maxCount = maxCount;
        this.single = single;
        this.fileType = fileType;
        this.styleType = mStyleType;
        this.isShowCamra = isShowCamra;
    }

    public SelectParms(int maxCount, boolean single, FileType fileType, SelectedStyleType styleType, boolean isShowCamra, CaptureStrategy captureStrategy) {
        this.maxCount = maxCount;
        this.single = single;
        this.fileType = fileType;
        this.styleType = styleType;
        this.captureStrategy = captureStrategy;
        this.isShowCamra = isShowCamra;
    }

    public int getMaxCount() {
        return maxCount;
    }



    public boolean isSingle() {
        return single;
    }


    public FileType getFileType() {
        return fileType;
    }

    public SelectedStyleType getmStyleType() {
        return styleType;
    }

    public boolean isShowCamra() {
        return isShowCamra;
    }
    public CaptureStrategy getCaptureStrategy() {
        return captureStrategy;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.maxCount);
        dest.writeByte(this.single ? (byte) 1 : (byte) 0);
        dest.writeInt(this.fileType == null ? -1 : this.fileType.ordinal());
        dest.writeInt(this.styleType == null ? -1 : this.styleType.ordinal());
        dest.writeParcelable(this.captureStrategy, flags);
        dest.writeByte(this.isShowCamra ? (byte) 1 : (byte) 0);
    }

    protected SelectParms(Parcel in) {
        this.maxCount = in.readInt();
        this.single = in.readByte() != 0;
        int tmpFileType = in.readInt();
        this.fileType = tmpFileType == -1 ? null : FileType.values()[tmpFileType];
        int tmpStyleType = in.readInt();
        this.styleType = tmpStyleType == -1 ? null : SelectedStyleType.values()[tmpStyleType];
        this.captureStrategy = in.readParcelable(CaptureStrategy.class.getClassLoader());
        this.isShowCamra = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SelectParms> CREATOR = new Parcelable.Creator<SelectParms>() {
        @Override
        public SelectParms createFromParcel(Parcel source) {
            return new SelectParms(source);
        }

        @Override
        public SelectParms[] newArray(int size) {
            return new SelectParms[size];
        }
    };
}
