package com.yt.tselectlibrary.ui;

import android.content.Intent;
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
import com.yt.tselectlibrary.ui.callback.OnCameraCallback;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;
import com.yt.tselectlibrary.ui.callback.OnLoadDataCallback;
import com.yt.tselectlibrary.ui.callback.OnPreViewCallback;
import com.yt.tselectlibrary.ui.callback.OnSelectedFileResultCallback;
import com.yt.tselectlibrary.ui.callback.OnUiSelectResultCallback;
import com.yt.tselectlibrary.ui.event.FilePreviewDataEvent;
import com.yt.tselectlibrary.ui.event.PreviewDataEvent;

import com.yt.tselectlibrary.ui.event.SelectDataEvent;
import com.yt.tselectlibrary.ui.helper.LoadDataHelper;
import com.yt.tselectlibrary.ui.util.MediaStoreCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class SelectedImageFragment extends Fragment {

    private View mView;
    private RecyclerView mRlv;
    private SelectImageAdapter mImageAdapter;

    private List<SelectFileEntity> mListData;
    private List<SelectFileEntity> mSeleetedData;

    private OnUiSelectResultCallback mUiCallback;
    private boolean mIsShowCamra = true;//是否显示拍照
    private MediaStoreCompat mMediaStoreCompat;
    public final int REQUEST_CODE_CAPTURE = 24;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_selected_image_layout, container, false);


        mRlv = mView.findViewById(R.id.fragment_rlv_image);
        mMediaStoreCompat = new MediaStoreCompat(getActivity());

        mMediaStoreCompat.setCaptureStrategy(new CaptureStrategy(true, "com.yt.tselectfilelibrary.fileprovider", "test"));

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

        mImageAdapter.setOnCamreCallback(new OnCameraCallback() {
            @Override
            public void openCamera() {
                openCapture();
            }
        });

        mImageAdapter.setOnPreViewCallback(new OnPreViewCallback() {
            @Override
            public void openPreView(int postion) {

                goIntent(postion);
            }
        });
    }

    private void goIntent(int postion) {

        if (mIsShowCamra) {
            mListData.remove(0);
            postion = postion - 1;
        }
        EventBus.getDefault().postSticky(new PreviewDataEvent(mListData, mSeleetedData, postion));
        Intent intent = new Intent(getActivity(), FilePreviewActivity.class);
        startActivity(intent);
    }

    private void openCapture() {
        if (mMediaStoreCompat != null) {
            mMediaStoreCompat.dispatchCaptureIntent(getActivity(), REQUEST_CODE_CAPTURE);
        }
    }

    private void upDataCbv(List<SelectFileEntity> list, int count) {
        //更新一下底部的按钮

        if (null != mUiCallback) {
            mSeleetedData = list;
            mUiCallback.selected(list, count, FileType.IMAGE);
        }
    }

    public void setOnUiSelectResultCallback(OnUiSelectResultCallback callback) {
        mUiCallback = callback;
    }


    /**
     * 来自预览选泽的图片
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onSelectedFileEvent(SelectDataEvent event) {


        mListData = event.getList();
        if (mIsShowCamra) {
            mListData.add(0, new SelectFileEntity(-1));
        }
        mImageAdapter.notifyDataSetChanged();

        if (null != mUiCallback) {

            mSeleetedData = event.getmSelectedList();
            mUiCallback.selected(mSeleetedData, mSeleetedData.size(), FileType.IMAGE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }

    }

    @Override
    public void onDestroy() {

        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
