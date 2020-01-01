package com.yt.tselectlibrary.ui.event;

import com.yt.tselectlibrary.ui.bean.SelectFileEntity;

import java.util.List;

public class SelectDataEvent {



    private List<SelectFileEntity> list;//全部的

    private List<SelectFileEntity> mSelectedList;//被选中的

    private int position;
    public SelectDataEvent(List<SelectFileEntity> list) {
        this.list = list;
    }

    public SelectDataEvent(List<SelectFileEntity> list, List<SelectFileEntity> mSelectedList, int position) {
        this.list = list;
        this.mSelectedList = mSelectedList;
        this.position = position;
    }

    public List<SelectFileEntity> getList() {
        return list;
    }


    public void setList(List<SelectFileEntity> list) {
        this.list = list;
    }

    public List<SelectFileEntity> getmSelectedList() {
        return mSelectedList;
    }

    public void setmSelectedList(List<SelectFileEntity> mSelectedList) {
        this.mSelectedList = mSelectedList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
