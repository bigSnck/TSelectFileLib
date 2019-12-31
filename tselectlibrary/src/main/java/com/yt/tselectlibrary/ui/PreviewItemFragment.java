package com.yt.tselectlibrary.ui;

import android.os.Bundle;
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
import com.yt.tselectlibrary.ui.event.ClickPreviewImageEvent;

import org.greenrobot.eventbus.EventBus;

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
                Toast.makeText(getActivity(),"过来了1",Toast.LENGTH_SHORT).show();

                EventBus.getDefault().post(new ClickPreviewImageEvent());
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
            Glide.with(getActivity())
                    .load(mSelectFileEntity.getOriginalPath())
                    .into(mImageViewTouch);
        }
    }
}
