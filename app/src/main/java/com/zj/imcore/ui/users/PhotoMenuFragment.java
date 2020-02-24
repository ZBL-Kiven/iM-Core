package com.zj.imcore.ui.users;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.zj.album.AlbumIns;
import com.zj.album.options.AlbumOptions;
import com.zj.imcore.R;
import com.zj.imcore.yj.base.permission.PermissionListener;
import com.zj.imcore.yj.base.permission.PermissionManager;
import com.zj.imcore.yj.base.permission.ResponsePermission;

import java.io.File;
import java.util.List;


/**
 * 头像选择器
 *
 * @author yangji
 */
public class PhotoMenuFragment extends DialogFragment implements View.OnClickListener {

    private static final int CAMERA_CODE = 0x1000;

    public interface PhotoMenuListener {
        void exec(String file);
    }

    private PhotoMenuListener cameraListener;

    private View rootView;
    private TextView btnCamera, btnAlbum;


    public PhotoMenuFragment setListener(PhotoMenuListener listener) {
        this.cameraListener = listener;
        return this;
    }

    protected <T extends View> T findViewById(@IdRes int resId) {
        return rootView.findViewById(resId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null) getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogAnim;
        rootView = inflater.inflate(R.layout.app_fragment_phtot_menu, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog == null) {
            return;
        }
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        // 一定要设置Background，如果不设置，window属性设置无效
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
    }

    protected void initListener() {
        btnCamera.setOnClickListener(this);
        btnAlbum.setOnClickListener(this);

        findViewById(R.id.app_fragment_photo_menu_btn_cancel).setOnClickListener(v -> {
            dismissAllowingStateLoss();
        });
    }

    protected void initView() {
        btnCamera = findViewById(R.id.app_fragment_photo_menu_btn_camera);
        btnAlbum = findViewById(R.id.app_fragment_photo_menu_btn_album);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_fragment_photo_menu_btn_camera:
                requestCameraPermission();
                break;
            case R.id.app_fragment_photo_menu_btn_album:
                requestAlbumPermission();
                break;
            default:
        }
    }

    private void requestAlbumPermission() {
        new PermissionManager(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .exec(new PermissionListener() {
                    @Override
                    public void onSuccess() {
                        openAlbum();
                    }

                    @Override
                    public void failed(List<ResponsePermission> permission) {
                        Toast.makeText(getContext(), "权限获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openAlbum() {
        AlbumIns.INSTANCE.with(this)
                .mimeTypes(AlbumOptions.ofImage())
                .maxSelectedCount(1)
                .imgSizeRange(1024, Long.MAX_VALUE)
                .start((isSelected, fileInfos) -> {
                    if (isSelected && fileInfos != null && fileInfos.size() > 0) {
                        execListener(fileInfos.get(0).getPath());
                    }
                    return null;
                });
    }

    private void requestCameraPermission() {
        new PermissionManager(this)
                .request(Manifest.permission.CAMERA)
                .exec(new PermissionListener() {
                    @Override
                    public void onSuccess() {
                        openCamera();
                    }

                    @Override
                    public void failed(List<ResponsePermission> permission) {
                        Toast.makeText(getContext(), "权限获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openCamera() {
        Uri uri = getCameraUri(getCameraFile(getContext()));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK != resultCode) {
            return;
        }
        if (requestCode == CAMERA_CODE) {
            execCamera();
        }
    }

    private void execCamera() {
        File file = getCameraFile(getContext());
        execListener(file.getAbsolutePath());
    }

    private void execListener(String path) {
        if (cameraListener != null)
            cameraListener.exec(path);
        dismissAllowingStateLoss();

    }

    private Uri getCameraUri(File file) {
        Context context = getActivity();
        if (context == null) {
            return null;
        }
        String packageName = context.getPackageName();
        return FileProvider.getUriForFile(context, packageName + ".provider", file);
    }

    private File getCameraFile(Context context) {
        return new File(context.getCacheDir(), "PhotoMenuFragment.jpg");
    }
}
