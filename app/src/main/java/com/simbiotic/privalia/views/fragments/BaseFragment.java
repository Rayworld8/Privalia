package com.simbiotic.privalia.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import de.greenrobot.event.EventBus;


public class BaseFragment extends Fragment {

    public BaseFragment() {
    }

    public static BaseFragment newInstance() {

        Bundle args = new Bundle();

        BaseFragment fragment = new BaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().registerSticky(this, 10);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    public void onEventMainThread(EventBus event) {

    }
}
