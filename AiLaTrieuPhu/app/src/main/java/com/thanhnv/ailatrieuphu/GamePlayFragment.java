package com.thanhnv.ailatrieuphu;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

/**
 * Created by thanh on 7/20/2016.
 */
public class GamePlayFragment extends Fragment implements View.OnClickListener, Animation.AnimationListener {

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

    private Activity mContext;
    private Dialog dialog;
    private TextView txtScore, txtTime, txtNamePlayer, txtQuestionContent, txtQuestionName;
    private TextView txtCaseA, txtCaseB, txtCaseC, txtCaseD;
    private TableRow caseA, caseB, caseC, caseD;
    private ProgressBar progressTimeDown;
    private SoundManager soundManager;
    private Game game;
    private View view;
    private Animation animationSelect, animDelayPress;
    private ImageView imgStop, imgChangeQuestion, img5050, imgAudients, imgCallYourFriend;
    private int selectionAns = -1;
    private OnListenSaveHighScore saveHighScore;

    public void setOnListenSaveHighScore(OnListenSaveHighScore event){
        saveHighScore = event;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mContext == null) {
            mContext = getActivity();
        }

        view = inflater.inflate(R.layout.fragment_game_playing, container, false);
        animationSelect = AnimationUtils.loadAnimation(mContext, R.anim.anim_select_answer);
        animDelayPress = AnimationUtils.loadAnimation(mContext, R.anim.anim_delay_to_press);
        game = ((GameAct)mContext).getGame();
        soundManager = new SoundManager(mContext);
        animDelayPress.setAnimationListener(this);

        progressTimeDown = (ProgressBar) view.findViewById(R.id.progress_time_down);
        txtScore = (TextView) view.findViewById(R.id.txt_money);
        txtTime = (TextView) view.findViewById(R.id.txt_time_down);
        txtQuestionContent = (TextView) view.findViewById(R.id.txt_question_content);
        txtQuestionName = (TextView) view.findViewById(R.id.txt_question_name);
        txtNamePlayer = (TextView) view.findViewById(R.id.txt_name_player_playing);

        caseA = (TableRow) view.findViewById(R.id.tr_case_a);
        caseB = (TableRow) view.findViewById(R.id.tr_case_b);
        caseC = (TableRow) view.findViewById(R.id.tr_case_c);
        caseD = (TableRow) view.findViewById(R.id.tr_case_d);

        txtCaseA = (TextView) view.findViewById(R.id.txt_case_a);
        txtCaseB = (TextView) view.findViewById(R.id.txt_case_b);
        txtCaseC = (TextView) view.findViewById(R.id.txt_case_c);
        txtCaseD = (TextView) view.findViewById(R.id.txt_case_d);

        imgStop = (ImageView) view.findViewById(R.id.img_stop_game);
        img5050 = (ImageView) view.findViewById(R.id.img_50_50);
        imgChangeQuestion = (ImageView) view.findViewById(R.id.img_change_question);
        imgCallYourFriend = (ImageView) view.findViewById(R.id.img_call_your_friend);
        imgAudients = (ImageView) view.findViewById(R.id.img_audients);

        caseA.setOnClickListener(this);
        caseB.setOnClickListener(this);
        caseC.setOnClickListener(this);
        caseD.setOnClickListener(this);

        img5050.setOnClickListener(this);
        imgStop.setOnClickListener(this);
        imgCallYourFriend.setOnClickListener(this);
        imgAudients.setOnClickListener(this);
        imgChangeQuestion.setOnClickListener(this);

        showPlayingWithQuestion();

        return view;
    }

    private void showPlayingWithQuestion() {
//        game.setSelectAnswer(-1);
        int indexQuestion = ((GameAct)mContext).getGame().getCurrentQuestion() + 1;
        switch (indexQuestion){
            case 0:
                if (getActivity() != null) {
                    showDialogReady();
                }
                return;
            default:
                break;
        }

        // system
        txtNamePlayer.setText(game.getmNamePlayer());
        txtScore.setText(game.getScore()+"");
        txtTime.setText("30");

        // question
        Question question = game.getQuestion();

        txtQuestionName.setText("Câu hỏi số " + question.getLevel());
        txtQuestionContent.setText(question.getQuestion());
        txtCaseA.setText(question.getCaseA());
        txtCaseB.setText(question.getCaseB());
        txtCaseC.setText(question.getCaseC());
        txtCaseD.setText(question.getCaseD());

        caseA.setEnabled(true);
        caseB.setEnabled(true);
        caseC.setEnabled(true);
        caseD.setEnabled(true);


        switch (selectionAns){
            case 1:
                caseA.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_normal);
                break;
            case 2:
                caseB.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_normal);
                break;
            case 3:
                caseC.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_normal);
                break;
            case 4:
                caseD.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_normal);
                break;
            default:
                break;
        }

        // time
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute(30);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play_again:
                soundManager.destroySound();
                mContext.finish();
                break;
            case R.id.btn_quit:

                dialog.dismiss();
                soundManager.destroySound();
                mContext.finish();
                break;
            case R.id.btn_bo_qua:
                backToMainAct();
                break;
            case R.id.btn_san_sang:
                beginPlayGame();
                break;
            case R.id.tr_case_a:
                if(game.getSelectionCase() > 0){
                    return;
                }
                selectionAns = 1;
                game.setSelectAnswer(1);
                soundManager.playSound(R.raw.ans_a, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (getActivity() != null) {
                            showAnswer();
                        }
                    }
                });
                caseA.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_selected);
                caseA.startAnimation(animationSelect);
                break;
            case R.id.tr_case_b:
                if(game.getSelectionCase() > 0){
                    return;
                }
                selectionAns = 2;
                game.setSelectAnswer(2);
                soundManager.playSound(R.raw.ans_b, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (getActivity() != null) {
                            showAnswer();
                        }
                    }
                });
                caseB.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_selected);
                caseB.startAnimation(animationSelect);
                break;
            case R.id.tr_case_c:
                if(game.getSelectionCase() > 0){
                    return;
                }
                selectionAns = 3;
                game.setSelectAnswer(3);
                soundManager.playSound(R.raw.ans_c, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (getActivity() != null) {
                            showAnswer();
                        }
                    }
                });
                caseC.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_selected);
                caseC.startAnimation(animationSelect);
                break;
            case R.id.tr_case_d:
                if(game.getSelectionCase() > 0){
                    return;
                }
                selectionAns = 4;
                game.setSelectAnswer(4);
                soundManager.playSound(R.raw.ans_d, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (getActivity() != null) {
                            showAnswer();
                        }
                    }
                });
                caseD.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_selected);
                caseD.startAnimation(animationSelect);
                break;
            case R.id.img_stop_game:
                if (getActivity() != null) {
                    showDialogStopGame();
                }
                break;
            case R.id.img_50_50:
                if (game.getCurrentQuestion() < 0 || game.getSelectHelp() == 5 || game.getSelectionCase() > 0){
                    return;
                }
                game.setSelectHelp(Game.HELP_50_50);
                select5050();
                break;
            case R.id.img_change_question:
                if (game.getCurrentQuestion() < 0 || game.getSelectHelp() == 5 || game.getSelectionCase() > 0){
                    return;
                }
                game.setSelectHelp(Game.HELP_CHANGE_QUES);
                selectChangeQuestion();
                break;
            case R.id.img_audients:
                if (game.getCurrentQuestion() < 0 || game.getSelectHelp() == 5 || game.getSelectionCase() > 0){
                    return;
                }
                game.setSelectHelp(Game.HELP_AUDIENTS);
                selectAudients();
                break;
            case R.id.img_call_your_friend:
                if (game.getCurrentQuestion() < 0 || game.getSelectHelp() == 5 || game.getSelectionCase() > 0){
                    return;
                }
                game.setSelectHelp(Game.HELP_CALL_FRIEND);
                selectCallFriend();
                break;
            default:
                break;
        }
    }

    private void selectCallFriend() {
        imgCallYourFriend.setImageResource(R.drawable.atp__activity_player_button_image_help_call_active);
        imgCallYourFriend.startAnimation(animDelayPress);

        soundManager.playSound(R.raw.help_call, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (getActivity() != null) {
                    showDialogCallFriend();
                }
            }
        });

    }



    private void selectAudients() {
        imgAudients.setImageResource(R.drawable.atp__activity_player_button_image_help_audience_active);
        imgAudients.startAnimation(animDelayPress);

        soundManager.playSound(R.raw.khan_gia, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Animation animPercent = AnimationUtils.loadAnimation(mContext, R.anim.anim_dialog_audience_percent_appear);
                dialog = new Dialog(mContext, R.style.AudienceDialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_audience);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button btnThankEveryOne = (Button) dialog.findViewById(R.id.btn_audience_thank_every_one);
                TextView txtCaseAPercent = (TextView) dialog.findViewById(R.id.txt_case_a_percent);
                TextView txtCaseBPercent = (TextView) dialog.findViewById(R.id.txt_case_b_percent);
                TextView txtCaseCPercent = (TextView) dialog.findViewById(R.id.txt_case_c_percent);
                TextView txtCaseDPercent = (TextView) dialog.findViewById(R.id.txt_case_d_percent);

                TextView txtCaseA = (TextView) dialog.findViewById(R.id.txt_audience_case_a);
                TextView txtCaseB = (TextView) dialog.findViewById(R.id.txt_audience_case_b);
                TextView txtCaseC = (TextView) dialog.findViewById(R.id.txt_audience_case_c);
                TextView txtCaseD = (TextView) dialog.findViewById(R.id.txt_audience_case_d);

                int audience[] = game.getAudiencePercent();

                TableRow.LayoutParams Params1 = new TableRow.LayoutParams(audience[0] * 5, 24);
                txtCaseA.setLayoutParams(Params1);
                txtCaseAPercent.setText(audience[0] + "%");
                TableRow.LayoutParams Params2 = new TableRow.LayoutParams(audience[1] * 5, 24);
                txtCaseB.setLayoutParams(Params2);
                txtCaseBPercent.setText(audience[1] + "%");
                TableRow.LayoutParams Params3 = new TableRow.LayoutParams(audience[2] * 5, 24);
                txtCaseC.setLayoutParams(Params3);
                txtCaseCPercent.setText(audience[2] + "%");
                TableRow.LayoutParams Params4 = new TableRow.LayoutParams(audience[3] * 5, 24);
                txtCaseD.setLayoutParams(Params4);
                txtCaseDPercent.setText(audience[3] + "%");

                btnThankEveryOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        game.setSelectAnswer(-1);
                        MyAsyncTask myAsyncTask = new MyAsyncTask();
                        myAsyncTask.execute(22);
                    }
                });

                txtCaseA.startAnimation(animPercent);
                txtCaseB.startAnimation(animPercent);
                txtCaseC.startAnimation(animPercent);
                txtCaseD.startAnimation(animPercent);

                dialog.show();
            }
        });
    }

    private void selectChangeQuestion() {
        imgChangeQuestion.setImageResource(R.drawable.atp__activity_player_button_image_help_change_question_active);
        imgChangeQuestion.startAnimation(animDelayPress);

        ((GameAct)mContext).changeQuestion(game.getCurrentQuestion() + 1);
//        Question newQuestion = game.getChangeQuestion();

//        game.setSelectAnswer(-1);
//
//        MyAsyncTask myAsyncTask = new MyAsyncTask();
//        myAsyncTask.execute(30);

        soundManager.playSound(R.raw.touch_sound, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                game.setSelectAnswer(-1);
                if (getActivity() != null) {
                    showPlayingWithQuestion();
                }
            }
        });


//        txtQuestionContent.setText(newQuestion.getQuestion());
//        txtQuestionName.setText("Câu hỏi số " + newQuestion.getLevel());
//
//        txtCaseA.setText(newQuestion.getCaseA());
//        txtCaseB.setText(newQuestion.getCaseB());
//        txtCaseC.setText(newQuestion.getCaseC());
//        txtCaseD.setText(newQuestion.getCaseD());
//        caseA.setEnabled(true);
//        caseB.setEnabled(true);
//        caseC.setEnabled(true);
//        caseD.setEnabled(true);


    }

    private void select5050() {
        soundManager.playSound(R.raw.sound5050, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                int answerId[] = game.select5050();
                for (int i = 0; i < answerId.length; i++){
                    switch (answerId[i]){
                        case 0:
                            txtCaseA.setText("");
                            caseA.setEnabled(false);
                            break;
                        case 1:
                            txtCaseB.setText("");
                            caseB.setEnabled(false);
                            break;
                        case 2:
                            txtCaseC.setText("");
                            caseC.setEnabled(false);
                            break;
                        case 3:
                            txtCaseD.setText("");
                            caseD.setEnabled(false);
                            break;
                        default:
                            break;
                    }
                }
                game.setSelectAnswer(-1);
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(20);
            }
        });

        img5050.setImageResource(R.drawable.atp__activity_player_button_image_help_5050_active);
        img5050.startAnimation(animDelayPress);

    }


    private void showAnswer() {
        soundManager.playSound(R.raw.ans_now1, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (game.checkAnswer()){
                    final int trueCase = game.getQuestion().getTrueCase();
                    switch (trueCase){
                        case 1:
                            caseA.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_true);
                            caseA.startAnimation(animationSelect);
                            soundManager.playSound(R.raw.true_a, new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    game.createNewQuestion();
                                    if (getActivity() != null) {
                                        showDialogListQuestion();
                                    }
                                }
                            });

                            break;
                        case 2:
                            caseB.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_true);
                            caseB.startAnimation(animationSelect);
                            soundManager.playSound(R.raw.true_b, new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    game.createNewQuestion();
                                    if (getActivity() != null) {
                                        showDialogListQuestion();
                                    }
                                }
                            });

                            break;
                        case 3:
                            caseC.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_true);
                            caseC.startAnimation(animationSelect);
                            soundManager.playSound(R.raw.true_c, new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    game.createNewQuestion();
                                    if (getActivity() != null) {
                                        showDialogListQuestion();
                                    }
                                }
                            });

                            break;
                        case 4:
                            caseD.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_true);
                            caseD.startAnimation(animationSelect);
                            soundManager.playSound(R.raw.true_d2, new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    game.createNewQuestion();
                                    if (getActivity() != null) {
                                        showDialogListQuestion();
                                    }
                                }
                            });

                            break;
                        default:
                            break;
                    }

                } else {
                    switch (game.getSelectionCase()){
                        case 1:
                            caseA.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_wrong);
                            caseA.startAnimation(animationSelect);
                            break;
                        case 2:
                            caseB.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_wrong);
                            caseB.startAnimation(animationSelect);
                            break;
                        case 3:
                            caseC.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_wrong);
                            caseC.startAnimation(animationSelect);
                            break;
                        case 4:
                            caseD.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_wrong);
                            caseD.startAnimation(animationSelect);
                            break;
                        default:
                            break;
                    }
                    final int trueCase = game.getQuestion().getTrueCase();
                    switch (trueCase){
                        case 1:
                            caseA.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_true);
                            caseA.startAnimation(animationSelect);
                            soundManager.playSound(R.raw.lose_a, new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    if (getActivity() != null) {
                                        showDialogGameLosing(game.getCurrentQuestion());
                                    }
                                }
                            });

                            break;
                        case 2:
                            caseB.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_true);
                            caseB.startAnimation(animationSelect);
                            soundManager.playSound(R.raw.lose_b, new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    if (getActivity() != null) {
                                        showDialogGameLosing(game.getCurrentQuestion());
                                    }
                                }
                            });

                            break;
                        case 3:
                            caseC.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_true);
                            caseC.startAnimation(animationSelect);
                            soundManager.playSound(R.raw.lose_c, new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    if (getActivity() != null) {
                                        showDialogGameLosing(game.getCurrentQuestion());
                                    }
                                }
                            });

                            break;
                        case 4:
                            caseD.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_true);
                            caseD.startAnimation(animationSelect);
                            soundManager.playSound(R.raw.lose_d2, new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    if (getActivity() != null) {
                                        showDialogGameLosing(game.getCurrentQuestion());
                                    }
                                }
                            });

                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    private void beginPlayGame() {
        dialog.dismiss();
        soundManager.playSound(R.raw.gofind, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                game.createNewQuestion();
                if (getActivity() != null) {
                    showDialogListQuestion();
                }
            }
        });
    }

    private void backToMainAct() {
        soundManager.destroySound();
        mContext.finish();
    }

    private void showDialogTimeOut() {
        game.setSelectHelp(5);
        game.setSelectAnswer(1);

        Dialog dialog = new Dialog(mContext, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_game_lose);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnPlayAgain = (Button) dialog.findViewById(R.id.btn_play_again);
        final Button btnQuit = (Button) dialog.findViewById(R.id.btn_quit);
        TextView txtQuestionLose = (TextView) dialog.findViewById(R.id.txt_game_lose_result);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txt_dialog_title);
        TextView txtContent = (TextView) dialog.findViewById(R.id.txt_dialog_content);

        txtContent.setText("Rất tiếc bạn đã hết thời gian suy nghĩ :(");
        txtTitle.setText("HẾT THỜI GIAN");
        final int numberQues = game.getCurrentQuestion();
        txtQuestionLose.setText("Bạn đã vượt qua " + numberQues + " câu hỏi của chương trình");

        btnPlayAgain.setVisibility(View.INVISIBLE);
        btnQuit.setVisibility(View.INVISIBLE);
        soundManager.playSound(R.raw.out_of_time, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                soundManager.playSound(R.raw.lose, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        btnPlayAgain.setVisibility(View.VISIBLE);
                        btnQuit.setVisibility(View.VISIBLE);
                        showDialogSaveHighScore(numberQues);
                    }
                });
            }
        });

        btnPlayAgain.setOnClickListener(this);
        btnQuit.setOnClickListener(this);

        dialog.show();
    }

    private void showDialogStopGame() {
        final Dialog dialog = new Dialog(mContext, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_stop_game);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnBoQua = (Button) dialog.findViewById(R.id.btn_bo_qua_stop);
        Button btnU = (Button) dialog.findViewById(R.id.btn_u);

        btnBoQua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.setSelectHelp(0);
                dialog.dismiss();
                soundManager.destroySound();
                mContext.finish();
            }
        });

        dialog.show();
    }

    private void showDialogCallFriend() {
        final Dialog dialog = new Dialog(mContext, R.style.AudienceDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_call_friend);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TableRow trCallFriend1 = (TableRow) dialog.findViewById(R.id.tr_call_friend_1);
        final TableRow trCallFriend2 = (TableRow) dialog.findViewById(R.id.tr_call_friend_2);
        final LinearLayout llCallFriend3 = (LinearLayout) dialog.findViewById(R.id.ll_affter_call);

        final Button btnThankFriend = (Button) dialog.findViewById(R.id.btn_call_friend_thanks);
        final TextView txtSuggest = (TextView) dialog.findViewById(R.id.txt_call_friend_suggest);

        final TextView txtNurser = (TextView) dialog.findViewById(R.id.txt_call_friend_nurser);
        final TextView txtStudent = (TextView) dialog.findViewById(R.id.txt_call_friend_student);
        final TextView txtEngineer = (TextView) dialog.findViewById(R.id.txt_call_friend_engineer);
        final TextView txtStaff = (TextView) dialog.findViewById(R.id.txt_call_friend_staff);
        final TextView txtSelected = (TextView) dialog.findViewById(R.id.txt_call_friend_selected);

        View.OnClickListener event = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.txt_call_friend_nurser:
                        txtSelected.setBackgroundResource(R.drawable.btn_call_friend_click_1);
                        break;
                    case R.id.txt_call_friend_student:
                        txtSelected.setBackgroundResource(R.drawable.btn_call_friend_click_2);
                        break;
                    case R.id.txt_call_friend_engineer:
                        txtSelected.setBackgroundResource(R.drawable.btn_call_friend_click_3);
                        break;
                    case R.id.txt_call_friend_staff:
                        txtSelected.setBackgroundResource(R.drawable.btn_call_friend_click_4);
                        break;
                    default:
                        break;
                }
                txtEngineer.setEnabled(false);
                txtNurser.setEnabled(false);
                txtStudent.setEnabled(false);
                txtStaff.setEnabled(false);

                soundManager.playSound(R.raw.call, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        trCallFriend1.setVisibility(View.GONE);
                        trCallFriend2.setVisibility(View.GONE);
                        Animation animTxtSuggest = AnimationUtils.loadAnimation(mContext, R.anim.anim_dialog_call_friend_text_suggest);
                        llCallFriend3.setVisibility(View.VISIBLE);
                        btnThankFriend.setVisibility(View.INVISIBLE);
                        txtSuggest.startAnimation(animTxtSuggest);
                        soundManager.playSound(R.raw.help_callb, new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                String anser = "";
                                switch (game.getAnserSuggest()){
                                    case 1:
                                        anser = "A";
                                        break;
                                    case 2:
                                        anser = "B";
                                        break;
                                    case 3:
                                        anser = "C";
                                        break;
                                    case 4:
                                        anser = "D";
                                        break;
                                    default:
                                        anser = "C";
                                        break;
                                }
                                txtSuggest.clearAnimation();
                                txtSuggest.setText("Câu trả lời hỗ trợ bạn là " + anser);
                                btnThankFriend.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });


            }
        };

        txtNurser.setOnClickListener(event);
        txtStudent.setOnClickListener(event);
        txtEngineer.setOnClickListener(event);
        txtStaff.setOnClickListener(event);




        btnThankFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                game.setSelectAnswer(-1);
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(22);
            }
        });

        dialog.show();
    }

    private void showDialogGameLosing(final int numberQues) {
        Dialog dialog = new Dialog(mContext, R.style.ExitGame);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_game_lose);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnPlayAgain = (Button) dialog.findViewById(R.id.btn_play_again);
        final Button btnQuit = (Button) dialog.findViewById(R.id.btn_quit);
        TextView txtQuestionLose = (TextView) dialog.findViewById(R.id.txt_game_lose_result);

        btnPlayAgain.setVisibility(View.GONE);
        btnQuit.setVisibility(View.GONE);

        txtQuestionLose.setText("Bạn đã vượt qua " + numberQues + " câu hỏi của chương trình");
        btnPlayAgain.setOnClickListener(this);
        btnQuit.setOnClickListener(this);

        soundManager.playSound(R.raw.lose, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                showDialogSaveHighScore(numberQues);
                btnPlayAgain.setVisibility(View.VISIBLE);
                btnQuit.setVisibility(View.VISIBLE);
            }
        });

        dialog.show();
    }

    private void showDialogListQuestion() {
        int numberQuestion = game.getCurrentQuestion();
        if ( numberQuestion > 14 ){
            if (getActivity() != null) {
                showDialogChampion();
                return;
            }
        }


        final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_game_question);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setWindowAnimations(R.style.FullScreenDialogAnimation);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtQues = (TextView) dialog.findViewById(LIST_QUESTION_ID[numberQuestion]);
        txtQues.setBackgroundResource(R.drawable.atp__activity_player_image_money_curent);
        soundManager.playSound(LIST_SOUND_QUESTION_ID[numberQuestion], new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if (getActivity() != null) {
                            showPlayingWithQuestion();
                        }
                    }
                }, 800);

            }
        });

        dialog.show();
    }

    private void showDialogChampion() {
        final Dialog dialog = new Dialog(mContext, R.style.AudienceDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_game_champion);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final Button btnPlayQuitChampion = (Button) dialog.findViewById(R.id.btn_quit_champion);
        btnPlayQuitChampion.setVisibility(View.INVISIBLE);


        soundManager.playSound(R.raw.best_player, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnPlayQuitChampion.setVisibility(View.VISIBLE);
            }
        });

        btnPlayQuitChampion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (getActivity() != null) {
                    showDialogSaveHighScore(15);
                }
            }
        });
        dialog.show();
    }

    private void showDialogSaveHighScore(final int highScore) {
        if (!((GameAct)mContext).isHighScore(highScore)){
            return;
        }

        final Dialog dialog = new Dialog(mContext, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_save_high_score);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText edtName = (EditText) dialog.findViewById(R.id.edt_name_high_score_user);
        TextView txtLevel = (TextView) dialog.findViewById(R.id.txt_level_save_high_score);
        Button btnBoQua = (Button) dialog.findViewById(R.id.btn_save_high_score_bo_qua);
        Button btnSave = (Button) dialog.findViewById(R.id.btn_save_high_score_save);

        txtLevel.setText("Bạn đã vượt qua level " + highScore);

        btnBoQua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                saveHighScore.onSaveHighScore(name, highScore);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showDialogReady() {
        dialog = new Dialog(mContext, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ready);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnBoQua = (Button) dialog.findViewById(R.id.btn_bo_qua);
        Button btnSanSang = (Button) dialog.findViewById(R.id.btn_san_sang);

        btnBoQua.setOnClickListener(this);
        btnSanSang.setOnClickListener(this);

        dialog.show();
    }

    public void onBackPress() {
        if (soundManager != null) {
            soundManager.destroySound();
            mContext.finish();
        }
    }

    private class MyAsyncTask extends AsyncTask<Integer, Integer, Integer>{

        @Override
        protected Integer doInBackground(Integer... params) {
            int timeOut = 1;
            for (int i = params[0] - 1; i >= 0; i--){
                try {
                    Thread.sleep(1000);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (game.getSelectionCase() != -1){
                    timeOut = 0;
                    break;
                }
            }
            return timeOut;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            txtTime.setText("" + values[0]);
            progressTimeDown.setProgress(30 - values[0]);
        }

        @Override
        protected void onPostExecute(Integer timeOut) {
            if (timeOut == 1){
                if (getActivity() != null) {
                    showDialogTimeOut();
                }
            }
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        int help = game.getSelectHelp();
        switch (help){
            case Game.HELP_50_50:
                img5050.setImageResource(R.drawable.atp__activity_player_button_image_help_5050_x);
                img5050.setEnabled(false);
                break;
            case Game.HELP_CHANGE_QUES:
                imgChangeQuestion.setImageResource(R.drawable.atp__activity_player_button_image_help_change_question_x);
                imgChangeQuestion.setEnabled(false);
                break;
            case Game.HELP_AUDIENTS:
                imgAudients.setImageResource(R.drawable.atp__activity_player_button_image_help_audience_x);
                imgAudients.setEnabled(false);
                break;
            case Game.HELP_CALL_FRIEND:
                imgCallYourFriend.setImageResource(R.drawable.atp__activity_player_button_image_help_call_x);
                imgCallYourFriend.setEnabled(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public interface OnListenSaveHighScore{
        void onSaveHighScore(String name, int highLevel);
    }
}
