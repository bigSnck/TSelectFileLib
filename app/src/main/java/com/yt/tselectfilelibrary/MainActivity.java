package com.yt.tselectfilelibrary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.View;


import com.yt.tselectlibrary.ui.bean.CaptureStrategy;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;
import com.yt.tselectlibrary.ui.callback.OnResultCallback;
import com.yt.tselectlibrary.ui.contast.FileType;
import com.yt.tselectlibrary.ui.contast.OperationType;
import com.yt.tselectlibrary.ui.contast.SelectedStyleType;
import com.yt.tselectlibrary.ui.contast.TSelectFile;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSelectClick(View view) {

        new TSelectFile()
                .from(this)
                .setSelectMax(5)
                .setSelectedStyle(SelectedStyleType.NUMBER)
                .setSeletctFileType(FileType.VIDEO)
                .setIsShowCamra(true)
                .setOperationType(OperationType.TakeSelect)
                .setCaptureStrategy(new CaptureStrategy(true, "com.yt.tselectfilelibrary.fileprovider", "test"))
                .creat(new OnResultCallback() {
                    @Override
                    public void successSingleResult(SelectFileEntity fileEntity) {
                          Log.i("AA","选中"+fileEntity.toString());
                    }

                    @Override
                    public void successResult(List<SelectFileEntity> resultList) {

                    }

                    @Override
                    public void errorResult(Exception es) {

                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("AA","进来了吗1"+"--------->");
    }
}
