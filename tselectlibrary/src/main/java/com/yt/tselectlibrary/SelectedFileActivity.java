package com.yt.tselectlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import com.yt.tselectlibrary.ui.SelectedVideoFragment;
import com.yt.tselectlibrary.ui.callback.OnResultCallback;
import com.yt.tselectlibrary.ui.contast.FileType;
import com.yt.tselectlibrary.ui.bean.SelectFileEntity;
import com.yt.tselectlibrary.ui.callback.OnUiSelectResultCallback;
import com.yt.tselectlibrary.ui.callback.OnViewClickCallback;
import com.yt.tselectlibrary.ui.contast.OperationType;
import com.yt.tselectlibrary.ui.contast.SelectParms;
import com.yt.tselectlibrary.ui.contast.SelectedViewType;

import com.yt.tselectlibrary.ui.event.FilePreviewDataEvent;


import com.yt.tselectlibrary.ui.helper.CameraHelper;
import com.yt.tselectlibrary.ui.util.RotateBitmapUtils;
import com.yt.tselectlibrary.ui.util.SingleMediaScanner;
import com.yt.tselectlibrary.ui.widget.SelectBottomView;


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
    private static OnResultCallback mOnResultCallback;
    private static final int REQUEST_CODE_TAKE_PHOTO = 10;//拍照
    private static final int REQUEST_CODE_TAKE_VIDEO = 11;//拍视频
    private static CameraHelper mCameraHelper;


    private FragmentTransaction transaction;


    public static void selectFile(Context context, SelectParms selectParms, OnResultCallback callback) {
        mOnResultCallback = callback;
        Intent intent = new Intent(context, SelectedFileActivity.class);
        intent.putExtra("data", selectParms);
        context.startActivity(intent);
    }

    private static CameraHelper getCameraHelper(Context context, SelectParms selectParms) {
        CameraHelper mCameraHelper = new CameraHelper((Activity) context);
        mCameraHelper.setCaptureStrategy(selectParms.getCaptureStrategy());
        return mCameraHelper;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDataIntent();
        setContentView(R.layout.activity_selected_file_layout);

        mCameraHelper = getCameraHelper(this, mSelectParms);
        if (mSelectParms.getOperationType() == OperationType.TakePhoto) {//拍摄照片
            setTakePhoto();
        }
        if (mSelectParms.getOperationType() == OperationType.TakeVideo) {//拍摄视频
            setTakeVideo();
        }
        if (mSelectParms.getOperationType() == OperationType.TakeSelect) {
            setSelected();
        }
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(SelectedFileActivity.this, perms)) {
            if (null != transaction) {

                transaction.commit();
            }
        } else {

            EasyPermissions.requestPermissions(SelectedFileActivity.this, "需要内存权限", 1001, perms);
        }


    }

    /**
     * 设置图片选择
     */
    private void setSelected() {
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
            mFileFragment = new SelectedImageFragment(mCameraHelper);
        }
        if (mSelectParms.getFileType() == FileType.VIDEO) {
            mFileFragment = new SelectedVideoFragment(mCameraHelper);
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

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.s_contanier, mFileFragment);

    }

    /**
     * 设置拍摄视频
     */
    private void setTakeVideo() {

        mCameraHelper.openCameraVideo(REQUEST_CODE_TAKE_VIDEO);
    }

    /**
     * 设置拍照
     */
    private void setTakePhoto() {

        mCameraHelper.openCameraImage(REQUEST_CODE_TAKE_PHOTO);
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
            Toast.makeText(this, "权限设置界面返回", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            setImageResult();

        }
        if (requestCode == REQUEST_CODE_TAKE_VIDEO && resultCode == RESULT_OK) {
            setImageVideo();

        }

    }

    /**
     * 处理返回的视频
     */
    private void setImageVideo() {
        String path = mCameraHelper.getCurrentPhotoPath();
        Uri uri = mCameraHelper.getCurrentPhotoUri();
        Log.i("AA","视频路径"+uri);

        SelectFileEntity entity = new SelectFileEntity(true, path, path, FileType.VIDEO);
        mOnResultCallback.successSingleResult(entity);
        //不添加这句新拍的照片在相册里里面是找不到的,相当重要的

        SingleMediaScanner videoScanner = new SingleMediaScanner(this, new SingleMediaScanner.ScanListener() {
            @Override
            public void onScanFinish() {
                finish();
            }
        });
        videoScanner.insertIntoMediaStore(true, path);
    }

    /**
     * 处理返回的图片
     */
    private void setImageResult() {
        String path = mCameraHelper.getCurrentPhotoPath();
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        RotateBitmapUtils rotateBitmapUtils = new RotateBitmapUtils();//解决部分手机图片旋转问题
        int degree = rotateBitmapUtils.getBitmapDegree(path);
        if (degree != 0) {
            bitmap = rotateBitmapUtils.rotateBitmapByDegree(bitmap, degree);
        }

        SelectFileEntity entity = new SelectFileEntity(true, path, path, FileType.IMAGE);
        mOnResultCallback.successSingleResult(entity);
        //刷新图片在手机数据库里面的信息，才能在相册里面查找到
        SingleMediaScanner imageScanner = new SingleMediaScanner(this, new SingleMediaScanner.ScanListener() {
            @Override
            public void onScanFinish() {

                finish();
            }
        });
        imageScanner.insertIntoImageStore(bitmap);
    }


    private void goIntent() {

        EventBus.getDefault().postSticky(new FilePreviewDataEvent(mAllList, mSelectFileList));
        Intent intent = new Intent(this, SelectedFilePreviewActivity.class);
        intent.putExtra("data", mSelectParms);
        startActivity(intent);
    }

    private void setSelectedResult() {
        mOnResultCallback.successResult(mSelectFileList);
        finish();
    }

}
