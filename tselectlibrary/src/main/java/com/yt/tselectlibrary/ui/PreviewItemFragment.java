package com.yt.tselectlibrary.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.yt.tselectlibrary.R;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;
import com.yt.tselectlibrary.ui.contast.FileType;
import com.yt.tselectlibrary.ui.contast.SelectParms;
import com.yt.tselectlibrary.ui.event.ClickPreviewImageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Locale;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class PreviewItemFragment extends Fragment {
    private View mView;
    private ImageViewTouch mImageViewTouch;
    private ImageView mIvVideoPaly;
    private static final String ARGS_ITEM = "args_item";
    private static final String ARGS_PARAM = "args_param";

    private SelectFileEntity mSelectFileEntity;
    private Uri mVideoUri;
    private SelectParms mSelectParms;


    public static Fragment newInstance(SelectFileEntity entity, SelectParms selectParms) {
        PreviewItemFragment fragment = new PreviewItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_ITEM, entity);
        bundle.putParcelable(ARGS_PARAM, selectParms);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_preview_item_layout, container, false);

        mImageViewTouch = mView.findViewById(R.id.fragment_ivt_view);

        mIvVideoPaly = mView.findViewById(R.id.fragment_iv_video_play);

        mImageViewTouch.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
            @Override
            public void onSingleTapConfirmed() {
                EventBus.getDefault().post(new ClickPreviewImageEvent());
            }
        });

        mIvVideoPaly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mOriginalPath = mSelectFileEntity.getOriginalPath();
                if (mOriginalPath.length() > 0) {
                    palyVideo(mOriginalPath);
                }

            }
        });

        mIvVideoPaly.setVisibility(View.GONE);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSelectFileEntity = getArguments().getParcelable(ARGS_ITEM);
        mSelectParms = getArguments().getParcelable(ARGS_PARAM);
        if (mSelectFileEntity != null) {
            Glide.with(this)
                    .load(mSelectFileEntity.getOriginalPath())

                    .into(mImageViewTouch);
        }

        if (mSelectFileEntity.getFileType() == FileType.IMAGE) {
            mIvVideoPaly.setVisibility(View.GONE);
        }
        if (mSelectFileEntity.getFileType() == FileType.VIDEO) {
            mIvVideoPaly.setVisibility(View.VISIBLE);
        }
    }

    public void palyVideo(String path) {
        ///storage/emulated/0/DCIM/Camera/VID_20190816_124851.mp4
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24) {

            mVideoUri = FileProvider.getUriForFile(getActivity(), mSelectParms.getCaptureStrategy().authority, new File(path));
            Log.i("AA", "路径=" + mVideoUri.toString()+"::"+mVideoUri.getPath());
        } else {
            mVideoUri = Uri.fromFile(new File(path));
        }

        intent.setDataAndType(mVideoUri,
                "video/*");

        startActivity(intent);


    }

}
