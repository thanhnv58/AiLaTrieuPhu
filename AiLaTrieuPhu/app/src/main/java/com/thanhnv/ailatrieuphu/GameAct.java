package com.thanhnv.ailatrieuphu;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by thanh on 7/20/2016.
 */
public class GameAct extends FragmentActivity implements GamePlayFragment.OnListenSaveHighScore {


    private Game game;
    private DBManager dbManager;

    private IntroducGameFragment introducGameFragment;
    private GamePlayFragment gamePlayFragment;
    private GameQuestionFragment gameQuestionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initData();
//        showIntroducGameFragment();
        showGameQuestionFragment();
    }

    private void initData() {
        game = new Game("Nguyen Thanh");
        dbManager = new DBManager(this);
        dbManager.createQuestions();
        game.setListQuestion(dbManager.getListQuestion());

        introducGameFragment = new IntroducGameFragment();
        gamePlayFragment = new GamePlayFragment();
        gameQuestionFragment = new GameQuestionFragment();

        gamePlayFragment.setOnListenSaveHighScore(this);
    }

    private void showIntroducGameFragment() {
//        Bundle bundle = new Bundle();
//        bundle.putString("NAME_PLAYER", game.getmNamePlayer());
//        introducGameFragment.setArguments(bundle);

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_bottom);
        transaction.replace(R.id.fragment_game, introducGameFragment)
                .commitAllowingStateLoss();
    }

    public void showGameQuestionFragment(){
//        Bundle bundle = new Bundle();
//        bundle.putInt("QUESTION", index);
//        gameQuestionFragment.setArguments(bundle);
        int index = game.getCurrentQuestion();

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (index == -1){
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        } else {
            removeGamePlayingFragment();
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left);
        }

        transaction.replace(R.id.fragment_game, gameQuestionFragment)
                .commitAllowingStateLoss();
    }

    public void showGamePlayingFragment() {
//        Bundle bundle;
//        if (gamePlayFragment.getArguments() == null) {
//            bundle = new Bundle();
//            bundle.putString("NAME_PLAYER", game.getmNamePlayer());
//            bundle.putInt("QUESTION", question);
//            gamePlayFragment.setArguments(bundle);
//        } else {
//            bundle = gamePlayFragment.getArguments();
//            bundle.putInt("QUESTION", question);
//        }
//

        removeGameQuestionFragment();

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);

        transaction.replace(R.id.fragment_game, gamePlayFragment)
                .commitAllowingStateLoss();
    }

    private void removeGameQuestionFragment(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.remove(gameQuestionFragment)
                .commitAllowingStateLoss();
    }

    private void removeGamePlayingFragment(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.remove(gamePlayFragment)
                .commitAllowingStateLoss();
    }


    public Game getGame(){
        return game;
    }

    public void changeQuestion(int quesNum){
        Question question = dbManager.getNewQuestionAt(quesNum);
        game.setNewQuestion(question);
    }

    @Override
    public boolean isDestroyed() {
        dbManager.closeDB();
        return super.isDestroyed();
    }

    @Override
    public void onBackPressed() {
        if (gameQuestionFragment != null){
            gameQuestionFragment.onBackPress();
        }
        if (introducGameFragment != null){
            introducGameFragment.onBackPress();
        }

        if (gamePlayFragment != null){
            gamePlayFragment.onBackPress();
        }


        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveHighScore(String name, int highLevel) {
        dbManager.saveHighScore(name, highLevel);
    }

    public boolean isHighScore(int highScore) {
        return dbManager.isHighScore(highScore);
    }
}
