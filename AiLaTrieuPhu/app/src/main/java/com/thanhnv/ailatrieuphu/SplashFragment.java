package com.thanhnv.ailatrieuphu;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

/**
 * Created by thanh on 7/18/2016.
 */
public class SplashFragment extends Fragment {

    private boolean isRunning = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        LinearLayout llSplash = (LinearLayout)view.findViewById(R.id.ll_splash);
        Animation animSplashAppear = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_splah_appear);
        llSplash.startAnimation(animSplashAppear);

        startTimeToShow();
        return view;
    }

    private void startTimeToShow(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isRunning = false;
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).showMainFragment();
                }
            }
        }, 3000);
    }

    public boolean isRunning() {
        return isRunning;
    }
}
