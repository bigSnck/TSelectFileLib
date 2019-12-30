package com.yt.tselectlibrary.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yt.tselectlibrary.R;
import com.yt.tselectlibrary.ui.adapter.SelectImageAdapter;
import com.yt.tselectlibrary.ui.bean.CaptureStrategy;
import com.yt.tselectlibrary.ui.bean.FileType;
import com.yt.tselectlibrary.ui.bean.OnCamreCallback;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;
import com.yt.tselectlibrary.ui.callback.OnLoadDataCallback;
import com.yt.tselectlibrary.ui.callback.OnSelectedFileResultCallback;
import com.yt.tselectlibrary.ui.callback.OnUiSelectResultCallback;
import com.yt.tselectlibrary.ui.helper.LoadDataHelper;
import com.yt.tselectlibrary.ui.util.MediaStoreCompat;

import java.util.ArrayList;
import java.util.List;

public class SelectedImageFragment extends Fragment {

    private View mView;
    private RecyclerView mRlv;
    private SelectImageAdapter mImageAdapter;

    private List<SelectFileEntity> mListData;

    private OnUiSelectResultCallback mUiCallback;
    private boolean mIsShowCamra = false;//是否显示拍照
    private MediaStoreCompat mMediaStoreCompat;
    public final int REQUEST_CODE_CAPTURE = 24;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_selected_image_layout, container, false);


        mRlv = mView.findViewById(R.id.fragment_rlv_image);
        mMediaStoreCompat = new MediaStoreCompat(getActivity());

        mMediaStoreCompat.setCaptureStrategy(new CaptureStrategy(true, "com.yt.tselectfilelibrary.fileprovider","test"));

        initData();
        initAdapter();


        return mView;
    }

    private void initData() {
        mListData = new ArrayList<>();

        if (mIsShowCamra) {
            mListData.add(new SelectFileEntity(-1));
        }
        LoadDataHelper helper = LoadDataHelper.getInstance();
        helper.getLoadData(getActivity());
        helper.setOnLoadDataCallback(new OnLoadDataCallback() {
            @Override
            public void loadData(SelectFileEntity entity) {

                mListData.add(entity);
                if (mImageAdapter != null) {
                    mImageAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void initAdapter() {

        mImageAdapter = new SelectImageAdapter(getActivity(), mListData);
        mRlv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRlv.setAdapter(mImageAdapter);
        mImageAdapter.setOnSelectedFileCallback(new OnSelectedFileResultCallback() {
            @Override
            public void selected(List<SelectFileEntity> list, int count) {
                upDataCbv(list, count);
            }
        });

        mImageAdapter.setOnCamreCallback(new OnCamreCallback() {
            @Override
            public void openCamrae() {
                openCapture();
            }
        });
    }

    private void openCapture() {
        if (mMediaStoreCompat != null) {
            mMediaStoreCompat.dispatchCaptureIntent(getActivity(), REQUEST_CODE_CAPTURE);
        }
    }

    private void upDataCbv(List<SelectFileEntity> list, int count) {
        //更新一下底部的按钮

        if (null != mUiCallback) {
            mUiCallback.selected(list, count, FileType.IMAGE);
        }
    }

    public void setOnUiSelectResultCallback(OnUiSelectResultCallback callback) {
        mUiCallback = callback;
    }
}
