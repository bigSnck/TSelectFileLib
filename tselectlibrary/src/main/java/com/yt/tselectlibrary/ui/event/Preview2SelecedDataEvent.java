package com.yt.tselectlibrary.ui.event;

import com.yt.tselectlibrary.ui.bean.SelectFileEntity;

import java.util.List;

public class Preview2SelecedDataEvent {

    private List<SelectFileEntity> list;//全部的

    private List<SelectFileEntity> selectedList;//被选中的

    public Preview2SelecedDataEvent(List<SelectFileEntity> selectedList) {
        this.selectedList = selectedList;
    }


    public List<SelectFileEntity> getSelectedList() {
        return selectedList;
    }

    public List<SelectFileEntity> getList() {
        return list;
    }

    public Preview2SelecedDataEvent(List<SelectFileEntity> list, List<SelectFileEntity> selectedList) {
        this.list = list;
        this.selectedList = selectedList;
    }
}
