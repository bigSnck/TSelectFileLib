package com.yt.tselectlibrary.ui.contast;

import android.os.Parcel;
import android.os.Parcelable;

public class SelectParms implements Parcelable {

    private int maxCount;
    private boolean single;
    private FileType fileType;
    private int resultCode;

    public SelectParms(int maxCount, boolean single, FileType fileType, int resultCode) {
        this.maxCount = maxCount;
        this.single = single;
        this.fileType = fileType;
        this.resultCode = resultCode;
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


    public int getResultCode() {
        return resultCode;
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
        dest.writeInt(this.resultCode);
    }

    protected SelectParms(Parcel in) {
        this.maxCount = in.readInt();
        this.single = in.readByte() != 0;
        int tmpFileType = in.readInt();
        this.fileType = tmpFileType == -1 ? null : FileType.values()[tmpFileType];
        this.resultCode = in.readInt();
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
