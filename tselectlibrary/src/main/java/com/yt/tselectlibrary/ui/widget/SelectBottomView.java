package com.yt.tselectlibrary.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yt.tselectlibrary.R;
import com.yt.tselectlibrary.ui.CheckRadioView;
import com.yt.tselectlibrary.ui.callback.OnViewClickCallback;
import com.yt.tselectlibrary.ui.contast.SelectedViewType;


public class SelectBottomView extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private View mView;

    private int mCount = 1;//选中图片的数目

    private TextView mTvPreview;
    private TextView mTvSelect;
    private CheckRadioView mCrOriginal;
    private TextView mTvOriginal;

    private OnViewClickCallback mClickCallback;
    private LinearLayout mLLOriginal;


    public SelectBottomView(Context context) {
        this(context, null);
    }

    public SelectBottomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectBottomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();

    }

    private void init() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.widget_selected_bottom_layout, this, false);
        //预览控件
        mTvPreview = mView.findViewById(R.id.widget_tv_preview);
        //选中
        mTvSelect = mView.findViewById(R.id.widget_tv_select);
        //原图
        mCrOriginal = mView.findViewById(R.id.widget_cr_original);
        mTvOriginal = mView.findViewById(R.id.widget_tv_original);
        mLLOriginal = mView.findViewById(R.id.widget_ll_original);

        addView(mView);
        setListener();
    }

    private void setListener() {
        mTvPreview.setOnClickListener(this);
        mLLOriginal.setOnClickListener(this);
        mTvSelect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.widget_tv_preview) {
            onPreviewClick();
        } else if (id == R.id.widget_tv_select) {
            onSelectedClick();
        } else if (id == R.id.widget_ll_original) {
            onOriginalClick();

        }
    }

    /***
     * 预览点击事件
     */
    private void onPreviewClick() {
        if (null != mClickCallback) {
            if (mCount > 0) {
                mClickCallback.selected(SelectedViewType.PREVIEW_VIEW);
            }
        }
    }

    /***
     * 原图点击事件
     */
    private void onOriginalClick() {
        if (null != mClickCallback) {

            updataOraiginal();
            mClickCallback.selected(SelectedViewType.ORIGINAL_VIEW);

        }
    }

    /***
     * 选中点击事件
     */
    private void onSelectedClick() {
        if (null != mClickCallback) {

            mClickCallback.selected(SelectedViewType.SELECTED_VIEW);

        }
    }

    /**
     * 更新ViewState
     */
    private void updataOraiginal() {
        if (mCrOriginal.getIsSeleced()) {
            mCrOriginal.setIsSeleced(!mCrOriginal.getIsSeleced());
        } else {
            mCrOriginal.setIsSeleced(!mCrOriginal.getIsSeleced());

        }
    }


    /**
     * 设置监听事件
     */


    /**
     * 更新选择图片的数目
     *
     * @param count
     */
    public void upDataSelectedFileCount(int count) {
        mCount = count;

        setAlpha(mTvPreview, 0.5f, 1f);
        setAlpha(mTvSelect, 0.5f, 1f);

        if (mCount > 0) {
            mTvSelect.setText("使用(" + mCount + ")");
        }
    }

    //提供View字体颜色大小随便怎么设置，自己看着办
    public TextView getTvPreviewView() {
        if (null != mTvPreview) {
            return mTvPreview;
        } else {
            return null;
        }

    }

    public TextView getTvSelectedView() {
        if (null != mTvSelect) {
            return mTvSelect;
        } else {
            return null;
        }
    }

    public TextView getTvOriginalView() {
        if (null != mTvOriginal) {
            return mTvOriginal;
        } else {
            return null;
        }
    }


    public void setOnViewClickCallback(OnViewClickCallback clickCallback) {
        mClickCallback = clickCallback;
    }


    private void setAlpha(View view, float minValue, float maxValue) {
        if (mCount > 0) {
            view.setAlpha(maxValue);
        } else {
            view.setAlpha(minValue);

        }
    }

}
