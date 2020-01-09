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
    private OperationType operationType;

    private boolean isShowCamra;

    public SelectParms() {
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public SelectedStyleType getStyleType() {
        return styleType;
    }

    public void setStyleType(SelectedStyleType styleType) {
        this.styleType = styleType;
    }

    public CaptureStrategy getCaptureStrategy() {
        return captureStrategy;
    }

    public void setCaptureStrategy(CaptureStrategy captureStrategy) {
        this.captureStrategy = captureStrategy;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public boolean isShowCamra() {
        return isShowCamra;
    }

    public void setShowCamra(boolean showCamra) {
        isShowCamra = showCamra;
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
        dest.writeInt(this.operationType == null ? -1 : this.operationType.ordinal());
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
        int tmpOperationType = in.readInt();
        this.operationType = tmpOperationType == -1 ? null : OperationType.values()[tmpOperationType];
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
