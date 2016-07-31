package com.thanhnv.ailatrieuphu;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thanh on 7/16/2016.
 */
public class DBManager {
    private static final String PATH_DB = Environment.getDataDirectory().getPath() + "/data/com.thanhnv.ailatrieuphu/databases";
    private static final String DB_NAME = "Question";
    private static final String TAG = "DBManager";
    private static final String LIST_COLUMN_NAME[] = {
            "question", "level", "casea", "caseb", "casec", "cased",
            "truecase"
    };

    private SQLiteDatabase mSQLiteDB;

    private static final String SQL_GET_QUESTIONS =
            "select * from (select * from Question order by random()) " +
            "group by level " +
            "order by level " +
            "limit 15 ;";

    private List<Question> listQuestion;

    public DBManager(Context context){
        copyDB(context);
    }

    private void openDB(){
        if ( mSQLiteDB == null || !mSQLiteDB.isOpen()) {
            mSQLiteDB = SQLiteDatabase.openDatabase(PATH_DB + "/" + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    public void closeDB(){
        if ( mSQLiteDB != null && mSQLiteDB.isOpen() ) {
            mSQLiteDB.close();
            mSQLiteDB = null;
        }
    }

    public boolean insertHighScore(String tableName, String columData[][]){
        openDB();
        ContentValues content = new ContentValues();
        for(int i = 0; i < columData.length; i++){
            content.put(columData[i][0], columData[i][1]);
        }


        int result = (int)mSQLiteDB.insert(tableName, null, content);
        return result > -1;
    }

    public void createQuestions(){
        openDB();
        listQuestion = new ArrayList<Question>();
        Cursor cursor = mSQLiteDB.rawQuery(SQL_GET_QUESTIONS, null);
        if(cursor != null){
            cursor.moveToFirst();
            int questionIndex = cursor.getColumnIndex("question");
            int levelIndex = cursor.getColumnIndex("level");
            int caseAIndex = cursor.getColumnIndex("casea");
            int caseBIndex = cursor.getColumnIndex("caseb");
            int caseCIndex = cursor.getColumnIndex("casec");
            int caseDIndex = cursor.getColumnIndex("cased");
            int trueCaseIndex = cursor.getColumnIndex("truecase");

            while (cursor.isAfterLast() == false){
                String question, caseA, caseB, caseC, caseD;
                int level, trueCase;

                question = cursor.getString(questionIndex);
                caseA = cursor.getString(caseAIndex);
                caseB = cursor.getString(caseBIndex);
                caseC = cursor.getString(caseCIndex);
                caseD = cursor.getString(caseDIndex);
                level = Integer.parseInt(cursor.getString(levelIndex));
                trueCase = Integer.parseInt(cursor.getString(trueCaseIndex));

                Question mQuestion = new Question(question, caseA, caseB, caseC, caseD, level, trueCase);
                listQuestion.add(mQuestion);

                cursor.moveToNext();
            }
        }

        for (int i = 0; i < listQuestion.size(); i++){
            Log.d(TAG, "Question " + i + " : " +listQuestion.get(i).toString());
        }

    }

    public List<Question> getListQuestion() {
        return listQuestion;
    }

    private void copyDB(Context context){
        new File(PATH_DB).mkdir();
        File dbFile = new File(PATH_DB + "/" + DB_NAME);
        if(dbFile.exists()){
            return;
        }
        try {
            dbFile.createNewFile();
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(DB_NAME);
            FileOutputStream outputStream = new FileOutputStream(dbFile);
            byte[] b = new byte[1024];
            int lenght = inputStream.read(b);
            while (lenght > 0){
                outputStream.write(b, 0, lenght);
                lenght = inputStream.read(b);
            }
            inputStream.close();
            outputStream.close();
            Log.d(TAG, "DB is copied");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public Question getNewQuestionAt(int quesNum) {
        openDB();
        int trueCase = listQuestion.get(quesNum-1).getTrueCase();
        Question resultQues = null;
        String sqlQuery =
                          "select * "
                        + "from question "
                        + "where level = " + quesNum + " and truecase != " + trueCase + " "
                        + "order by random() "
                        + "limit 1";
        Log.d("DBManager", sqlQuery);
        Cursor cursor = mSQLiteDB.rawQuery(sqlQuery, null);
        cursor.moveToFirst();
        String value[] = new String[LIST_COLUMN_NAME.length];
        for (int i = 0; i < value.length; i++){
            value[i] = cursor.getString(cursor.getColumnIndex(LIST_COLUMN_NAME[i]));
        }
        resultQues = new Question(value[0],
                                    value[2], value[3], value[4], value[5],
                                    Integer.parseInt(value[1]),
                                    Integer.parseInt(value[6]));
        return resultQues;
    }

    public String[] getHighScore() {
        String result[] = new String[3];
        String sql = "select name, score from highscore order by score desc limit 1;";
        openDB();
        Cursor cursor = mSQLiteDB.rawQuery(sql, null);
        if (cursor == null || cursor.getCount() == 0){
            return new String[]{"", "", ""};
        }
        cursor.moveToFirst();
        result[0] = cursor.getString(cursor.getColumnIndex("name"));
        result[1] = cursor.getString(cursor.getColumnIndex("Score"));
        cursor.close();
        switch (Integer.parseInt(result[1])){
            case 1:
                result[2] = "200.000";
                break;
            case 2:
                result[2] = "600.000";
                break;
            case 3:
                result[2] = "800.000";
                break;
            case 4:
                result[2] = "1.000.000";
                break;
            case 5:
                result[2] = "2.000.000";
                break;
            case 6:
                result[2] = "3.000.000";
                break;
            case 7:
                result[2] = "5.000.000";
                break;
            case 8:
                result[2] = "8.000.000";
                break;
            case 9:
                result[2] = "15.000.000";
                break;
            case 10:
                result[2] = "20.000.000";
                break;
            case 11:
                result[2] = "30.000.000";
                break;
            case 12:
                result[2] = "50.000.000";
                break;
            case 13:
                result[2] = "80.000.000";
                break;
            case 14:
                result[2] = "100.000.000";
                break;
            case 15:
                result[2] = "150.000.000";
                break;
            default:
                result[2] = "000.000";
                break;
        }

        Log.d("DBManager", "update ok " + result[0] + " " + result[1]);
        return result;
    }

    public void saveHighScore(String name, int highLevel) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("Score", highLevel);

        mSQLiteDB.update("highScore", cv, null, null);
        Log.d("DBManager", "update ok " + name + " " + highLevel);
    }

    public boolean isHighScore(int highScore) {
        String sql = "select score from highscore order by score desc limit 1;";
        openDB();
        Cursor cursor = mSQLiteDB.rawQuery(sql, null);
        if (cursor == null || cursor.getCount() == 0){
            return true;
        }
        cursor.moveToFirst();
        int mHighScore = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Score")));
        cursor.close();
        return highScore >= mHighScore;
    }
}
