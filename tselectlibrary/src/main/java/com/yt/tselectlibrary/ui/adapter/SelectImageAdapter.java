package com.yt.tselectlibrary.ui.adapter;

import android.content.Context;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yt.baseadapterlibrary.TBaseAdapter;
import com.yt.baseadapterlibrary.view.MultiTypeSupport;
import com.yt.baseadapterlibrary.view.ViewHolder;
import com.yt.tselectlibrary.R;
import com.yt.tselectlibrary.ui.bean.OnCamreCallback;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;
import com.yt.tselectlibrary.ui.callback.OnSelectedFileResultCallback;
import com.yt.tselectlibrary.ui.widget.CheckView;


import java.util.ArrayList;
import java.util.List;

public class SelectImageAdapter extends TBaseAdapter<SelectFileEntity> {
    private List<SelectFileEntity> mSelectedArray;
    private int mSelectStyle = 1;//表示有右上角的数字
    private OnSelectedFileResultCallback mOnSelectedFileResultCallback;
    private OnCamreCallback mOnCamreCallback;

    public SelectImageAdapter(Context context, List<SelectFileEntity> data) {
        super(context, data, new MultiTypeSupport<SelectFileEntity>() {
            @Override
            public int getLayout(SelectFileEntity item, int postion) {
                if (item.getIdInt() == -1) {
                    return R.layout.adapter_photo_capture_item;
                } else {
                    return R.layout.adapter_selected_image_layout;
                }

            }
        });

        mSelectedArray=new ArrayList<>();

    }

    @Override
    public void convert(ViewHolder holder, final SelectFileEntity selectFileEntity) {


        if (selectFileEntity.getIdInt() == -1) {//触发拍照按钮
            TextView itemTvHint = (TextView) holder.getView(R.id.item_tv_hint);
            itemTvHint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnCamreCallback != null) {
                        mOnCamreCallback.openCamrae();
                    }
                }
            });
        } else {

            ImageView imageView = (ImageView) holder.getView(R.id.item_siv_image);
            final CheckView checkView = (CheckView) holder.getView(R.id.item_check_view);
            Glide.with(mContext)
                    .load(selectFileEntity.getOriginalPath())
                    .centerCrop()
                    .into(imageView);

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
            checkView.setChecked(selectFileEntity.isSelected());

            if (mSelectStyle == 1) {
                checkView.setCheckedNum(selectFileEntity.getSelectIndex());
                if (selectFileEntity.isSelected()) {
                    checkView.setCountable(true);
                } else {
                    checkView.setCountable(false);
                }
            }

        }


    }


    /**
     * 更新右上角的数字
     *
     * @param selectFileEntity
     */
    public void upDataSelected(SelectFileEntity selectFileEntity) {
        selectFileEntity.setSelected(!selectFileEntity.isSelected());
        if (mSelectedArray.contains(selectFileEntity)) {
            mSelectedArray.remove(selectFileEntity);
        } else {
            mSelectedArray.add(selectFileEntity);
        }
        for (int i = 0; i < mSelectedArray.size(); i++) {
            mSelectedArray.get(i).setSelectIndex(i + 1);
        }
    }


    public void setOnSelectedFileCallback(OnSelectedFileResultCallback callback) {
        mOnSelectedFileResultCallback = callback;
    }

    public void setOnCamreCallback(OnCamreCallback callback) {
        mOnCamreCallback = callback;
    }

}
