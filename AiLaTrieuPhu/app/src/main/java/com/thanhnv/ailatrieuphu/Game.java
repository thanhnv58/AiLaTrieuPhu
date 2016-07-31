package com.thanhnv.ailatrieuphu;

import java.util.List;
import java.util.Random;

/**
 * Created by thanh on 7/20/2016.
 */
public class Game {
    private List<Question> listQuestion;
    private int currentQuestion, score;
    private String mNamePlayer;
    private int selectionCase, selectHelp;
    private Question newQuestion;

    private static final int SCORE[] = {
            0, 200000, 600000, 800000, 1000000, 2000000,
            3000000, 5000000, 8000000, 15000000, 20000000,
            30000000, 50000000, 80000000, 100000000, 150000000
    };

    public static final int HELP_50_50 = 2, HELP_CHANGE_QUES = 1, HELP_CALL_FRIEND = 3, HELP_AUDIENTS = 4;

    public Game(String namePlayer){
        currentQuestion = -1;
        score = 0;
        mNamePlayer = namePlayer;
        selectionCase = 1;
        selectHelp = -1;
    }


    public void setListQuestion(List<Question> list){
        listQuestion = list;
    }

    public Question getQuestion(){
        if (currentQuestion > 14){
            return null;
        }
        return listQuestion.get(currentQuestion);
    }

    public int getScore() {
        return score;
    }

    public String getmNamePlayer() {
        return mNamePlayer;
    }

    public void createNewQuestion(){
        if(currentQuestion < 15){
            currentQuestion += 1;
            score = SCORE[currentQuestion];
            selectionCase = -1;
        } else {
            return;
        }
    }

    public void setSelectAnswer(int selection) {
        selectionCase = selection;
    }

    public int getSelectionCase() {
        return selectionCase;
    }

    public boolean checkAnswer(){
        if (selectionCase == listQuestion.get(currentQuestion).getTrueCase()){
            return true;
        }
        return false;
    }

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public int[] select5050() {
        int trueCase = listQuestion.get(currentQuestion).getTrueCase() - 1;
        int remove1, remove2;
        Random rd = new Random();
        int indexRD = rd.nextInt(3);
        switch (indexRD){
            case 0:
                remove1 = trueCase + 1;
                remove2 = trueCase - 1;
                break;
            case 1:
                remove1 = trueCase + 2;
                remove2 = trueCase - 1;
                break;
            case 2:
                remove1 = trueCase + 1;
                remove2 = trueCase + 2;
                break;
            default:
                remove1 = 0;
                remove2 = 0;
                break;
        }
        if (remove1 < 0){
            remove1 = remove1 + 4;
        }
        if (remove1 > 3){
            remove1 = remove1 - 4;
        }

        if (remove2 < 0){
            remove2 = remove2 + 4;
        }
        if (remove2 > 3){
            remove2 = remove2 - 4;
        }

        return new int[]{remove1, remove2};
    }

    public int getSelectHelp() {
        return selectHelp;
    }

    public void setSelectHelp(int selectHelp) {
        this.selectHelp = selectHelp;
        selectionCase = -2;
    }

    public void setNewQuestion(Question newQues){
        newQuestion = newQues;
        listQuestion.set(currentQuestion, newQuestion);
    }

    public int[] getAudiencePercent() {
        Random rd = new Random();
        int valueRandom[] = {5, 10, 5};
        int valueRandomBegin[] = {36, 21, 16};

        int result[] = {0, 0, 0, 0};
        int trueCase = listQuestion.get(currentQuestion).getTrueCase() - 1;
        result[trueCase] = rd.nextInt(valueRandom[0]) + valueRandomBegin[0];
        for (int i = 1; i < 3; i++){
            int temp = trueCase + i;
            if(temp > 3){
                temp -= 4;
            }
            result[temp] = rd.nextInt(valueRandom[i]) + valueRandomBegin[i];
        }
        int sumCurrent = 0, index = 0;
        for (int i = 0; i < 4; i++){
            if (result[i] > 0){
                sumCurrent += result[i];
            } else {
                index = i;
            }
        }

        result[index] = 100 - sumCurrent;

        return result;
    }

    public int getAnserSuggest() {
        Random rd = new Random();
        int i = rd.nextInt(10);
        int trueCase = listQuestion.get(currentQuestion).getTrueCase();
        if (i > 2){
            return trueCase;
        } else {
            return -1;
        }
    }
}
