package com.yt.tselectfilelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import com.yt.tselectlibrary.ui.contast.FileType;
import com.yt.tselectlibrary.ui.contast.SelectedStyleType;
import com.yt.tselectlibrary.ui.contast.TSelectFile;
import com.yt.tselectlibrary.ui.event.ResultEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
                .setSeletctFileType(FileType.IMAGE)
                .setIsShowCamra(true)
                .creat();
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onSelectedResultEvent(ResultEvent event) {
        List list = event.getList();
        Toast.makeText(this, "选中了" + list.size() + "张图片", Toast.LENGTH_SHORT).show();

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
