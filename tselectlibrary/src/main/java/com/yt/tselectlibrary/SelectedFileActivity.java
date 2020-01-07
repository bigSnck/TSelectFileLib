package com.yt.tselectlibrary;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.yt.tselectlibrary.ui.SelectedFilePreviewActivity;
import com.yt.tselectlibrary.ui.SelectedImageFragment;
import com.yt.tselectlibrary.ui.contast.FileType;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;
import com.yt.tselectlibrary.ui.callback.OnUiSelectResultCallback;
import com.yt.tselectlibrary.ui.callback.OnViewClickCallback;
import com.yt.tselectlibrary.ui.contast.SelectParms;
import com.yt.tselectlibrary.ui.contast.SelectedViewType;

import com.yt.tselectlibrary.ui.event.FilePreviewDataEvent;
import com.yt.tselectlibrary.ui.event.ResultEvent;

import com.yt.tselectlibrary.ui.widget.SelectBottomView;
import com.yt.tselectlibrary.ui.SelectedVideoFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class SelectedFileActivity extends FragmentActivity implements EasyPermissions.PermissionCallbacks {

    private SelectBottomView mBottomView;

    private Fragment mFileFragment;

    private List<SelectFileEntity> mSelectFileList;
    private List<SelectFileEntity> mAllList;
    private SelectParms mSelectParms;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selected_file_layout);


        getDataIntent();
        mBottomView = findViewById(R.id.s_file_sbv);

        mBottomView.setOnViewClickCallback(new OnViewClickCallback() {
            @Override
            public void selected(SelectedViewType selectedViewType) {

                if (null != mSelectFileList) {
                    if (selectedViewType == SelectedViewType.PREVIEW_VIEW) {
                        goIntent();
                    }
                    if (selectedViewType == SelectedViewType.SELECTED_VIEW) {
                        //确定选中的图片
                        setSelectedResult();
                    }
                }
            }
        });
        if (mSelectParms.getFileType() == FileType.IMAGE) {
            mBottomView.setLLOraiginalView(View.VISIBLE);
        } else {
            mBottomView.setLLOraiginalView(View.INVISIBLE);

        }

        if (mSelectParms.getFileType() == FileType.IMAGE) {
            mFileFragment = new SelectedImageFragment();
        }
        if (mSelectParms.getFileType() == FileType.VIDEO) {
            mFileFragment = new SelectedVideoFragment();
        }


        Bundle bundle = new Bundle();
        bundle.putParcelable("data", mSelectParms);
        mFileFragment.setArguments(bundle);

        if (mFileFragment instanceof SelectedImageFragment) {
            SelectedImageFragment mImageFragment = (SelectedImageFragment) mFileFragment;
            mImageFragment.setOnUiSelectResultCallback(new OnUiSelectResultCallback() {
                @Override
                public void selected(List<SelectFileEntity> allList, List<SelectFileEntity> selectedFileList, int count, FileType fileType) {
                    mSelectFileList = selectedFileList;
                    mAllList = allList;
                    mBottomView.upDataSelectedFileCount(count);


                }
            });
        }

        if (mFileFragment instanceof SelectedVideoFragment) {
            SelectedVideoFragment mVideoFragment = (SelectedVideoFragment) mFileFragment;
            mVideoFragment.setOnUiSelectResultCallback(new OnUiSelectResultCallback() {
                @Override
                public void selected(List<SelectFileEntity> allList, List<SelectFileEntity> selectedFileList, int count, FileType fileType) {
                    mSelectFileList = selectedFileList;
                    mAllList = allList;
                    mBottomView.upDataSelectedFileCount(count);


                }
            });
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.s_contanier, mFileFragment);


        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(SelectedFileActivity.this, perms)) {
            transaction.commit();
        } else {

            EasyPermissions.requestPermissions(SelectedFileActivity.this, "需要内存权限", 1001, perms);
        }


    }

    private void getDataIntent() {
        Intent intent = getIntent();
        mSelectParms = intent.getParcelableExtra("data");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // 此处表示权限申请已经成功，可以使用该权限完成app的相应的操作了

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // 此处表示权限申请被用户拒绝了，此处可以通过弹框等方式展示申请该权限的原因，以使用户允许使用该权限

        //(可选的)检查用户是否拒绝授权权限，并且点击了“不再询问”（测试如果不点击 不再询问也会调用这个方法，所以只要拒绝就会调用这个方法）
        //下面的语句，展示一个对话框指导用户在应用设置里授权权限

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("申请权限")
                    .setRationale("应用需要这个权限")
                    .build()
                    .show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            // 当用户从应用设置界面返回的时候，可以做一些事情，比如弹出一个土司。
            Toast.makeText(this, "权限设置界面返回", Toast.LENGTH_SHORT).show();
        }

    }


    private void goIntent() {

        EventBus.getDefault().postSticky(new FilePreviewDataEvent(mAllList, mSelectFileList));
        Intent intent = new Intent(this, SelectedFilePreviewActivity.class);
        intent.putExtra("data", mSelectParms);
        startActivity(intent);
    }

    private void setSelectedResult() {

        EventBus.getDefault().postSticky(new ResultEvent(mSelectFileList));
        finish();
    }

}
