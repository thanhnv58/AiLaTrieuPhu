package com.thanhnv.ailatrieuphu;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by thanh on 7/20/2016.
 */
public class GameQuestionFragment extends Fragment {

    private static final int LIST_QUESTION_ID[] = {
            R.id.txt_question_1, R.id.txt_question_2, R.id.txt_question_3,
            R.id.txt_question_4, R.id.txt_question_5, R.id.txt_question_6,
            R.id.txt_question_7, R.id.txt_question_8, R.id.txt_question_9,
            R.id.txt_question_10, R.id.txt_question_11, R.id.txt_question_12,
            R.id.txt_question_13, R.id.txt_question_14, R.id.txt_question_15
    };

    private static final int LIST_SOUND_QUESTION_ID[] = {
            R.raw.ques1, R.raw.ques2, R.raw.ques3,
            R.raw.ques4_b, R.raw.ques5_b, R.raw.ques6,
            R.raw.ques7, R.raw.ques8, R.raw.ques9_b,
            R.raw.ques10, R.raw.ques11, R.raw.ques12,
            R.raw.ques13, R.raw.ques14, R.raw.ques15
    };
    private SoundManager soundManager;
    private Activity mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_game_question, container, false);
        soundManager = new SoundManager((Context) mContext);


        int question = ((GameAct)mContext).getGame().getCurrentQuestion() + 1;
        runQuestionWithKey(view, question);

        return view;
    }

    private void runQuestionWithKey(View view, final int question) {
        switch (question){
            case 0:
                soundIntroductGame();
                return;
            case 1:
                TextView txtQuestion = (TextView) view.findViewById(LIST_QUESTION_ID[question-1]);
                txtQuestion.setBackgroundResource(R.drawable.atp__activity_player_image_money_curent);
                soundManager.playSound(LIST_SOUND_QUESTION_ID[question-1], new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        ((GameAct)mContext).showGamePlayingFragment();
                    }
                });
                return;
            default:
                break;
        }
        TextView txtCurrenQuesion = (TextView) view.findViewById(LIST_QUESTION_ID[question-1]);
        TextView txtPreQuestion = (TextView) view.findViewById(LIST_QUESTION_ID[question-2]);
        txtCurrenQuesion.setBackgroundResource(R.drawable.atp__activity_player_image_money_curent);
        txtPreQuestion.setBackgroundResource(0);
        soundManager.playSound(LIST_SOUND_QUESTION_ID[question-1], new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ((GameAct)mContext).showGamePlayingFragment();
            }
        });
    }

    private void soundIntroductGame(){

        soundManager.playSound(R.raw.luatchoi_b, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                soundReady();
            }
        });
    }

    private void soundReady() {
        soundManager.destroySound();
        soundManager.playSound(R.raw.ready, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ((GameAct)mContext).showGamePlayingFragment();
            }
        });
    }

    public void onBackPress() {
        if (soundManager != null) {
            soundManager.destroySound();
            mContext.finish();
        }
    }
}
