package com.yt.tselectlibrary.ui.contast;


import android.os.Parcel;
import android.os.Parcelable;

public class SelectParms implements Parcelable {

    private int maxCount;
    private boolean single;
    private FileType fileType;
    private SelectedStyleType mStyleType;

    private boolean isShowCamra;


    public SelectParms(int maxCount, boolean single, FileType fileType) {
        this.maxCount = maxCount;
        this.single = single;
        this.fileType = fileType;
    }

    public SelectParms(int maxCount, boolean single, FileType fileType, SelectedStyleType mStyleType) {
        this.maxCount = maxCount;
        this.single = single;
        this.fileType = fileType;
        this.mStyleType = mStyleType;
    }

    public SelectParms(int maxCount, boolean single, FileType fileType, SelectedStyleType mStyleType, boolean isShowCamra) {
        this.maxCount = maxCount;
        this.single = single;
        this.fileType = fileType;
        this.mStyleType = mStyleType;
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
        return mStyleType;
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
        dest.writeInt(this.mStyleType == null ? -1 : this.mStyleType.ordinal());
        dest.writeByte(this.isShowCamra ? (byte) 1 : (byte) 0);
    }

    protected SelectParms(Parcel in) {
        this.maxCount = in.readInt();
        this.single = in.readByte() != 0;
        int tmpFileType = in.readInt();
        this.fileType = tmpFileType == -1 ? null : FileType.values()[tmpFileType];
        int tmpMStyleType = in.readInt();
        this.mStyleType = tmpMStyleType == -1 ? null : SelectedStyleType.values()[tmpMStyleType];
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
