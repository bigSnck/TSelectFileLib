package com.yt.tselectlibrary.ui.event;

import com.yt.tselectlibrary.ui.bean.SelectFileEntity;

import java.util.List;

public class PreviewDataEvent {

    private List<SelectFileEntity> list;//全部的

    private List<SelectFileEntity> mSelectedList;//被选中的

    private int position;


    public PreviewDataEvent(List<SelectFileEntity> list) {
        this.list = list;
    }



    public PreviewDataEvent(List<SelectFileEntity> list, int position) {
        this.list = list;
        this.position = position;
    }

    public PreviewDataEvent(List<SelectFileEntity> list, List<SelectFileEntity> mSelectedList, int position) {
        this.list = list;
        this.mSelectedList = mSelectedList;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
    public List<SelectFileEntity> getList() {
        return list;
    }

    public List<SelectFileEntity> getmSelectedList() {
        return mSelectedList;
    }

    public void setmSelectedList(List<SelectFileEntity> mSelectedList) {
        this.mSelectedList = mSelectedList;
    }
}
