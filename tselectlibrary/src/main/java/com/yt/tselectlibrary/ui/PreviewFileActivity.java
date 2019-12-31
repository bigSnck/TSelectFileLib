package com.yt.tselectlibrary.ui;


import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.viewpager.widget.ViewPager;

import com.yt.tselectlibrary.R;
import com.yt.tselectlibrary.ui.adapter.PreviewPagerAdapter;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;

import com.yt.tselectlibrary.ui.event.ClickPreviewImageEvent;
import com.yt.tselectlibrary.ui.event.PreviewDataEvent;
import com.yt.tselectlibrary.ui.widget.CheckView;
import com.yt.tselectlibrary.ui.widget.PreviewViewPager;
import com.yt.tselectlibrary.ui.widget.SelectBottomView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class PreviewFileActivity extends AppCompatActivity {

    private PreviewViewPager mPreviewViewPager;
    private SelectBottomView mSelectBottomView;
    private FrameLayout mTopFrameLayout;
    private CheckView mCheckView;

    private List<SelectFileEntity> mList;
    private PreviewPagerAdapter mAdpter;
    private int mPostion;

    private int mTopHeight;
    private int mBottomHeight;
    private boolean mIsShow = true;//是否显示操作按钮
    private List<SelectFileEntity> mSelecedData;


    private boolean mIsChecked;//是否选中状态

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_file_layout);
        initView();

    }


    private void initAdapter() {
        mAdpter = new PreviewPagerAdapter(getSupportFragmentManager(), mList);
        mPreviewViewPager.setAdapter(mAdpter);


        mPreviewViewPager.setCurrentItem(mPostion, false);

        mPreviewViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPostion = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setBottomView();
        setTopView();
        Log.i("AA", "数据=" + mPostion);
    }

    private void initView() {

        mPreviewViewPager = findViewById(R.id.preview_pager);
        mSelectBottomView = findViewById(R.id.preview_file_sbv);
        mTopFrameLayout = findViewById(R.id.preview_top_toolbar);
        mCheckView = findViewById(R.id.preview_check_view);


        mTopFrameLayout.post(new Runnable() {
            @Override
            public void run() {
                mTopHeight = mTopFrameLayout.getMeasuredHeight();
            }
        });

        mSelectBottomView.post(new Runnable() {
            @Override
            public void run() {
                mBottomHeight = mSelectBottomView.getMeasuredHeight();
            }
        });

    }

    private void setTopView() {


        final SelectFileEntity entity = mList.get(mPostion);

        if (entity.isSelected()) {
            mIsChecked=true;
            mCheckView.setCountable(true);
            mCheckView.setCheckedNum(entity.getSelectIndex());
        }

        mCheckView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upDataTopView(mList.get(mPostion));
            }
        });
    }

    /**
     * 更新选中状态
     *
     * @param entity
     */
    private void upDataTopView(SelectFileEntity entity) {

        if (mSelecedData != null && mSelecedData.isEmpty()) {
            //如果一开始选了，那就走这
            if (mSelecedData.contains(entity)) {
                mSelecedData.remove(mPostion);
            } else {
                entity.setSelected(!entity.isSelected());
                mList.get(mPostion).setSelected(entity.isSelected());
                mSelecedData.add(entity);
            }
        }
        if(mSelecedData.isEmpty()){
            entity.setSelected(!entity.isSelected());
            mList.get(mPostion).setSelected(entity.isSelected());
            mSelecedData.add(entity);//如果一开始一个都没选，那就走这
        }

        for (int i = 0; i < mSelecedData.size(); i++) {
            mSelecedData.get(i).setSelectIndex(i + 1);
        }

        if(entity.isSelected()){
            Log.i("AA","lla"+"进来了");

            mCheckView.setCountable(true);
            mCheckView.setCheckedNum(mSelecedData.size());
        }else {
            mCheckView.setChecked(false);
            Log.i("AA","llb"+"进来了");
        }

    }

    private void setBottomView() {
        TextView mTvPreviewView = mSelectBottomView.getTvPreviewView();
        TextView mOriginalView = mSelectBottomView.getTvOriginalView();
        mTvPreviewView.setTextColor(getResources().getColor(R.color.t_commom_text_withe));
        mTvPreviewView.setText("返回");
        mTvPreviewView.setAlpha(1);
        mOriginalView.setTextColor(getResources().getColor(R.color.t_commom_text_withe));

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getFileDataEvent(PreviewDataEvent event) {
        mList = event.getList();
        mPostion = event.getPosition();
        mSelecedData = event.getmSelectedList();

        initAdapter();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void setClcikImageEvent(ClickPreviewImageEvent event) {
        //点击了图片

        if (mIsShow) {
            mIsShow = false;
            closeAnimView();
        } else {
            mIsShow = true;
            openAnimView();
        }

    }


    private void openAnimView() {
        mTopFrameLayout.animate().translationY(0).setInterpolator(new FastOutLinearInInterpolator()).start();
        mSelectBottomView.animate().translationY(0).setInterpolator(new FastOutLinearInInterpolator()).start();
    }

    private void closeAnimView() {

        mTopFrameLayout.animate().translationY(-mTopHeight).setInterpolator(new FastOutLinearInInterpolator()).start();
        mSelectBottomView.animate().translationY(mBottomHeight).setInterpolator(new FastOutLinearInInterpolator()).start();
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

        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
