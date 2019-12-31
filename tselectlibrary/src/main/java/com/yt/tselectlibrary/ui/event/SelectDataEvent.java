package com.yt.tselectlibrary.ui.event;

import com.yt.tselectlibrary.ui.bean.SelectFileEntity;

import java.util.List;

public class SelectDataEvent {

    private List<SelectFileEntity> list;


    public SelectDataEvent(List<SelectFileEntity> list) {
        this.list = list;
    }

    public List<SelectFileEntity> getList() {
        return list;
    }
}
