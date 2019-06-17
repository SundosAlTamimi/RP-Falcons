package com.tamimi.sundos.restpos.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ForceQuestions {
    private int questionNo;
    private String questionText;
    private int multipleAnswer;
    private String answer;

    public ForceQuestions(){

    }

    public ForceQuestions(int questionNo, String questionText, int multipleAnswer, String answer) {
        this.questionNo = questionNo;
        this.questionText = questionText;
        this.multipleAnswer = multipleAnswer;
        this.answer = answer;
    }

    public int getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(int questionNo) {
        this.questionNo = questionNo;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getMultipleAnswer() {
        return multipleAnswer;
    }

    public void setMultipleAnswer(int multipleAnswer) {
        this.multipleAnswer = multipleAnswer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public JSONObject getJSONObject() { // for kitchen
        JSONObject obj = new JSONObject();
        try {
            obj.put("QUESTION_NO", questionNo);
            obj.put("QUESTION_TEXT", questionText);
            obj.put("MULTIPLE_ANSWER", multipleAnswer);
            obj.put("ANSWER", answer);

        } catch (JSONException e) {
            Log.e("Tag", "JSONException");
        }
        return obj;
    }
}
