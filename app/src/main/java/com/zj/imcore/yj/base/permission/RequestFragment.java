package com.zj.imcore.yj.base.permission;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class RequestFragment extends Fragment {
    private static final String REQUEST_FRAGMENT = "REQUEST_FRAGMENT";


    public static <T extends Fragment> T injectIfNeededIn(Fragment curFragment, Class<T> fragmentClass) {
        FragmentManager manager = getFragmentManager(curFragment);
        return inject(manager, fragmentClass);
    }


    public static <T extends Fragment> T injectIfNeededIn(Context context, Class<T> fragmentClass) {
        FragmentManager manager = getFragmentManager(context);
        return inject(manager, fragmentClass);
    }

    public static <T extends Fragment> T inject(FragmentManager manager, Class<T> fragmentClass) {
        Fragment fragment = manager.findFragmentByTag(fragmentClass.getName());
        if (fragment == null) {
            try {
                fragment = fragmentClass.newInstance();
                manager.beginTransaction()
                        .add(fragment, REQUEST_FRAGMENT)
                        .commitAllowingStateLoss();
                manager.executePendingTransactions();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (T) fragment;
    }

    private static FragmentManager getFragmentManager(Fragment activity) {
        return activity.getChildFragmentManager();
    }

    private static FragmentManager getFragmentManager(Context context) {
        if (context instanceof FragmentActivity) {
            return ((FragmentActivity) context).getSupportFragmentManager();
        }
        throw new IllegalArgumentException("context 不是 FragmentActivity");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

}
