package com.yt.tselectfilelibrary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.yt.tselectlibrary.ui.bean.CaptureStrategy;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;
import com.yt.tselectlibrary.ui.callback.OnResultCallback;
import com.yt.tselectlibrary.ui.contast.FileType;
import com.yt.tselectlibrary.ui.contast.OperationType;
import com.yt.tselectlibrary.ui.contast.SelectedStyleType;
import com.yt.tselectlibrary.ui.contast.TSelectFile;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private TextView mTvResultShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvResultShow = findViewById(R.id.tv_result_show);
    }

    /**
     * 拍摄照片
     *
     * @param view
     */
    public void onImageClick(View view) {
        new TSelectFile()
                .from(this)
                .setOperationType(OperationType.TakePhoto)//三种模式：OperationType.TakePhoto：拍照 OperationType.TakeVideo：拍视频 OperationType.TakeSelect 选择图片
                .setCaptureStrategy(new CaptureStrategy(true, "com.yt.tselectfilelibrary.fileprovider", "test"))
                .creat(new OnResultCallback() {
                    @Override
                    public void successSingleResult(SelectFileEntity fileEntity) {
                        Log.i("AA", "选中" + fileEntity.toString());
                        mTvResultShow.setText(fileEntity.getOriginalPath());
                    }

                    @Override
                    public void successResult(List<SelectFileEntity> resultList) {

                    }

                    @Override
                    public void errorResult(Exception es) {

                    }
                });
    }

    /**
     * 拍摄录像
     *
     * @param view
     */
    public void onVideoClick(View view) {
        new TSelectFile()
                .from(this)
                .setOperationType(OperationType.TakeVideo)
                .setCaptureStrategy(new CaptureStrategy(true, "com.yt.tselectfilelibrary.fileprovider", "test"))
                .creat(new OnResultCallback() {
                    @Override
                    public void successSingleResult(SelectFileEntity fileEntity) {
                        Log.i("AA", "选中" + fileEntity.toString());
                        mTvResultShow.setText(fileEntity.getOriginalPath());
                    }

                    @Override
                    public void successResult(List<SelectFileEntity> resultList) {

                    }

                    @Override
                    public void errorResult(Exception es) {

                    }
                });
    }

    /**
     * 选择图片
     *
     * @param view
     */
    public void onSelectImageClick(View view) {

        new TSelectFile()
                .from(this)
                .setOperationType(OperationType.TakeSelect)//设置选择
                .setCaptureStrategy(new CaptureStrategy(true, "com.yt.tselectfilelibrary.fileprovider", "test"))//设置路径
                //.setSingle()//设置单选(只选一张)
                .setIsShowCamra(true)//是否显示相机 true:显示 false:不显示
                .setSelectMax(9)//最多选择多少张
                .setSelectedStyle(SelectedStyleType.COMMON) //普通样式 SelectedStyleType.COMMON: 数字样式：SelectedStyleType.NUMBER
                .setSeletctFileType(FileType.IMAGE)
                .creat(new OnResultCallback() {
                    @Override
                    public void successSingleResult(SelectFileEntity fileEntity) {

                    }

                    @Override
                    public void successResult(List<SelectFileEntity> resultList) {
                        mTvResultShow.setText(resultList.toString());
                    }

                    @Override
                    public void errorResult(Exception es) {

                    }
                });
    }

    /**
     * 选择视频
     *
     * @param view
     */
    public void onSelectVideoClick(View view) {
        new TSelectFile()
                .from(this)
                .setOperationType(OperationType.TakeSelect)//设置选择
                .setCaptureStrategy(new CaptureStrategy(true, "com.yt.tselectfilelibrary.fileprovider", "test"))//设置路径
                //.setSingle()//设置单选(只选一张)
                .setIsShowCamra(true)//是否显示相机 true:显示 false:不显示
                .setSelectMax(9)//最多选择多少张,没有设置的时候表示没有限制
                .setSelectedStyle(SelectedStyleType.NUMBER) //普通样式 SelectedStyleType.COMMON: 数字样式：SelectedStyleType.NUMBER
                .setSeletctFileType(FileType.VIDEO)//FileType.AL暂时不支持同时选择
                .creat(new OnResultCallback() {
                    @Override
                    public void successSingleResult(SelectFileEntity fileEntity) {

                    }

                    @Override
                    public void successResult(List<SelectFileEntity> resultList) {
                        mTvResultShow.setText(resultList.toString());
                    }

                    @Override
                    public void errorResult(Exception es) {

                    }
                });
    }
}
