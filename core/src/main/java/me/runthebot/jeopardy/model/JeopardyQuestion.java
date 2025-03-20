package me.runthebot.jeopardy.model;

public class JeopardyQuestion {
    private int value;
    private String question;
    private String answer;
    private boolean answered = false;

    public JeopardyQuestion() {}

    public JeopardyQuestion(int value, String question, String answer) {
        this.value = value;
        this.question = question;
        this.answer = answer;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }
}
