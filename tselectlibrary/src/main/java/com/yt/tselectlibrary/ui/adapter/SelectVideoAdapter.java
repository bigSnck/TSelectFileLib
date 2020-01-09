package com.yt.tselectlibrary.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.yt.baseadapterlibrary.TBaseAdapter;
import com.yt.baseadapterlibrary.view.MultiTypeSupport;
import com.yt.baseadapterlibrary.view.ViewHolder;
import com.yt.tselectlibrary.R;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;
import com.yt.tselectlibrary.ui.callback.OnCameraCallback;
import com.yt.tselectlibrary.ui.callback.OnNotSelectCallback;
import com.yt.tselectlibrary.ui.callback.OnPreViewCallback;
import com.yt.tselectlibrary.ui.callback.OnSelectedFileResultCallback;
import com.yt.tselectlibrary.ui.contast.SelectedStyleType;
import com.yt.tselectlibrary.ui.widget.CheckView;

import java.util.ArrayList;
import java.util.List;

public class SelectVideoAdapter extends TBaseAdapter<SelectFileEntity> {
    private List<SelectFileEntity> mSelectedArray;
    private SelectedStyleType mSelectStyle;//表示有右上角的数字
    private OnSelectedFileResultCallback mOnSelectedFileResultCallback;
    private OnCameraCallback mOnCameraCallback;
    private OnPreViewCallback mOnPreViewCallback;
    private OnNotSelectCallback mOnNotSelectCallback;

    private int mMaxCount = -1;

    public SelectVideoAdapter(Context context, List<SelectFileEntity> data, int maxCount, SelectedStyleType selectStyle) {
        super(context, data, new MultiTypeSupport<SelectFileEntity>() {
            @Override
            public int getLayout(SelectFileEntity item, int postion) {
                if (item.getIdLong() == -1) {
                    return R.layout.adapter_photo_capture_item;
                } else {
                    return R.layout.adapter_selected_video_layout;
                }

            }
        });

        mSelectedArray = new ArrayList<>();
        mMaxCount = maxCount;
        mSelectStyle = selectStyle;

    }

    @Override
    public void convert(final ViewHolder holder, final SelectFileEntity selectFileEntity) {
        Log.i("AA", "adapter=" + selectFileEntity.toString());

        if (selectFileEntity.getIdLong() == -1) {//触发拍照按钮
            TextView itemTvHint = (TextView) holder.getView(R.id.item_tv_hint);
            itemTvHint.setText("录视频");
            itemTvHint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnCameraCallback != null) {
                        mOnCameraCallback.openCamera();
                    }
                }
            });
        } else {

            ImageView imageView = (ImageView) holder.getView(R.id.item_siv_image);

            holder.setText(R.id.item_tv_videoTime, selectFileEntity.getDurationTime());
            final CheckView checkView = (CheckView) holder.getView(R.id.item_check_view);
            Request request = Glide.with(mContext)
                    .load(selectFileEntity.getThumbnailPath())
                    .centerCrop()
                    .into(imageView)
                    .getRequest();

            if (request.isFailed()) {
                Glide.with(mContext)
                        .load(selectFileEntity.getOriginalPath())
                        .centerCrop()
                        .into(imageView);
            }

            checkView.setChecked(selectFileEntity.isSelected());

            if (mSelectStyle == SelectedStyleType.NUMBER) {
                checkView.setCheckedNum(selectFileEntity.getSelectIndex());
                if (selectFileEntity.isSelected()) {
                    checkView.setCountable(true);
                } else {
                    checkView.setCountable(false);
                }
            }

            checkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    upDataSelected(selectFileEntity);
                    if (mOnSelectedFileResultCallback != null) {
                        mOnSelectedFileResultCallback.selected(mSelectedArray, mSelectedArray.size());
                    }
                    notifyDataSetChanged();

                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mOnPreViewCallback != null) {
                        mOnPreViewCallback.openPreView(holder.getAdapterPosition());
                    }
                }
            });


        }


    }


    /**
     * 更新右上角的数字
     *
     * @param selectFileEntity
     */
    public void upDataSelected(SelectFileEntity selectFileEntity) {

        if (mSelectedArray.contains(selectFileEntity)) {
            mSelectedArray.remove(selectFileEntity);
        } else {
            if (mSelectedArray.size() >= mMaxCount) {
                if (mOnNotSelectCallback != null) {
                    selectFileEntity.setSelected(true);
                    mOnNotSelectCallback.notSelect();
                }
            } else {
                mSelectedArray.add(selectFileEntity);

            }
        }
        for (int i = 0; i < mSelectedArray.size(); i++) {
            mSelectedArray.get(i).setSelectIndex(i + 1);
        }
        selectFileEntity.setSelected(!selectFileEntity.isSelected());
    }


    public void setOnSelectedFileCallback(OnSelectedFileResultCallback callback) {
        mOnSelectedFileResultCallback = callback;
    }

    public void setOnCamreCallback(OnCameraCallback callback) {
        mOnCameraCallback = callback;
    }


    public void setOnPreViewCallback(OnPreViewCallback callback) {
        mOnPreViewCallback = callback;
    }

    public void setOnNotSelectCallback(OnNotSelectCallback callback) {
        mOnNotSelectCallback = callback;
    }

}
