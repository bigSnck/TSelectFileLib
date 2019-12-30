package com.yt.tselectlibrary.ui.bean;

public class SelectFileEntity {
    private boolean isSelected;//是否选中状态 false:未选中 true:选中状态
    private String originalPath;//源文件路径
    private String compressPath;//压缩后的文件路径
    private FileType fileType;//文件类型
    private int idInt;//唯一的id,如果idInt值等于-1,表示拍一张照片
    private int selectIndex;//选中的位置也就是右上角显示的数字

    public SelectFileEntity(int idInt) {
        this.idInt = idInt;
    }

    public SelectFileEntity(boolean isSelected, String originalPath, String compressPath, FileType fileType, int idInt) {
        this.isSelected = isSelected;
        this.originalPath = originalPath;
        this.compressPath = compressPath;
        this.fileType = fileType;
        this.idInt = idInt;
    }

    @Override
    public String toString() {
        return "SelectFileEntity{" +
                "isSelected=" + isSelected +
                ", originalPath='" + originalPath + '\'' +
                ", compressPath='" + compressPath + '\'' +
                ", fileType=" + fileType +
                '}';
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public int getIdInt() {
        return idInt;
    }

    public void setIdInt(int idInt) {
        this.idInt= idInt;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
