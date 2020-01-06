package com.yt.tselectlibrary.ui.event;

import com.yt.tselectlibrary.ui.bean.SelectFileEntity;

import java.util.List;

public class ResultEvent {
    private List<SelectFileEntity> list;

    public ResultEvent(List<SelectFileEntity> list) {
        this.list = list;
    }

    public List<SelectFileEntity> getList() {
        return list;
    }


}
