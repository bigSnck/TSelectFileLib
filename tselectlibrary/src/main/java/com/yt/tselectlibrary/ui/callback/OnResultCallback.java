package com.yt.tselectlibrary.ui.callback;

import com.yt.tselectlibrary.ui.bean.SelectFileEntity;

import java.util.List;

public interface OnResultCallback {

    void successSingleResult(SelectFileEntity fileEntity);

    void successResult(List<SelectFileEntity> resultList);

    void errorResult(Exception es);
}
