package com.thanhnv.ailatrieuphu;

/**
 * Created by thanh on 7/16/2016.
 */
public class Question {
    private String question, caseA, caseB, caseC, caseD;
    private int level, trueCase;

    public Question(String question, String caseA, String caseB, String caseC, String caseD, int level, int trueCase) {
        this.question = question;
        this.caseA = caseA;
        this.caseB = caseB;
        this.caseC = caseC;
        this.caseD = caseD;
        this.level = level;
        this.trueCase = trueCase;
    }

    public String getQuestion() {
        return question;
    }

    public String getCaseA() {
        return caseA;
    }

    public String getCaseB() {
        return caseB;
    }

    public String getCaseC() {
        return caseC;
    }

    public String getCaseD() {
        return caseD;
    }

    public int getLevel() {
        return level;
    }

    public int getTrueCase() {
        return trueCase;
    }

    public boolean checkAnswer(int selectionAnswer){
        if(selectionAnswer == trueCase){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        return question + "\n"
                + caseA + " - case a\n"
                + caseB + " - case b\n"
                + caseC + " - case c\n"
                + caseD + " - case d\n"
                + trueCase + " - true case\n"
                + level + " - level\n";
    }
}
