package com.yt.tselectlibrary.ui.callback;

import com.yt.tselectlibrary.ui.contast.FileType;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;

import java.util.List;

public interface OnUiSelectResultCallback {

    void selected(List<SelectFileEntity> allList,List<SelectFileEntity> selectedFileList, int count, FileType fileType);
}
