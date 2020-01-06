package com.yt.tselectlibrary.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yt.tselectlibrary.R;
import com.yt.tselectlibrary.ui.FilePreviewActivity;
import com.yt.tselectlibrary.ui.adapter.SelectImageAdapter;
import com.yt.tselectlibrary.ui.adapter.SelectVideoAdapter;
import com.yt.tselectlibrary.ui.bean.CaptureStrategy;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;
import com.yt.tselectlibrary.ui.callback.OnCameraCallback;
import com.yt.tselectlibrary.ui.callback.OnLoadDataCallback;
import com.yt.tselectlibrary.ui.callback.OnNotSelectCallback;
import com.yt.tselectlibrary.ui.callback.OnPreViewCallback;
import com.yt.tselectlibrary.ui.callback.OnSelectedFileResultCallback;
import com.yt.tselectlibrary.ui.callback.OnUiSelectResultCallback;
import com.yt.tselectlibrary.ui.contast.FileType;
import com.yt.tselectlibrary.ui.contast.SelectParms;
import com.yt.tselectlibrary.ui.contast.SelectedStyleType;
import com.yt.tselectlibrary.ui.event.Preview2SelecedDataEvent;
import com.yt.tselectlibrary.ui.event.PreviewDataEvent;
import com.yt.tselectlibrary.ui.event.SelectDataEvent;
import com.yt.tselectlibrary.ui.helper.LoadDataHelper;
import com.yt.tselectlibrary.ui.util.MediaStoreCompat;
import com.yt.tselectlibrary.ui.util.SingleMediaScanner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class SelectedVideoFragment extends Fragment {

    private View mView;
    private RecyclerView mRlv;
    private SelectVideoAdapter mVideoAdapter;

    private List<SelectFileEntity> mListData;
    private List<SelectFileEntity> mSeleetedData;

    private OnUiSelectResultCallback mUiCallback;

    private MediaStoreCompat mMediaStoreCompat;
    public final int REQUEST_CODE_CAPTURE = 24;

    private int mMaxCount; //最多选择多少张图片

    private boolean mIsSingle = false;//默认是多些
    private SelectedStyleType mSelectStyle;//表示有右上角的样式
    private boolean mIsShowCamra = true;//是否显示拍照
    private FileType mFileType;
    private SelectParms mSelectParms;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_selected_image_layout, container, false);


        Bundle bundle = getArguments();
        mSelectParms = bundle.getParcelable("data");
        mMaxCount = mSelectParms.getMaxCount();
        mIsSingle = mSelectParms.isSingle();//默认是多些
        mSelectStyle = mSelectParms.getmStyleType();
        mIsShowCamra = mSelectParms.isShowCamra();
        mFileType = mSelectParms.getFileType();

        if (mIsSingle) {
            mMaxCount = 1;
        }

        mRlv = mView.findViewById(R.id.fragment_rlv_image);
        mMediaStoreCompat = new MediaStoreCompat(getActivity(), this);

        mMediaStoreCompat.setCaptureStrategy(new CaptureStrategy(true, "com.yt.tselectfilelibrary.fileprovider", "test"));

        initData();
        initAdapter();


        return mView;
    }

    private void initData() {
        mListData = new ArrayList<>();
        updataIsShowCamra();
        LoadDataHelper helper = LoadDataHelper.getInstance();
        helper.getLoadData(getActivity(), mFileType);
        helper.setOnLoadDataCallback(new OnLoadDataCallback() {
            @Override
            public void loadData(SelectFileEntity entity) {
                mListData.add(entity);
                if (mVideoAdapter != null) {
                    mVideoAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void initAdapter() {

        mVideoAdapter = new SelectVideoAdapter(getActivity(), mListData, mMaxCount, mSelectStyle);
        mRlv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRlv.setAdapter(mVideoAdapter);
        mVideoAdapter.setOnSelectedFileCallback(new OnSelectedFileResultCallback() {
            @Override
            public void selected(List<SelectFileEntity> list, int count) {
                upDataCbv(list, count);
            }
        });

        mVideoAdapter.setOnCamreCallback(new OnCameraCallback() {
            @Override
            public void openCamera() {
                openCapture();
            }
        });

        mVideoAdapter.setOnPreViewCallback(new OnPreViewCallback() {
            @Override
            public void openPreView(int postion) {


                goIntent(postion);
            }
        });
        mVideoAdapter.setOnNotSelectCallback(new OnNotSelectCallback() {
            @Override
            public void notSelect() {
                Toast.makeText(getActivity(), "最多选" + mMaxCount + "张", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goIntent(int postion) {

        EventBus.getDefault().postSticky(new PreviewDataEvent(mListData, mSeleetedData, postion));
        Intent intent = new Intent(getActivity(), FilePreviewActivity.class);
        intent.putExtra("data", mSelectParms);
        startActivity(intent);
    }

    private void openCapture() {
        if (mMediaStoreCompat != null) {
            mMediaStoreCompat.dispatchVideoIntent(getContext(), REQUEST_CODE_CAPTURE);
        }
    }

    private void upDataCbv(List<SelectFileEntity> list, int count) {
        //更新一下底部的按钮

        if (null != mUiCallback) {
            mSeleetedData = list;
            mUiCallback.selected(mListData, list, count, FileType.IMAGE);
        }
    }


    public void setOnUiSelectResultCallback(OnUiSelectResultCallback callback) {
        mUiCallback = callback;
    }

    /**
     * 来自预览所有所选泽的图片（来自这个类FilePreviewActivity--》initView（））
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onSelectedFileEvent(SelectDataEvent event) {

        mListData = event.getList();
        mVideoAdapter.notifyDataSetChanged();

        if (null != mUiCallback) {
            mSeleetedData = event.getmSelectedList();
            mUiCallback.selected(mListData, mSeleetedData, mSeleetedData.size(), FileType.IMAGE);
        }
        EventBus.getDefault().cancelEventDelivery(event);

    }


    /**
     * 数据来自预览所选泽的图片（来自这个类SelectedFilePreviewActivity--》initView（））
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onFilePreviewSlectedEvent(Preview2SelecedDataEvent event) {


        mListData = event.getList();
        mVideoAdapter.notifyDataSetChanged();

        if (null != mUiCallback) {

            mSeleetedData = event.getSelectedList();
            mUiCallback.selected(mListData, mSeleetedData, mSeleetedData.size(), FileType.IMAGE);
        }

        EventBus.getDefault().cancelEventDelivery(event);
    }


    /**
     * 注册EventBus
     */
    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }

    }

    /**
     * 销毁EventBus
     */
    @Override
    public void onDestroy() {


        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }//加上判

        //离开这个页面的时候删除调所有的Event
        EventBus.getDefault().removeAllStickyEvents();

    }

    private void updataIsShowCamra() {
        if (mIsShowCamra) {
            if (!isContainer(mListData)) {
                mListData.add(0, new SelectFileEntity(-1));
            }
        }
    }

    private boolean isContainer(List<SelectFileEntity> list) {

        if (!list.isEmpty()) {
            if (list.get(0).getIdInt() == -1) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAPTURE) {
            Uri contentUri = mMediaStoreCompat.getCurrentPhotoUri();
            String path = mMediaStoreCompat.getCurrentPhotoPath();
            ArrayList<Uri> selected = new ArrayList<>();
            selected.add(contentUri);
            ArrayList<String> selectedPath = new ArrayList<>();
            selectedPath.add(path);

            //不添加这句新拍的照片在相册里里面是找不到的
            new SingleMediaScanner(getActivity(), path, new SingleMediaScanner.ScanListener() {
                @Override
                public void onScanFinish() {
                    Log.i("SingleMediaScanner", "scan finish!");

                }
            });
            getActivity().finish();
        }
    }
}
