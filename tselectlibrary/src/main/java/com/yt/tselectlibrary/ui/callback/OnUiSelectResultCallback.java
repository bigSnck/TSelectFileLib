package com.yt.tselectlibrary.ui.callback;

import com.yt.tselectlibrary.ui.bean.FileType;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;

import java.util.List;

public interface OnUiSelectResultCallback {

    void selected(List<SelectFileEntity> list, int count, FileType fileType);
}
