package com.yt.tselectfilelibrary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yt.tselectlibrary.SelectedImageActivity;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;
import com.yt.tselectlibrary.ui.contast.FileType;
import com.yt.tselectlibrary.ui.contast.TSelectFile;
import com.yt.tselectlibrary.ui.event.ResultEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
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
                .setSelectMax(10)
                .setSeletctFileType(FileType.IMAGE)
                .forResult(2001);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onSelectedResultEvent(ResultEvent event) {
        List list = event.getList();
        Log.i("AA", "结果" + list.toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        EventBus.getDefault().removeAllStickyEvents();
    }
}
