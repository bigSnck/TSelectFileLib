/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yt.tselectlibrary.ui.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class CaptureStrategy implements Parcelable {

    public final boolean isPublic;
    public final String authority;
    public final String directory;

    public CaptureStrategy(boolean isPublic, String authority) {
        this(isPublic, authority, null);
    }

    public CaptureStrategy(boolean isPublic, String authority, String directory) {
        this.isPublic = isPublic;
        this.authority = authority;
        this.directory = directory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isPublic ? (byte) 1 : (byte) 0);
        dest.writeString(this.authority);
        dest.writeString(this.directory);
    }

    protected CaptureStrategy(Parcel in) {
        this.isPublic = in.readByte() != 0;
        this.authority = in.readString();
        this.directory = in.readString();
    }

    public static final Parcelable.Creator<CaptureStrategy> CREATOR = new Parcelable.Creator<CaptureStrategy>() {
        @Override
        public CaptureStrategy createFromParcel(Parcel source) {
            return new CaptureStrategy(source);
        }

        @Override
        public CaptureStrategy[] newArray(int size) {
            return new CaptureStrategy[size];
        }
    };
}
