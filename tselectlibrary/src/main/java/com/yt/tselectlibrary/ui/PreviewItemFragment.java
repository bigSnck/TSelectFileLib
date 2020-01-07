package com.yt.tselectlibrary.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.yt.tselectlibrary.R;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;
import com.yt.tselectlibrary.ui.contast.FileType;
import com.yt.tselectlibrary.ui.event.ClickPreviewImageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class PreviewItemFragment extends Fragment {
    private View mView;
    private ImageViewTouch mImageViewTouch;
    private ImageView mIvVideoPaly;
    private static final String ARGS_ITEM = "args_item";
    private SelectFileEntity mSelectFileEntity;

    public static Fragment newInstance(SelectFileEntity entity) {
        PreviewItemFragment fragment = new PreviewItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_ITEM, entity);
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
               String  mOriginalPath=mSelectFileEntity.getOriginalPath();
                if(mOriginalPath.length()>0){
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
        if (mSelectFileEntity != null) {

            mImageViewTouch.setImageBitmap(BitmapFactory.decodeFile(mSelectFileEntity.getOriginalPath()));

        }

       if( mSelectFileEntity.getFileType()== FileType.IMAGE){
           mIvVideoPaly.setVisibility(View.GONE);
       }
        if( mSelectFileEntity.getFileType()== FileType.VIDEO){
            mIvVideoPaly.setVisibility(View.VISIBLE);
        }
    }

   public void palyVideo(String path){
        ///storage/emulated/0/DCIM/Camera/VID_20190816_124851.mp4
       Intent intent = new Intent(Intent.ACTION_VIEW);
       intent.setDataAndType(Uri.fromFile(new File(path)),
               "application/vnd.android.package-archive");
       try {
           startActivity(intent);
       } catch (ActivityNotFoundException e) {
           Toast.makeText(getContext(), "这个视频不支持预览", Toast.LENGTH_SHORT).show();
       }
   }

}
