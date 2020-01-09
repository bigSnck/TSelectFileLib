package com.yt.tselectlibrary.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.yt.tselectlibrary.ui.PreviewItemFragment;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;
import com.yt.tselectlibrary.ui.contast.SelectParms;

import java.util.List;

public class PreviewPagerAdapter extends FragmentPagerAdapter {
    private List<SelectFileEntity> mListData;
    private SelectParms mSelectParms;
    public PreviewPagerAdapter(@NonNull FragmentManager fm, List<SelectFileEntity> list,SelectParms selectParms) {
        super(fm);
        mListData=list;
        mSelectParms=selectParms;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return PreviewItemFragment.newInstance(mListData.get(position),mSelectParms);
    }

    @Override
    public int getCount() {
        return mListData.size();
    }



}
