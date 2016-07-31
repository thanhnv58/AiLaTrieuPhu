package com.thanhnv.ailatrieuphu;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by thanh on 7/20/2016.
 */
public class IntroducGameFragment extends Fragment{

    private Activity mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        View view = inflater.inflate(R.layout.fragment_introduc_game, container, false);

        TextView txtNamePlayer = (TextView) view.findViewById(R.id.txt_name_player);

//        Bundle bundle = getArguments();
//        String namePlayer = bundle.getString("NAME_PLAYER");
        txtNamePlayer.setText(((GameAct)mContext).getGame().getmNamePlayer());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((GameAct)mContext).showGameQuestionFragment();
            }
        }, 2000);

        return view;
    }

    public void onBackPress() {
        if (mContext != null) {

            mContext.finish();
        }
    }
}
