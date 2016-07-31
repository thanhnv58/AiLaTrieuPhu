package com.thanhnv.ailatrieuphu;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

/**
 * Created by thanh on 7/18/2016.
 */
public class MainMenuFragment extends Fragment implements View.OnClickListener, Animation.AnimationListener {

    private static final int TRIAL_PLAY_PRESS = 0, ZALO_PRESS = 1;
    private LinearLayout rlMainMenu;
    private ImageView imgButtonTrialPlay;
    private TextView txtBtnHighScore;
    private Animation animMainMenuAppear, animDelayToPress;
    private int buttonPress = -1, destroyBGSound = -2;
    private SoundManager soundManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Activity mActivity = getActivity();
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        findingViewsId(view);
        initData(mActivity);
        setEven();

        soundManager = new SoundManager(mActivity);
        soundManager.playSoundBG(R.raw.bgmusic);



        return view;
    }

    private void setEven() {
        rlMainMenu.startAnimation(animMainMenuAppear);

        txtBtnHighScore.setOnClickListener(this);
        imgButtonTrialPlay.setOnClickListener(this);

        animDelayToPress.setAnimationListener(this);
    }

    private void initData(Context context) {
        animMainMenuAppear = AnimationUtils.loadAnimation(context, R.anim.anim_main_menu_appear);
        animDelayToPress = AnimationUtils.loadAnimation(context, R.anim.anim_delay_to_press);
    }

    private void findingViewsId(View view) {
        rlMainMenu = (LinearLayout) view.findViewById(R.id.rl_main_menu);
        imgButtonTrialPlay = (ImageView) view.findViewById(R.id.img_button_trial_play);
        txtBtnHighScore = (TextView) view.findViewById(R.id.txt_btn_high_score);
    }

    @Override
    public void onClick(View v) {

        if(destroyBGSound == -1) {
            soundManager.destroyBG();
            destroyBGSound = 1;
        }
        switch (v.getId()){
            case R.id.img_button_trial_play:
                buttonPress = TRIAL_PLAY_PRESS;
                imgButtonTrialPlay.setImageResource(R.drawable.atp__button_trial_press);
                imgButtonTrialPlay.startAnimation(animDelayToPress);
                break;
            case R.id.txt_btn_high_score:
                showDialogHighScore();
                break;
            default:
                break;
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Activity mContext = getActivity();
        switch (buttonPress){
            case TRIAL_PLAY_PRESS:
                imgButtonTrialPlay.setImageResource(R.drawable.atp__button_trial_normal);
                Intent intent = new Intent();
                intent.setClass(mContext, GameAct.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("NAME_PLAYER", ((MainActivity)mContext).getNamePlayer());
//                intent.putExtras(bundle);
                getActivity().startActivity(intent);

                break;
            default:
                break;
        }
    }

    private void showDialogHighScore() {
        Dialog dialog = new Dialog(getActivity(), R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_high_score);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtName = (TextView) dialog.findViewById(R.id.txt_user_high_score);
        TextView txtLevel = (TextView) dialog.findViewById(R.id.txt_level_high_score);
        TextView txtMoney = (TextView) dialog.findViewById(R.id.txt_money_high_score);

        String highScore[] = ((MainActivity)getActivity()).getHighScore();

        txtName.setText(highScore[0]);
        txtLevel.setText("Level " + highScore[1]);
        txtMoney.setText(highScore[2]);
        dialog.show();

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void onBackPress() {
        if (destroyBGSound == -1) {
            soundManager.destroyBG();
            destroyBGSound = 1;
        }
    }

    public void setDestroy() {
        destroyBGSound = -1;
    }
}
