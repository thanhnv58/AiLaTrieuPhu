package com.thanhnv.ailatrieuphu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DBManager dbManager;
    private SplashFragment splashFragment;
    private MainMenuFragment mainMenuFragment;
    private int flash = -1;
//    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initData() {
        splashFragment = new SplashFragment();
        mainMenuFragment = new MainMenuFragment();

        if (flash == -1){
            showSplashFragment();
            flash = 1;
        } else {
            showMainFragment();
        }
    }

    private void initView() {
//        dbManager = new DBManager(this);
//        dbManager.createQuestions();
//        String[][] values = {
//                {"name", "thanh"},
//                {"score", "14"}
//        };
//        if (dbManager.insertHighScore("highScore", values)){
//            Toast.makeText(this, "insert successfully !!", Toast.LENGTH_SHORT).show();
//        }
//        game = new Game("Nguyen Thanh");

    }


    public void showMainFragment() {
        mainMenuFragment.setDestroy();
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_main_menu, mainMenuFragment)
                .commitAllowingStateLoss();
    }

    public void showSplashFragment(){
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_main_menu, splashFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (mainMenuFragment != null){
            mainMenuFragment.onBackPress();
        }
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    public String[] getHighScore() {
        dbManager = new DBManager(this);
        String result[] = dbManager.getHighScore();
        dbManager.closeDB();
        return result;
    }
}
