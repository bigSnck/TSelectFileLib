package com.yt.tselectlibrary.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.yt.tselectlibrary.ui.PreviewItemFragment;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;

import java.util.List;

public class PreviewPagerAdapter extends FragmentPagerAdapter {
    private List<SelectFileEntity> mListData;
    public PreviewPagerAdapter(@NonNull FragmentManager fm, List<SelectFileEntity> list) {
        super(fm);
        mListData=list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return PreviewItemFragment.newInstance(mListData.get(position));
    }

    @Override
    public int getCount() {
        return mListData.size();
    }
}
