package com.yt.tselectlibrary.ui;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
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
import com.yt.tselectlibrary.ui.callback.OnViewClickCallback;
import com.yt.tselectlibrary.ui.contast.FileType;
import com.yt.tselectlibrary.ui.contast.SelectParms;
import com.yt.tselectlibrary.ui.contast.SelectedStyleType;
import com.yt.tselectlibrary.ui.contast.SelectedViewType;
import com.yt.tselectlibrary.ui.event.ClickPreviewImageEvent;
import com.yt.tselectlibrary.ui.event.FilePreviewDataEvent;
import com.yt.tselectlibrary.ui.event.Preview2SelecedDataEvent;
import com.yt.tselectlibrary.ui.event.PreviewDataEvent;
import com.yt.tselectlibrary.ui.event.SelectDataEvent;
import com.yt.tselectlibrary.ui.widget.CheckView;
import com.yt.tselectlibrary.ui.widget.PreviewViewPager;
import com.yt.tselectlibrary.ui.widget.SelectBottomView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class SelectedFilePreviewActivity extends AppCompatActivity {

    private PreviewViewPager mPreviewViewPager;
    private SelectBottomView mSelectBottomView;
    private FrameLayout mTopFrameLayout;
    private CheckView mCheckView;


    private PreviewPagerAdapter mAdpter;
    private int mPostion;

    private int mTopHeight;
    private int mBottomHeight;
    private boolean mIsShow = true;//是否显示操作按钮
    private List<SelectFileEntity> mSelecedData;
    private List<SelectFileEntity> mList;
    private List<SelectFileEntity> mAllList;


    private SelectedStyleType mSelectStyle;//表示有右上角的数字
    private SelectParms mSelectParms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_file_layout);
        mSelectParms = getIntent().getParcelableExtra("data");
        mSelectStyle = mSelectParms.getmStyleType();
        mList = new ArrayList<>();
        initView();

    }


    private void initAdapter() {
        mAdpter = new PreviewPagerAdapter(getSupportFragmentManager(), mList);


        mPreviewViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPostion = position;
                updataTopShow(mList.get(mPostion));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPreviewViewPager.setCurrentItem(mPostion, false);

        mPreviewViewPager.setAdapter(mAdpter);


        mPreviewViewPager.setOffscreenPageLimit(3);


        setBottomView();
        setTopView();
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


        mSelectBottomView.setOnViewClickCallback(new OnViewClickCallback() {
            @Override
            public void selected(SelectedViewType selectedViewType) {
                if (selectedViewType == SelectedViewType.PREVIEW_VIEW) {

                    EventBus.getDefault().postSticky(new Preview2SelecedDataEvent(mAllList, mSelecedData));
                    finish();
                }
            }
        });

        if (mSelectParms.getFileType() == FileType.IMAGE) {
            mSelectBottomView.setLLOraiginalView(View.VISIBLE);
        } else {
            mSelectBottomView.setLLOraiginalView(View.INVISIBLE);
        }
    }

    private void setTopView() {


        final SelectFileEntity entity = mList.get(mPostion);

        if (entity.isSelected()) {


            if (mSelectStyle == SelectedStyleType.NUMBER) {
                mCheckView.setCountable(true);
                mCheckView.setCheckedNum(entity.getSelectIndex());
            } else {
                mCheckView.setChecked(true);
            }
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

        if (!mSelecedData.isEmpty()) {
            if (mSelecedData.contains(entity)) {
                mSelecedData.remove(entity);
            } else {
                mSelecedData.add(entity);
            }
        } else {
            mSelecedData.add(entity);
        }

        entity.setSelected(!entity.isSelected());

        for (int i = 0; i < mSelecedData.size(); i++) {
            mSelecedData.get(i).setSelectIndex(i + 1);
        }


        updataTopShow(entity);


        if (mAdpter != null) {
            mAdpter.notifyDataSetChanged();
        }

    }

    private void updataTopShow(SelectFileEntity entity) {
        if (entity.isSelected()) {
            if (mSelectStyle == SelectedStyleType.NUMBER) {
                mCheckView.setCountable(true);
                mCheckView.setCheckedNum(entity.getSelectIndex());
            } else {
                mCheckView.setChecked(true);
            }

        } else {
            mCheckView.setCountable(false);
            mCheckView.setChecked(false);

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
    public void getFilePreviewDataEvent(FilePreviewDataEvent event) {

        mList.addAll(event.getSelectedList());
        mPostion = 0;//默认第一张
        mSelecedData = event.getSelectedList();
        mAllList = event.getList();

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

    /**
     * 屏蔽实体返回键
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}
