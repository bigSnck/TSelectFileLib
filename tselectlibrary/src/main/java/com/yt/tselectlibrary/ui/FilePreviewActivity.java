package com.yt.tselectlibrary.ui;


import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class FilePreviewActivity extends AppCompatActivity {

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
    private int mMaxCount = -1;
    private boolean mIsSingle;
    private SelectedStyleType mSelectStyle;//表示有右上角的数字
    private SelectParms mSelectParms;
    private boolean mIsShowCamra;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_file_layout);

        mSelectParms = getIntent().getParcelableExtra("data");
        mMaxCount = mSelectParms.getMaxCount();
        //默认是多些
        mIsSingle = mSelectParms.isSingle();
        mSelectStyle = mSelectParms.getmStyleType();
        mIsShowCamra = mSelectParms.isShowCamra();
        if (mIsSingle) {
            mMaxCount = 1;
        }
        mSelecedData = new ArrayList<>();
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
                updataTopShow(mList.get(mPostion));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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

                    if(mIsShowCamra){
                        mList.add(0,new SelectFileEntity(-1));
                        Log.i("AA","进来了"+mIsShowCamra);
                    }
                    Log.i("AA","进来了="+mList.size());
                    EventBus.getDefault().postSticky(new SelectDataEvent(mList, mSelecedData, mPostion));
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
            mIsChecked = true;

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


        try {
            if (mSelecedData != null && !mSelecedData.isEmpty()) {

                if (mSelecedData.contains(entity)) {
                    mSelecedData.remove(entity);
                } else {
                    if (mSelecedData.size() >= mMaxCount) {
                        Toast.makeText(this, "最多选" + mMaxCount + "张", Toast.LENGTH_SHORT).show();
                        entity.setSelected(true);
                    } else {
                        mSelecedData.add(entity);
                    }
                }
            } else {
                mSelecedData.add(entity);

            }


        } catch (NullPointerException e) {
            e.getCause();
            mSelecedData = new ArrayList<>();
            mSelecedData.add(entity);
        }
        entity.setSelected(!entity.isSelected());

        for (int i = 0; i < mSelecedData.size(); i++) {
            mSelecedData.get(i).setSelectIndex(i + 1);
        }


        updataTopShow(entity);
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
    public void getFileDataEvent(PreviewDataEvent event) {
        mList = event.getList();
        mPostion = event.getPosition();
        mPostion = mPostion - 1;
        mSelecedData = event.getmSelectedList();
        if (mIsShowCamra) {
            if (mList.get(0).getIdInt() == -1) {
                mList.remove(0);
            }
        }

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
            //do something.
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

}
