package com.yt.tselectlibrary.ui.callback;

import com.yt.tselectlibrary.ui.bean.SelectFileEntity;

import java.util.List;

public interface OnSelectedFileResultCallback {

    void selected(List<SelectFileEntity> list,int count);
}
